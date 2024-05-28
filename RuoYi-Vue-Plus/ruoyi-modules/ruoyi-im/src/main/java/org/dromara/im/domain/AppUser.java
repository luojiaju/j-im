package org.dromara.im.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 用户应用关联对象 appbos_app_user
 *
 * @author Lion Li
 * @date 2024-02-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("appbos_app_user")
public class AppUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所在应用的状态:1正常 0禁用 2禁言
     */
    private Long status;

    /**
     * 禁言时间
     */
    private Date bannedTime;

    /**
     * 应用角色: 1 管理员 2Ai 用户 3普通成员
     */
    private Integer role;


}
