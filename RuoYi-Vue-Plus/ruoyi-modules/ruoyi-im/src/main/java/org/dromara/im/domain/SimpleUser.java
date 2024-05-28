package org.dromara.im.domain;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.system.domain.vo.SysUserVo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@Data
@NoArgsConstructor
@AutoMapper(target = SysUserVo.class)
public class SimpleUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名
     */
    private String deptName;


    /**
     * 用户类型
     */
    private String userType;


    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 最后登录时间
     */
    private Date loginDate;


    /**
     * 用户账号
     */
    private String userName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 备注
     */
    private String notes;

    /**
     * 附加消息
     */
    private String attachMsg;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 获取登录id
     */
    public String getLoginId() {
        if (userType == null) {
            throw new IllegalArgumentException("用户类型不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return userType + ":" + userId;
    }

    /**
     * 是否在线
     */
    private Boolean online;

    /**
     * 未读消息
     */
    private Integer unread;

    /**
     * 附加消息
     */
    private String extraMsg;

}
