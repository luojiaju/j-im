package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.domain.GroupChat;

import java.io.Serial;
import java.io.Serializable;


/**
 * 群聊视图对象 im_group_chat
 *
 * @author luojiaju
 * @date 2024-01-11
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GroupChat.class)
public class GroupChatVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private String id;

    /**
     * 群主id
     */
    @ExcelProperty(value = "群主id")
    private String groupLeaderId;

    /**
     * 群聊名称
     */
    @ExcelProperty(value = "群聊名称")
    private String groupName;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

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
