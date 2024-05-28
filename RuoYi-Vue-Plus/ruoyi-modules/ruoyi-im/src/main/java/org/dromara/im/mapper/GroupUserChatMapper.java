package org.dromara.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.im.domain.GroupChat;
import org.dromara.im.domain.GroupUserChat;
import org.dromara.im.domain.SimpleUser;
import org.dromara.im.domain.vo.GroupChatVo;
import org.dromara.im.domain.vo.GroupUserChatVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.springframework.messaging.simp.user.SimpUser;

/**
 * groupUserChatMapper接口
 *
 * @author luojiaju
 * @date 2024-02-10
 */
public interface GroupUserChatMapper extends BaseMapperPlus<GroupUserChat, GroupUserChatVo> {


    /**
     * 获取当前用户的群聊列表
     *
     */
    @Select("""
                    select  ig.id,ig.group_leader_id,ig.group_name,ig.create_by,ig.create_time,ig.remark,ig.create_dept,ig.update_by,ig.update_time,
                            ig.toped,so.url as avatar,ig.recent_message,gu.remark,gu.group_nickname,gu.unread,gu.top,gu.role
                    from im_group_user_chat gu
                             inner join im_group_chat ig on ig.id = gu.group_id
                 inner join sys_oss so on so.oss_id = ig.avatar
                    ${ew.getCustomSqlSegment}
                    """)
    Page<GroupChatVo> selectUserGroup(@Param(Constants.WRAPPER) Wrapper<GroupChat> wrapper,
                                                          Page<GroupChat> page);



    /**
     * 查询当前群聊的用户列表
     */
    @Select("""
             select su.*
             from im_group_user_chat gu
                      join sys_user su on gu.user_id = su.user_id
            ${ew.getCustomSqlSegment}
             """)
    Page<SysUserVo> selectUserList(@Param(Constants.WRAPPER) Wrapper<GroupChat> wrapper,
                                    Page<GroupChat> page);


}
