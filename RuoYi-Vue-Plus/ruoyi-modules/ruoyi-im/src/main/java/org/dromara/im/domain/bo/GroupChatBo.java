package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.im.domain.GroupChat;

import java.util.List;

/**
 * 群聊业务对象 im_group_chat
 *
 * @author luojiaju
 * @date 2024-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GroupChat.class, reverseConvertGenerate = false)
public class GroupChatBo extends BaseEntity {

    /**
     * id
     */
    private String id;

    /**
     * 群主id
     */
    private String groupLeaderId;

    /**
     * 群聊名称
     */
    private String groupName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 一个或者多个id
     */
    @NotEmpty(message = "用户不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, message = "用户过少", groups = {AddGroup.class, EditGroup.class})
    private List<String> userIds;


    /**
     * 群角色: 1管理员 2普通成员
     */
    private Integer role;

    /**
     * 群昵称
     */
    private String groupNickname;

    /**
     * 未读消息数量
     */
    private Integer unread;

    /**
     * 聊天置顶:1置顶 2置底
     */
    private Integer top;

    /**
     * 群头像
     */
    private String avatar;
    /**
     * 最近一条消息
     */
    private String recentMessage;




}
