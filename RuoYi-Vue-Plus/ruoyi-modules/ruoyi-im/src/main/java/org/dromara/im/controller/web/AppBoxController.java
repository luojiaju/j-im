package org.dromara.im.controller.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.AppboxApplicationBo;
import org.dromara.im.service.impl.AppboxApplicationServiceImpl;
import org.dromara.system.domain.bo.SysUserBo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 应用
 * <p>
 * description: 用户 添加或者修改应用
 * </p>
 *
 * @author luojiaju
 * @date 31/01/2024
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/im/appBox")
@RequiredArgsConstructor
public class AppBoxController extends BaseController {


    private final AppboxApplicationServiceImpl applicationService;

    /**
     * 为应用更新独立访客量
     */
    @PostMapping("/visitorUv/{appId}")
    public R<Void> updateVisitorByUv(@PathVariable String appId) {
        return toAjax(applicationService.updateVisitorByUv(appId));
    }

    /**
     * 为应用页面浏览量
     */
    @PostMapping("/visitorPv/{appId}")
    public R<Void> updateVisitorByPv(@PathVariable String appId) {
        return toAjax(applicationService.updateVisitorByPv(appId));
    }



    /**
     * 用户添加应用
     */
    @SaCheckLogin
    @PostMapping("/{appId}")
    public R<Void> addUserClass(@PathVariable String appId) {
        return toAjax(applicationService.addUserClass(appId));
    }


    /**
     * 用户退出应用
     */
    @SaCheckLogin
    @DeleteMapping("/{appId}")
    public R<Void> removeUserClass(@PathVariable String appId) {
        return toAjax(applicationService.removeUserClass(appId));
    }


    /**
     * 通过应用id查询用户信息
     *
     * @param appId     应用程序的id
     * @param sysUserBo 用户信息
     * @param pageQuery 查询参数
     */
    @GetMapping("/appBoxUsers/{appId}")
    public TableDataInfo<?> obtainUsersUnderTheApplication(@PathVariable Serializable appId,
                                                           SysUserBo sysUserBo, PageQuery pageQuery) {
        return TableDataInfo.build(applicationService.getUsersUnderTheApplication(appId, sysUserBo, pageQuery));
    }

    /**
     * 新增应用
     *
     * @param bo 应用表单
     */
    @PostMapping("/appBoxUsers")
    public R<Void> addAppBox(@RequestBody AppboxApplicationBo bo) {
        return applicationService.addAppBox(bo);
    }
}
