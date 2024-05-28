package org.dromara.im.domain.bo;

import org.dromara.im.domain.AppUser;
import org.dromara.im.domain.AppboxApplication;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.im.domain.vo.AppUserVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建应用or频道业务对象 appbox_application
 *
 * @author luojiaju
 * @date 2024-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AppboxApplication.class, reverseConvertGenerate = false)
public class AppboxApplicationBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 上级id
     */
    @NotNull(message = "上级id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String parentId;

    private String classesId;

    /**
     * 配置id
     */
    private String configId;

    /**
     * 分类id
     */
    private String ClassesId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String appName;

    /**
     * 图标
     */
    private String iconUrl;

    /**
     * 热度
     */
    private Long popularity;

    /**
     * 页面访问量
     */
    private Long pv;

    /**
     * 独立访客数
     */
    private Long uv;


    /**
     * 封面
     */
    private String cover;

    /**
     * 频道状态:1应用 2频道
     */
    private Long channelStatus;

    /**
     * 应用简介
     */
    private String remark;

    /**
     * 用户id
     */
    private List<String> userIds = new ArrayList<>();


    /**
     * 频道类型: 1官网 2公众
     */
    private Integer channelType;
    /**
     * 频道指定内容: 0更新通知 1视频 2官方通知 3反馈收集 99其他
     */
    private Integer  assign;

    /**
     * 应用角色:  0创建者 1 管理员 2Ai 用户 3普通成员
     */
    private Integer permission;

    private AppUserVo exteInfo;

}
