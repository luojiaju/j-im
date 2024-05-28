package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 群聊对象 im_group_chat
 *
 * @author luojiaju
 * @date 2024-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("im_group_chat")
public class GroupChat extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
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
     * 群头像
     */
    private String avatar;
    /**
     * 最近一条消息
     */
    private String recentMessage;


}
