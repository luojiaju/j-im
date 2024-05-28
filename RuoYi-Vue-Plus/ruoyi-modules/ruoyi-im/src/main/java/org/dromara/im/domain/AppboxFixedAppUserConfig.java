package org.dromara.im.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 应用的固定用户配置对象 appbox_fixed_app_user_config
 *
 * @author luojiaju
 * @date 2024-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("appbox_fixed_app_user_config")
public class AppboxFixedAppUserConfig extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 应用apo
     */
    private String appId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 标识
     */
    private Long identity;

    /**
     * 头像小图标
     */
    private String iconUrl;

    /**
     * 备注
     */
    private String remark;


}
