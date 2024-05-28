package org.dromara.im.domain.vo;

import jakarta.validation.constraints.NotNull;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.im.domain.AppUser;
import org.dromara.im.domain.AppboxApplication;
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
 * 创建应用or频道视图对象 appbox_application
 *
 * @author luojiaju
 * @date 2024-01-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AppboxApplication.class)
public class AppboxApplicationVo  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 上级id
     */
    private String parentId;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String appName;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String iconUrl;


    /**
     * 分类id
     */
    private  String classesId;

    /**
     * 热度
     */
    @ExcelProperty(value = "热度")
    private Long popularity;

    /**
     * 页面访问量
     */
    @ExcelProperty(value = "页面访问量")
    private Long pv;

    /**
     * 独立访客数
     */
    @ExcelProperty(value = " 独立访客数")
    private Long uv;


    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    private String cover;

    private String coverUrl;

    /**
     * 频道状态:1应用 2频道
     */
    @ExcelProperty(value = "频道状态:1应用 2频道", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "im_appbox_channel_status")
    private Long channelStatus;

    /**
     * 应用简介
     */
    @ExcelProperty(value = "应用简介")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建者
     */
    private String createBy;

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
