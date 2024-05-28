package org.dromara.im.domain.bo;

import org.dromara.im.domain.MsgRecord;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 聊天消息记录业务对象 im_msg_record
 *
 * @author luojiaju
 * @date 2024-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MsgRecord.class, reverseConvertGenerate = false)
public class MsgRecordBo extends BaseEntity {

    /**
     * id消息
     */
    @NotNull(message = "id消息不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 发送者
     */
    @NotNull(message = "发送者不能为空", groups = { AddGroup.class, EditGroup.class })
    private String senderId;

    /**
     * 接收者
     */
    private String toId;

    /**
     * 群聊
     */
    private String toGroupId;

    /**
     * 频道
     */
    private String toAppId;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空", groups = { AddGroup.class, EditGroup.class })
    @NotNull(message = "消息内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;


    /**
     * 内容类型
     */
    private Long msgType;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long toType;

    /**
     * 消息状态
     */
    private Long status;

    /**
     * 读取状态
     */
    private Long unread;

    /**
     * 引用|回复消息id
     */
    private Long refMsgId;

    /**
     * 备注
     */
    private String remark;

    /**
     * at人员数组
     * 0 是全体成员
     * 1 是管理员
     */
    private List<Long> atSelected;

}
