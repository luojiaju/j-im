package org.dromara.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.im.domain.AppboxApplication;
import org.dromara.im.domain.SimpleUser;
import org.dromara.im.domain.vo.AppboxApplicationVo;
import org.dromara.system.domain.vo.SysUserVo;

import java.io.Serializable;
import java.util.List;

/**
 * 创建应用or频道Mapper接口
 *
 * @author luojiaju
 * @date 2024-01-04
 */
public interface AppboxApplicationMapper extends BaseMapperPlus<AppboxApplication, AppboxApplicationVo> {

    /**
     * 根据appId查询
     * @param appId 应用id
     * @return 列表
     */
    List<String> selectVoByAppId(@Param("appId") String appId);


    /**
     * 根据用户id查询
     * @param id 用户id
     * @return 列表
     */
    List<AppboxApplicationVo> queryAppByUserId(@Param("id") Serializable id);

    /**
     * 获取应用树信息
     * @param id 应用id
     * @return 列表
     */
    List<AppboxApplicationVo> getTreeInfo(@Param("id") String id);

    /**
     * 获取应用下用户
     * @param query 查询
     * @param page 分页
     * @return 列表
     */
    Page<SysUserVo> getUsersUnderTheApplicationV2(@Param(Constants.WRAPPER) Wrapper<Object> query, Page<SysUserVo> page);

    /**
     * 根据应用id查询用户
     * @param application id
     * @param query 查询
     * @return 列表
     */
    List<SimpleUser> selectUserListByAppBoxChildId(@Param(value = "appbox") AppboxApplication application,@Param(value = "query") PageQuery query);


    /**
     * 根据应用id查询用户id
     */
    List<Long> selectUserIdListByAppId(String appId);

    /**
     * 根据应用id查询管理员用户id
     */
    List<Long> selectAdminUserIdListByAppId(String appId);
}
