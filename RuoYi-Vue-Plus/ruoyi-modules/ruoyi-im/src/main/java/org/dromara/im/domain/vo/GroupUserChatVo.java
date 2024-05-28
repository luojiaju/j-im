package org.dromara.im.domain.vo;

import org.dromara.im.domain.GroupUserChat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * groupUserChat视图对象 im_group_user_chat
 *
 * @author luojiaju
 * @date 2024-02-10
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GroupUserChat.class)
public class GroupUserChatVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String userId;

    /**
     * 群聊id
     */
    @ExcelProperty(value = "群聊id")
    private String groupId;

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


}
