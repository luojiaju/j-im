package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.im.domain.Friend;

/**
 * 好友业务对象 im_friend
 *
 * @author luojiaju
 * @date 2024-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Friend.class, reverseConvertGenerate = false)
public class FriendBo extends BaseEntity {

    /**
     * 主键id
     */
    private String id;

    /**
     * 用户
     */
    @NotNull(message = "用户不能为空", groups = {AddGroup.class, EditGroup.class})
    private String userId;


    /**
     * 好友
     */
    @NotNull(message = "好友不能为空", groups = {AddGroup.class, EditGroup.class})
    private String friendId;

    /**
     * 备注
     */
    private String notes;

    /**
     * 附加消息
     */
    private String attachMsg;

    /**
     * 好友关系
     */
    @NotNull(message = "关系不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer relationStatus;

    /**
     *
     */
    private String remark;

    private String userName;

    private String nickName;


}
