package org.dromara.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.im.domain.ImNotice;
import org.dromara.im.domain.bo.ImNoticeBo;
import org.dromara.im.domain.vo.ImNoticeVo;
import org.dromara.im.domain.vo.MsgRecordVo;
import org.dromara.im.enums.MsgRecordToType;
import org.dromara.im.mapper.ImNoticeMapper;
import org.dromara.im.service.IImNoticeService;
import org.dromara.im.utils.ImWebSocketMsgUtils;
import org.dromara.im.utils.SysUserUtils;
import org.dromara.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.dromara.im.constant.ImStringConstants.PUBLIC_NOTIFY;

/**
 * 用户通知Service业务层处理
 *
 * @author luojiaju
 * @date 2024-01-05
 */
@RequiredArgsConstructor
@Service
public class ImNoticeServiceImpl implements IImNoticeService {

    private final ImNoticeMapper baseMapper;


    private final SysUserMapper sysUserMapper;

    /**
     * 查询用户通知
     */
    @Override
    public ImNoticeVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户通知列表
     */
    @Override
    public TableDataInfo<ImNoticeVo> queryPageList(ImNoticeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImNotice> lqw = buildQueryWrapper(bo);
        Page<ImNoticeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户的通知列表
     */
    @Override
    public TableDataInfo<ImNoticeVo> queryByUserPageList(ImNoticeBo bo, PageQuery pageQuery) {
        // [1] 得到当前用户id
        String userId = SysUserUtils.getCurrentUserIdToString();
        // [2] 获取公告hashmap

        Collection<String> keys = RedisUtils.keys(PUBLIC_NOTIFY.formatted(bo.getNoticeType(),"*"));
        List<String> noticeIdList = new ArrayList<>();
        Map<String, Integer> map;
        for (String key : keys) {
            map = RedisUtils.getCacheMap(key);
            // [3] 得到当前用户未读的公告id,已读跳过
            Integer status = map.get(userId);
            if (status != null && status == 1) {
                continue;
            }
            String[] split = key.split(":");
            String noticeId = split[split.length - 1];
            noticeIdList.add(noticeId);
        }
        if (noticeIdList.isEmpty()) {
            noticeIdList.add("0");
        }
        // [4] 获取当前用户未读公告信息
        List<ImNoticeVo> result = this.baseMapper.selectVoBatchIds(noticeIdList);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户通知列表
     */
    @Override
    public List<ImNoticeVo> queryList(ImNoticeBo bo) {
        LambdaQueryWrapper<ImNotice> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ImNotice> buildQueryWrapper(ImNoticeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImNotice> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getNoticeType() != null, ImNotice::getNoticeType, bo.getNoticeType());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), ImNotice::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), ImNotice::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getFromUrl()), ImNotice::getFromUrl, bo.getFromUrl());
        return lqw;
    }


    /**
     * 新增用户通知
     */
    @Override
    public Boolean insertByBo(ImNoticeBo bo) {
        ImNotice add = MapstructUtils.convert(bo, ImNotice.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // [1] 为这个公告添加哈希表缓存, key用户id,value 1已读 0未读
            String key = PUBLIC_NOTIFY.formatted(bo.getNoticeType(),add.getId());
            RedisUtils.setCacheMap(key, Map.of("0", 0));


            // [2] websocket 推送公告,通过公告类型发布
            noticeSendByType(add);
        }
        return flag;
    }


    // 通过公告类型发送
    private static void noticeSendByType(ImNotice add) {
        MsgRecordVo recordVo = new MsgRecordVo();
        Long noticeType = add.getNoticeType();

        ImNoticeVo noticeVo = MapstructUtils.convert(add, ImNoticeVo.class);
        recordVo.setContent(JsonUtils.toJsonString(noticeVo));
        if (noticeType == 1) {
            // 精选消息
            recordVo.setToType(MsgRecordToType.CHOICE_NOTICE.getValue());
        }
        if (noticeType == 2) {
            // 其他消息
            recordVo.setToType(MsgRecordToType.OTHER_NOTICE.getValue());
        }
        if (noticeType == 3) {
            // 提及消息
            recordVo.setToType(MsgRecordToType.MENTION_NOTICE.getValue());
        }
        // 发送给在线用户
        ImWebSocketMsgUtils.sendAllOnlineUser(recordVo);
    }

    /**
     * 修改用户通知
     */
    @Override
    public Boolean updateByBo(ImNoticeBo bo) {
        ImNotice update = MapstructUtils.convert(bo, ImNotice.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 设置公告状态
     */
    @Override
    public Boolean setNoticeStatus(String noticeId,Long type, Integer status) {
        String userId = SysUserUtils.getCurrentUserIdToString();
        String key = PUBLIC_NOTIFY.formatted(type,noticeId);
        RedisUtils.setCacheMapValue(key, userId, status);
        return true;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ImNotice entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户通知
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
