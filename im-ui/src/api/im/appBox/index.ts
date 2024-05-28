import {api as request} from '@/boot/axios';
import {AxiosPromise} from 'axios';
import {
    AppboxApplicationFrom,
    AppboxapplicationQuery,
    AppboxapplicationVO,
    AppboxApplicationVo
} from "@/api/im/appBox/type";
import {R} from "@/api/result.data";
import {SysUserQuery} from "@/api/login/login.data";
import {useAppBoxs} from "@/stores/modules/appBox";


/**
 * 获取当前应用下的用户列表
 * @param query
 */

export const getAppBoxUserList = (query?: SysUserQuery): AxiosPromise<any[]> => {
    let appId = useAppBoxs().active
    return request({
        url: '/im/appBox/appBoxUsers/' + appId,
        method: 'get',
        params: query
    });
};

/**
 * 查询创建应用or频道列表
 * @param query
 * @returns {*}
 */

export const listAppboxapplication = (query?: AppboxapplicationQuery): AxiosPromise<AppboxapplicationVO[]> => {
    return request({
        url: '/im/appboxapplication/list',
        method: 'get',
        params: query
    });
};


/**
 * 通过id查询应用信息
 * @param id
 */
export const getAppTreeById = (id: string | number | null):
    AxiosPromise<any> => {
    return request({
        url: 'im/appboxapplication/tree/' + id,
        method: 'get',
    });
};


/**
 * 通过用户id查询应用
 */
export const queryAppByUser = ():
    AxiosPromise<R<Array<AppboxApplicationVo>>> => {
    return request({
        url: 'im/appboxapplication/user',
        method: 'get',
    });
};

// 用户添加应用
export const addAppBox = (data: any): any => {
    return request({
        url: '/im/appBox/' + data,
        method: 'post',
        data: data
    });
};


export const edit = (data: AppboxApplicationFrom): AxiosPromise<R<any>> => {
    return request({
        url: 'im/appboxapplication',
        method: 'put',
        data: data
    });
};

export const removeAppBox = (data: any): any => {
    return request({
        url: 'im/appBox/' + data,
        method: 'delete',
    });
};


// 添加应用
export const appBoxUsers = (data: any): any => {
    return request({
        url: '/im/appBox/appBoxUsers',
        method: 'post',
        data
    });
};

// 访客统计
export const visitorUv = (data: any): any => {
    return request({
        url: `/im/appBox/visitorUv/${data}`,
        method: 'post',
        data
    });
};

// 访客统计
export const visitorPv = (data: any): any => {
    return request({
        url: `/im/appBox/visitorPv/${data}`,
        method: 'post',
        data
    });
};

