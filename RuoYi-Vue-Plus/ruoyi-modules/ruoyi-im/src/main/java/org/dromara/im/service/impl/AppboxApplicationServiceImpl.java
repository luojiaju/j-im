package org.dromara.im.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.im.domain.AppUser;
import org.dromara.im.domain.AppboxApplication;
import org.dromara.im.domain.bo.AppboxApplicationBo;
import org.dromara.im.domain.vo.AppboxApplicationVo;
import org.dromara.im.mapper.AppUserMapper;
import org.dromara.im.mapper.AppboxApplicationMapper;
import org.dromara.im.mapper.AppboxClassesMapper;
import org.dromara.im.service.IAppboxApplicationService;
import org.dromara.im.utils.SysUserUtils;
import org.dromara.system.domain.SysOss;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.SysOssMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dromara.im.constant.ImStringConstants.PV_INCREMENT_CONTROL;
import static org.dromara.im.utils.SysUserUtils.getCurrentUserId;
import static org.dromara.im.utils.SysUserUtils.getCurrentUserIdToString;

/**
 * 创建应用or频道Service业务层处理
 *
 * @author luojiaju
 * @date 2024-01-04
 */
@RequiredArgsConstructor
@Service
public class AppboxApplicationServiceImpl implements IAppboxApplicationService {

    private final AppboxApplicationMapper baseMapper;
    private final AppboxClassesMapper appboxClassesMapper;
    private final SysOssMapper sysOssMapper;
    private final AppUserMapper appUserMapper;

    /**
     * 查询创建应用or频道
     */

    @Override
    public AppboxApplicationVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询创建应用or频道列表
     */
    @Override
    public List<AppboxApplicationVo> queryList(AppboxApplicationBo bo) {
        LambdaQueryWrapper<AppboxApplication> lqw = buildQueryWrapper(bo);

        lqw.orderBy(true, false, AppboxApplication::getAppName, AppboxApplication::getCreateTime);
        List<AppboxApplicationVo> vos = baseMapper.selectVoList(lqw);

        extractedCoverUrl(vos);
        return vos;
    }

    // 提取封面
    private void extractedCoverUrl(List<AppboxApplicationVo> vos) {
        // 获取所有 coverId
        List<String> coverIds = vos.stream()
            .filter(Objects::nonNull)
            .map(AppboxApplicationVo::getCover)
            .collect(Collectors.toList());

        if (!coverIds.isEmpty()) {
            // 批量查询 sysOss
            List<SysOss> sysOssList = sysOssMapper.selectBatchIds(coverIds);

            // 构建 coverId 到 SysOss 的映射
            Map<Long, SysOss> coverIdToSysOssMap = sysOssList.stream()
                .collect(Collectors.toMap(SysOss::getOssId, Function.identity()));

            // 更新 AppboxApplicationVo 的 coverUrl
            vos.forEach(vo -> {
                if (vo.getCover() != null && !vo.getCover().isEmpty()) {
                    SysOss sysOss = coverIdToSysOssMap.get(Long.valueOf(vo.getCover()));
                    if (sysOss != null) {
                        vo.setCoverUrl(sysOss.getUrl());
                    }
                }
            });
        }
    }

    private LambdaQueryWrapper<AppboxApplication> buildQueryWrapper(AppboxApplicationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AppboxApplication> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getParentId() != null, AppboxApplication::getParentId, bo.getParentId());
        lqw.eq(bo.getClassesId() != null, AppboxApplication::getClassesId, bo.getClassesId());
        lqw.eq(bo.getConfigId() != null, AppboxApplication::getConfigId, bo.getConfigId());
        lqw.like(StringUtils.isNotBlank(bo.getAppName()), AppboxApplication::getAppName, bo.getAppName());
        lqw.eq(StringUtils.isNotBlank(bo.getIconUrl()), AppboxApplication::getIconUrl, bo.getIconUrl());
        lqw.between(params.get("beginPopularity") != null && params.get("endPopularity") != null,
            AppboxApplication::getPopularity, params.get("beginPopularity"), params.get("endPopularity"));
        lqw.between(params.get("beginPv") != null && params.get("endPv") != null,
            AppboxApplication::getPv, params.get("beginPv"), params.get("endPv"));
        lqw.between(params.get("beginUv") != null && params.get("endUv") != null,
            AppboxApplication::getUv, params.get("beginUv"), params.get("endUv"));
        lqw.eq(bo.getChannelStatus() != null, AppboxApplication::getChannelStatus, bo.getChannelStatus());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            AppboxApplication::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增创建应用or频道
     */
    @DSTransactional
    @Override
    public Boolean insertByBo(AppboxApplicationBo bo) {
        bo.setPermission(0);
        AppboxApplication add = MapstructUtils.convert(bo, AppboxApplication.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        assert add != null;
        validEntityAfterSave(add);
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    // 添加新应用默认频道配置
    private void validEntityAfterSave(AppboxApplication add) {
        if (add.getChannelStatus() == 1) {
            // 是应用,添加默认频道
            ArrayList<AppboxApplication> applications = new ArrayList<>();

            AppboxApplication application = new AppboxApplication();
            application.setId(null);
            application.setParentId(add.getId());
            application.setAppName("角色");
            application.setIconUrl("""
                <svg width="30" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 448 512"><path d="M225.82 0c.26.15 216.57 124.51 217.12 124.72c3 1.18 3.7 3.46 3.7 6.56q-.11 125.17 0 250.36a5.88 5.88 0 0 1-3.38 5.78c-21.37 12-207.86 118.29-218.93 124.58h-3C142 466.34 3.08 386.56 2.93 386.48a3.29 3.29 0 0 1-1.88-3.24c0-.87 0-225.94-.05-253.1a5 5 0 0 1 2.93-4.93C27.19 112.11 213.2 6 224.07 0zM215.4 20.42l-.22-.16Q118.06 75.55 21 130.87c0 .12.08.23.13.35l30.86 11.64c-7.71 6-8.32 6-10.65 5.13c-.1 0-24.17-9.28-26.8-10v230.43c.88-1.41 64.07-110.91 64.13-111c1.62-2.82 3-1.92 9.12-1.52c1.4.09 1.48.22.78 1.42c-41.19 71.33-36.4 63-67.48 116.94c-.81 1.4-.61 1.13 1.25 1.13h186.5c1.44 0 1.69-.23 1.7-1.64v-8.88c0-1.34 2.36-.81-18.37-1c-7.46-.07-14.14-3.22-21.38-12.7c-7.38-9.66-14.62-19.43-21.85-29.21c-2.28-3.08-3.45-2.38-16.76-2.38c-1.75 0-1.78 0-1.76 1.82c.29 26.21.15 25.27 1 32.66c.52 4.37 2.16 4.2 9.69 4.81c3.14.26 3.88 4.08.52 4.92c-1.57.39-31.6.51-33.67-.1a2.42 2.42 0 0 1 .3-4.73c3.29-.76 6.16.81 6.66-4.44c1.3-13.66 1.17-9 1.1-79.42c0-10.82-.35-12.58-5.36-13.55c-1.22-.24-3.54-.16-4.69-.55c-2.88-1-2-4.84 1.77-4.85c33.67 0 46.08-1.07 56.06 4.86c7.74 4.61 12 11.48 12.51 20.4c.88 14.59-6.51 22.35-15 32.59a1.46 1.46 0 0 0 0 2.22c2.6 3.25 5 6.63 7.71 9.83c27.56 33.23 24.11 30.54 41.28 33.06c.89.13 1-.42 1-1.15v-11c0-1 .32-1.43 1.41-1.26a72.37 72.37 0 0 0 23.58-.3c1.08-.15 1.5.2 1.48 1.33c0 .11.88 26.69.87 26.8c-.05 1.52.67 1.62 1.89 1.62h186.71Q386.51 304.6 346 234.33c2.26-.66-.4 0 6.69-1.39c2-.39 2.05-.41 3.11 1.44c7.31 12.64 77.31 134 77.37 134.06V138c-1.72.5-103.3 38.72-105.76 39.68c-1.08.42-1.55.2-1.91-.88c-.63-1.9-1.34-3.76-2.09-5.62c-.32-.79-.09-1.13.65-1.39c.1 0 95.53-35.85 103-38.77c-65.42-37.57-130.56-75-196-112.6l86.82 150.39l-.28.33c-9.57-.9-10.46-1.6-11.8-3.94c-1-1.69-73.5-127.71-82-142.16c-9.1 14.67-83.56 146.21-85.37 146.32c-2.93.17-5.88.08-9.25.08q43.25-74.74 86.18-149zm51.93 129.92a37.68 37.68 0 0 0 5.54-.85c1.69-.3 2.53.2 2.6 1.92c0 .11.07 19.06-.86 20.45s-1.88 1.22-2.6-.19c-5-9.69 6.22-9.66-39.12-12c-.7 0-1 .23-1 .93c0 .13 3.72 122 3.73 122.11c0 .89.52 1.2 1.21 1.51a83.92 83.92 0 0 1 8.7 4.05c7.31 4.33 11.38 10.84 12.41 19.31c1.44 11.8-2.77 35.77-32.21 37.14c-2.75.13-28.26 1.08-34.14-23.25c-4.66-19.26 8.26-32.7 19.89-36.4a2.45 2.45 0 0 0 2-2.66c.1-5.63 3-107.1 3.71-121.35c.05-1.08-.62-1.16-1.35-1.15c-32.35.52-36.75-.34-40.22 8.52c-2.42 6.18-4.14 1.32-3.95.23q1.59-9 3.31-18c.4-2.11 1.43-2.61 3.43-1.86c5.59 2.11 6.72 1.7 37.25 1.92c1.73 0 1.78-.08 1.82-1.85c.68-27.49.58-22.59 1-29.55a2.69 2.69 0 0 0-1.63-2.8c-5.6-2.91-8.75-7.55-8.9-13.87c-.35-14.81 17.72-21.67 27.38-11.51c6.84 7.19 5.8 18.91-2.45 24.15a4.35 4.35 0 0 0-2.22 4.34c0 .59-.11-4.31 1 30.05c0 .9.43 1.12 1.24 1.11c.1 0 23-.09 34.47-.37zM68.27 141.7c19.84-4.51 32.68-.56 52.49 1.69c2.76.31 3.74 1.22 3.62 4c-.21 5-1.16 22.33-1.24 23.15a2.65 2.65 0 0 1-1.63 2.34c-4.06 1.7-3.61-4.45-4-7.29c-3.13-22.43-73.87-32.7-74.63 25.4c-.31 23.92 17 53.63 54.08 50.88c27.24-2 19-20.19 24.84-20.47a2.72 2.72 0 0 1 3 3.36c-1.83 10.85-3.42 18.95-3.45 19.15c-1.54 9.17-86.7 22.09-93.35-42.06c-2.71-25.85 10.44-53.37 40.27-60.15zm80 87.67h-19.49a2.57 2.57 0 0 1-2.66-1.79c2.38-3.75 5.89.92 5.86-6.14c-.08-25.75.21-38 .23-40.1c0-3.42-.53-4.65-3.32-4.94c-7-.72-3.11-3.37-1.11-3.38c11.84-.1 22.62-.18 30.05.72c8.77 1.07 16.71 12.63 7.93 22.62c-2 2.25-4 4.42-6.14 6.73c.95 1.15 6.9 8.82 17.28 19.68c2.66 2.78 6.15 3.51 9.88 3.13a2.21 2.21 0 0 0 2.23-2.12c.3-3.42.26 4.73.45-40.58c0-5.65-.34-6.58-3.23-6.83c-3.95-.35-4-2.26-.69-3.37l19.09-.09c.32 0 4.49.53 1 3.38c0 .05-.16 0-.24 0c-3.61.26-3.94 1-4 4.62c-.27 43.93.07 40.23.41 42.82c.11.84.27 2.23 5.1 2.14c2.49 0 3.86 3.37 0 3.4c-10.37.08-20.74 0-31.11.07c-10.67 0-13.47-6.2-24.21-20.82c-1.6-2.18-8.31-2.36-8.2-.37c.88 16.47 0 17.78 4 17.67c4.75-.1 4.73 3.57.83 3.55zm275-10.15c-1.21 7.13.17 10.38-5.3 10.34c-61.55-.42-47.82-.22-50.72-.31a18.4 18.4 0 0 1-3.63-.73c-2.53-.6 1.48-1.23-.38-5.6c-1.43-3.37-2.78-6.78-4.11-10.19a1.94 1.94 0 0 0-2-1.44a138 138 0 0 0-14.58.07a2.23 2.23 0 0 0-1.62 1.06c-1.58 3.62-3.07 7.29-4.51 11c-1.27 3.23 7.86 1.32 12.19 2.16c3 .57 4.53 3.72.66 3.73H322.9c-2.92 0-3.09-3.15-.74-3.21a6.3 6.3 0 0 0 5.92-3.47c1.5-3 2.8-6 4.11-9.09c18.18-42.14 17.06-40.17 18.42-41.61a1.83 1.83 0 0 1 3 0c2.93 3.34 18.4 44.71 23.62 51.92c2 2.7 5.74 2 6.36 2c3.61.13 4-1.11 4.13-4.29c.09-1.87.08 1.17.07-41.24c0-4.46-2.36-3.74-5.55-4.27c-.26 0-2.56-.63-.08-3.06c.21-.2-.89-.24 21.7-.15c2.32 0 5.32 2.75-1.21 3.45a2.56 2.56 0 0 0-2.66 2.83c-.07 1.63-.19 38.89.29 41.21a3.06 3.06 0 0 0 3.23 2.43c13.25.43 14.92.44 16-3.41c1.67-5.78 4.13-2.52 3.73-.19zm-104.72 64.37c-4.24 0-4.42-3.39-.61-3.41c35.91-.16 28.11.38 37.19-.65c1.68-.19 2.38.24 2.25 1.89c-.26 3.39-.64 6.78-1 10.16c-.25 2.16-3.2 2.61-3.4-.15c-.38-5.31-2.15-4.45-15.63-5.08c-1.58-.07-1.64 0-1.64 1.52V304c0 1.65 0 1.6 1.62 1.47c3.12-.25 10.31.34 15.69-1.52c.47-.16 3.3-1.79 3.07 1.76c0 .21-.76 10.35-1.18 11.39c-.53 1.29-1.88 1.51-2.58.32c-1.17-2 0-5.08-3.71-5.3c-15.42-.9-12.91-2.55-12.91 6c0 12.25-.76 16.11 3.89 16.24c16.64.48 14.4 0 16.43-5.71c.84-2.37 3.5-1.77 3.18.58c-.44 3.21-.85 6.43-1.23 9.64c0 .36-.16 2.4-4.66 2.39c-37.16-.08-34.54-.19-35.21-.31c-2.72-.51-2.2-3 .22-3.45c1.1-.19 4 .54 4.16-2.56c2.44-56.22-.07-51.34-3.91-51.33zm-.41-109.52c2.46.61 3.13 1.76 2.95 4.65c-.33 5.3-.34 9-.55 9.69c-.66 2.23-3.15 2.12-3.34-.27c-.38-4.81-3.05-7.82-7.57-9.15c-26.28-7.73-32.81 15.46-27.17 30.22c5.88 15.41 22 15.92 28.86 13.78c5.92-1.85 5.88-6.5 6.91-7.58c1.23-1.3 2.25-1.84 3.12 1.1c0 .1.57 11.89-6 12.75c-1.6.21-19.38 3.69-32.68-3.39c-21-11.19-16.74-35.47-6.88-45.33c14-14.06 39.91-7.06 42.32-6.47zM289.8 280.14c3.28 0 3.66 3 .16 3.43c-2.61.32-5-.42-5 5.46c0 2-.19 29.05.4 41.45c.11 2.29 1.15 3.52 3.44 3.65c22 1.21 14.95-1.65 18.79-6.34c1.83-2.24 2.76.84 2.76 1.08c.35 13.62-4 12.39-5.19 12.4l-38.16-.19c-1.93-.23-2.06-3-.42-3.38c2-.48 4.94.4 5.13-2.8c1-15.87.57-44.65.34-47.81c-.27-3.77-2.8-3.27-5.68-3.71c-2.47-.38-2-3.22.34-3.22c1.45-.02 17.97-.03 23.09-.02zm-31.63-57.79c.07 4.08 2.86 3.46 6 3.58c2.61.1 2.53 3.41-.07 3.43c-6.48 0-13.7 0-21.61-.06c-3.84 0-3.38-3.35 0-3.37c4.49 0 3.24 1.61 3.41-45.54c0-5.08-3.27-3.54-4.72-4.23c-2.58-1.23-1.36-3.09.41-3.15c1.29 0 20.19-.41 21.17.21s1.87 1.65-.42 2.86c-1 .52-3.86-.28-4.15 2.47c0 .21-.82 1.63-.07 43.8zm-36.91 274.27a2.93 2.93 0 0 0 3.26 0c17-9.79 182-103.57 197.42-112.51c-.14-.43 11.26-.18-181.52-.27c-1.22 0-1.57.37-1.53 1.56c0 .1 1.25 44.51 1.22 50.38a28.33 28.33 0 0 1-1.36 7.71c-.55 1.83.38-.5-13.5 32.23c-.73 1.72-1 2.21-2-.08c-4.19-10.34-8.28-20.72-12.57-31a23.6 23.6 0 0 1-2-10.79c.16-2.46.8-16.12 1.51-48c0-1.95 0-2-2-2h-183c2.58 1.63 178.32 102.57 196 112.76zm-90.9-188.75c0 2.4.36 2.79 2.76 3c11.54 1.17 21 3.74 25.64-7.32c6-14.46 2.66-34.41-12.48-38.84c-2-.59-16-2.76-15.94 1.51c.05 8.04.01 11.61.02 41.65zm105.75-15.05c0 2.13 1.07 38.68 1.09 39.13c.34 9.94-25.58 5.77-25.23-2.59c.08-2 1.37-37.42 1.1-39.43c-14.1 7.44-14.42 40.21 6.44 48.8a17.9 17.9 0 0 0 22.39-7.07c4.91-7.76 6.84-29.47-5.43-39a2.53 2.53 0 0 1-.36.12zm-12.28-198c-9.83 0-9.73 14.75-.07 14.87s10.1-14.88.07-14.91zm-80.15 103.83c0 1.8.41 2.4 2.17 2.58c13.62 1.39 12.51-11 12.16-13.36c-1.69-11.22-14.38-10.2-14.35-7.81c.05 4.5-.03 13.68.02 18.59zm212.32 6.4l-6.1-15.84c-2.16 5.48-4.16 10.57-6.23 15.84z" fill="currentColor"></path></svg>
                """);
            application.setPopularity(0L);
            application.setPv(1L);
            application.setUv(1L);
            application.setChannelStatus(2L);
            application.setPermission(1);


            AppboxApplication application1 = new AppboxApplication();
            BeanUtils.copyProperties(application, application1);
            application1.setAppName("视频");
            application1.setIconUrl("""
                <svg width="30" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 24 24"><g fill="none"><path d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 1.643.397 3.23 1.145 4.65L2.029 20.94a.85.85 0 0 0 1.036 1.036l4.29-1.117A9.96 9.96 0 0 0 12 22c5.523 0 10-4.477 10-10zM12 8a2 2 0 0 1 2 2v4a2 2 0 0 1-2 2H9a2 2 0 0 1-2-2v-4a2 2 0 0 1 2-2h3zm3 5.162v-2.324l1.734-1.642A.75.75 0 0 1 18 9.741v4.518a.75.75 0 0 1-1.266.545L15 13.162z" fill="currentColor"></path></g></svg>
                """);
            application1.setPermission(3);

            AppboxApplication application2 = new AppboxApplication();
            BeanUtils.copyProperties(application, application2);
            application2.setAppName("发布&通知");
            application2.setIconUrl("""
                <svg width="30" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 24 24"><path d="M23 12l-2.44-2.78l.34-3.68l-3.61-.82l-1.89-3.18L12 3L8.6 1.54L6.71 4.72l-3.61.81l.34 3.68L1 12l2.44 2.78l-.34 3.69l3.61.82l1.89 3.18L12 21l3.4 1.46l1.89-3.18l3.61-.82l-.34-3.68L23 12zm-10 5h-2v-2h2v2zm0-4h-2V7h2v6z" fill="currentColor"></path></svg>
                """);
            application2.setPermission(1);


            AppboxApplication application3 = new AppboxApplication();
            BeanUtils.copyProperties(application, application3);
            application3.setAppName("语音");
            application3.setIconUrl("""
                <svg width="30" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 32 32"><path d="M26 30h-2v-3h-4a5.005 5.005 0 0 1-5-5v-1.28l-2.316-.771a1 1 0 0 1-.541-1.463L15 13.723V11a9.01 9.01 0 0 1 9-9h5v2h-5a7.008 7.008 0 0 0-7 7v3a1 1 0 0 1-.143.514L14.5 18.446l1.817.605A1 1 0 0 1 17 20v2a3.003 3.003 0 0 0 3 3h5a1 1 0 0 1 1 1z" fill="currentColor"></path><path d="M19 12h4v2h-4z" fill="currentColor"></path><path d="M9.332 25.217a7 7 0 0 1 0-10.434l1.334 1.49a5 5 0 0 0 0 7.453z" fill="currentColor"></path><path d="M6.4 28.8a11.002 11.002 0 0 1 0-17.6l1.2 1.6a9 9 0 0 0 0 14.401z" fill="currentColor"></path></svg>
                """);
            application3.setPermission(3);

            AppboxApplication application4 = new AppboxApplication();
            BeanUtils.copyProperties(application, application4);
            application4.setAppName("群聊");
            application4.setIconUrl("""
                <svg width="30" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 16 16"><g fill="none"><path d="M3.5 3A2.5 2.5 0 0 0 1 5.5v5A2.5 2.5 0 0 0 3.5 13h9a2.5 2.5 0 0 0 2.5-2.5v-5A2.5 2.5 0 0 0 12.5 3h-9zM10 6.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5zm.5 2.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1zM6 6a1 1 0 1 1-2 0a1 1 0 0 1 2 0zM3.75 8h2.5a.75.75 0 0 1 .75.75v.75S7 11 5 11S3 9.5 3 9.5v-.75A.75.75 0 0 1 3.75 8zM8.5 6.25a.75.75 0 1 1-1.5 0a.75.75 0 0 1 1.5 0zm-.555 3.701a2.402 2.402 0 0 0 .053-.39L8 9.535V8.75c0-.268-.06-.523-.168-.75H8.5a.5.5 0 0 1 .5.5V9a1 1 0 0 1-1 1h-.066l.01-.049z" fill="currentColor"></path></g></svg>
                """);
            application4.setPermission(3);


            AppboxApplication application5 = new AppboxApplication();
            BeanUtils.copyProperties(application, application5);
            application5.setAppName("反馈");
            application5.setIconUrl("""
                <svg width="30" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 16 16"><g fill="none"><path d="M9.5 1.5a1 1 0 0 0-1 1v2a1 1 0 0 0 1 1V7l1.8-1.5h2.2a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1h-4zM5 4a2 2 0 1 0 0 4a2 2 0 0 0 0-4zm2.5 5h-5A1.5 1.5 0 0 0 1 10.5c0 1.116.459 2.01 1.212 2.615C2.953 13.71 3.947 14 5 14c1.053 0 2.047-.29 2.788-.885C8.54 12.51 9 11.616 9 10.5A1.5 1.5 0 0 0 7.5 9z" fill="currentColor"></path></g></svg>
                """);
            application5.setPermission(3);


            applications.add(application);
            applications.add(application1);
            applications.add(application2);
            applications.add(application3);
            applications.add(application4);
            applications.add(application5);
            this.baseMapper.insertBatch(applications);
        }
    }

    /**
     * 修改创建应用or频道
     */
    @Override
    public Boolean updateByBo(AppboxApplicationBo bo) {
        AppboxApplication update = MapstructUtils.convert(bo, AppboxApplication.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AppboxApplication entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除创建应用or频道
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if (isValid) {
            // TODO 删除业务逻辑
        }
        boolean deleted = baseMapper.deleteBatchIds(ids) > 0;
        LambdaQueryWrapper<AppboxApplication> queryWrapper = new LambdaQueryWrapper<>();
        for (String appId : ids) {
            queryWrapper.eq(true, AppboxApplication::getParentId, appId);
            baseMapper.delete(queryWrapper);
            queryWrapper.clear();
        }
        return deleted;
    }

    @Override
    public List<AppboxApplicationVo> queryAppByUser() {
        List<AppboxApplicationVo> applicationVos = baseMapper.queryAppByUserId(getCurrentUserId());
        this.extractedCoverUrl(applicationVos);
        return applicationVos;
    }

    @Override
    public List<AppboxApplicationVo> getTreeInfo(String id) {
        List<AppboxApplicationVo> treeInfo = baseMapper.getTreeInfo(id);
        this.extractedCoverUrl(treeInfo);
        return treeInfo;
    }


    @Override
    public Page<SysUserVo> getUsersUnderTheApplication(Serializable appId,
                                                       SysUserBo sysUserBo, PageQuery pageQuery) {
        Page<SysUserVo> page = pageQuery.build();
        QueryWrapper<Object> query = Wrappers.query();
        query.eq(true, "aau.app_id", appId)
            .like(StringUtils.isNotEmpty(sysUserBo.getUserName()), "su.user_name", sysUserBo.getUserName())
            .like(StringUtils.isNotEmpty(sysUserBo.getNickName()), "su.nick_name", sysUserBo.getNickName())
            .orderByAsc("aau.app_id");
        page = baseMapper.getUsersUnderTheApplicationV2(query, page);
        for (SysUserVo vo : page.getRecords()) {
            // 查询用户在线状态
            String userToken = StpUtil.getTokenValueByLoginId(vo.getUserType() + ":" + vo.getUserId());
            // 通过token是否过期,判断用户是否在线
            long time = StpUtil.stpLogic.getTokenActiveTimeoutByToken(userToken);
            if (time > 1) {
                // 用户在线
                vo.setOnline(true);
            } else {
                // 用户不在线
                vo.setOnline(false);
            }
        }
        //如果(AI)部门是:1753687739292135425 排序放置第一位,
        page.getRecords().sort((o1, o2) -> {
            if (o1.getDeptId().equals(1753687739292135425L)) {
                return -1;
            } else if (o2.getDeptId().equals(1753687739292135425L)) {
                return 1;
            } else {
                return 0;
            }
        });
        return page;
    }

    public LambdaQueryWrapper buildQueryWrapper(SysUserBo sysUserBo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper
            .like(StringUtils.isNotBlank(sysUserBo.getUserName()), SysUser::getUserName, sysUserBo.getUserName())
            .like(StringUtils.isNotBlank(sysUserBo.getNickName()), SysUser::getNickName, sysUserBo.getNickName())
            .orderByAsc(SysUser::getUserId);

        return wrapper;
    }

    @Override
    public Boolean addUserClass(String appId) {
        AppUser appUser = new AppUser();
        appUser.setUserId(getCurrentUserIdToString());
        appUser.setAppId(appId);
        return appUserMapper.insert(appUser) > 0;
    }


    @Override
    public Boolean removeUserClass(String appId) {
        String userId = getCurrentUserIdToString();
        LambdaQueryWrapper<AppUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(AppUser::getUserId, userId)
            .eq(AppUser::getAppId, appId);
        return appUserMapper.delete(lqw) > 0;
    }


    /**
     * 校验用户是否重复加入应用
     */
    public void validRepeatedAddAppBox(String appId) {
        String userId = getCurrentUserIdToString();
        LambdaQueryWrapper<AppUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(AppUser::getUserId, userId)
            .eq(AppUser::getAppId, appId);
        if (appUserMapper.selectCount(lqw) > 0) {
            throw new UserException("400", "重复加入的应用");
        }
    }

    @Transactional
    public R<Void> addAppBox(AppboxApplicationBo bo) {
        try {
            AppboxApplication entity = MapstructUtils.convert(bo, AppboxApplication.class);

            // 执行插入操作
            entity.setPermission(0);
            int insertCount = this.baseMapper.insert(entity);
            if (insertCount <= 0) {
                throw new IllegalArgumentException("应用添加失败!!!");
            }

            // 创建者同时加入该应用
            List<String> userIds = bo.getUserIds();
            userIds.add(SysUserUtils.getCurrentUserIdToString());
            List<AppUser> appUsers = userIds.stream()
                .map(userId -> {
                    AppUser appUser = new AppUser();
                    appUser.setAppId(entity.getId());
                    appUser.setUserId(userId);
                    appUser.setStatus(1L);
                    appUser.setRole(0);
                    return appUser;
                })
                .collect(Collectors.toList());

            boolean isBatchInsertSuccess = appUserMapper.insertBatch(appUsers);
            if (!isBatchInsertSuccess) {
                throw new IllegalArgumentException("应用添加失败");
            }
            // 默认频道设置
            validEntityAfterSave(entity);
        } catch (IllegalArgumentException e) {
            // 可添加日志记录和事务回滚的逻辑
            throw e;
        }
        return R.ok();
    }

    @Override
    public int updateVisitorByPv(String appId) {
        //[1] 获取redis key 存在不更新，不存在则更新并且设置key，过期时间3分钟
        String currUserId = getCurrentUserIdToString();
        String key = PV_INCREMENT_CONTROL.formatted(appId + ":" + currUserId);
        Boolean hasKey = RedisUtils.hasKey(key);
        if (!hasKey) {
            // [2] 如果kye是空，更新pv
            AppboxApplication application = baseMapper.selectById(appId);
            if (application != null) {
                application.setPv(application.getPv() + 1);
                application.setPopularity(application.getPv() * application.getUv());
                baseMapper.updateById(application);
                // [3] 设置key，过期3分钟
                RedisUtils.setCacheObject(key, application, Duration.ofMinutes(3));
                updateVisitorByUv(appId);
            }
        }
        return 1;
    }


    @Override
    public int updateVisitorByUv(String appId) {

        AppboxApplication application = baseMapper.selectById(appId);
        if (application != null) {
            application.setUv(application.getUv() + 1);
            application.setPopularity(application.getPv() * application.getUv());
            return baseMapper.updateById(application);
        } else {
            return 1;
        }
    }
}
