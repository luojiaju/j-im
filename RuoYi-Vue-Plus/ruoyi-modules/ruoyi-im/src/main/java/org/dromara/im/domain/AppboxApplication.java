package org.dromara.im.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 创建应用or频道对象 appbox_application
 *
 * @author luojiaju
 * @date 2024-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("appbox_application")
public class AppboxApplication extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 上级id
     */
    private String parentId;

    /**
     * 配置id
     */
    private String configId;

    /**
     * 名称
     */
    private String appName;

    /**
     * 分类id
     */
    private  String classesId;

    /**
     * 图标
     */
    private String iconUrl;

    /**
     * 热度
     */
    private Long popularity;

    /**
     * 封面
     */
    private String cover;

    /**
     * 页面访问量
     */
    private Long pv;

    /**
     * 独立访客数
     */
    private Long uv;

    /**
     * 频道状态:1应用 2频道
     */
    private Long channelStatus;

    /**
     * 应用简介
     */
    private String remark;

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


}
