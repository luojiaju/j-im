package org.dromara.im.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImNoticeBo;
import org.dromara.im.domain.vo.ImNoticeVo;
import org.dromara.im.service.IImNoticeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户通知
 * 前端访问路由地址为:/im/notice
 *
 * @author luojiaju
 * @date 2024-01-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/notice")
public class NoticeController extends BaseController {

    private final IImNoticeService imNoticeService;

    /**
     * 查询用户通知列表
     */
    @SaCheckLogin
    @GetMapping("/list")
    public TableDataInfo<ImNoticeVo> list(ImNoticeBo bo, PageQuery pageQuery) {
        return imNoticeService.queryByUserPageList(bo, pageQuery);
    }

    /**
     * 查询用户通知列表
     */
    @SaCheckLogin
    @GetMapping("/list/v2")
    public TableDataInfo<ImNoticeVo> listv2(ImNoticeBo bo, PageQuery pageQuery) {
        return imNoticeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户通知列表
     */
    @SaCheckLogin
    @Log(title = "用户通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImNoticeBo bo, HttpServletResponse response) {
        List<ImNoticeVo> list = imNoticeService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户通知", ImNoticeVo.class, response);
    }

    /**
     * 获取用户通知详细信息
     *
     * @param id 主键
     */
    @SaCheckLogin
    @GetMapping("/{id}")
    public R<ImNoticeVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable String id) {
        return R.ok(imNoticeService.queryById(id));
    }

    /**
     * 新增用户通知
     */
    @SaCheckLogin
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImNoticeBo bo) {
        return toAjax(imNoticeService.insertByBo(bo));
    }

    /**
     * 修改用户通知
     */
    @SaCheckLogin
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImNoticeBo bo) {
        return toAjax(imNoticeService.updateByBo(bo));
    }


    /**
     * 设置用户通知已读状态
     */
    @SaCheckLogin
    @RepeatSubmit()
    @PutMapping("/{noticeId}/{type}/read")
    public R<Void> setNoticeRead(@PathVariable String noticeId,
                                 @PathVariable Long type) {
        return toAjax(imNoticeService.setNoticeStatus(noticeId, type, 1));
    }

    /**
     * 设置用户通知未读状态
     */
    @SaCheckLogin
    @RepeatSubmit()
    @PutMapping("/{noticeId}/{type}/unread")
    public R<Void> setNoticeStatusUnread(@PathVariable String noticeId,
                                         @PathVariable Long type) {
        return toAjax(imNoticeService.setNoticeStatus(noticeId, type, 0));
    }

    /**
     * 删除用户通知
     *
     * @param ids 主键串
     */
    @SaCheckLogin
    @Log(title = "用户通知", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(imNoticeService.deleteWithValidByIds(List.of(ids), true));
    }
}
