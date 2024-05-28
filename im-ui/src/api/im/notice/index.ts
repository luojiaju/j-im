import {api as request} from '@/boot/axios';
import { AxiosPromise } from 'axios';
import { NoticeVO, NoticeForm, NoticeQuery } from '@/api/im/notice/types';

/**
 * 查询用户通知列表
 * @param query
 * @returns {*}
 */

export const listNotice = (query?: NoticeQuery): AxiosPromise<NoticeVO[]> => {
    return request({
        url: '/im/notice/list',
        method: 'get',
        params: query
    });
};

/**
 * 查询用户通知详细
 * @param id
 */
export const getNotice = (id: string | number): AxiosPromise<NoticeVO> => {
    return request({
        url: '/im/notice/' + id,
        method: 'get'
    });
};

/**
 * 新增用户通知
 * @param data
 */
export const addNotice = (data: NoticeForm) => {
    return request({
        url: '/im/notice',
        method: 'post',
        data: data
    });
};

/**
 * 修改用户通知
 * @param data
 */
export const updateNotice = (data: NoticeForm) => {
    return request({
        url: '/im/notice',
        method: 'put',
        data: data
    });
};
/**
 * 修改用户通知
 * @param data
 */
export const updateNoticeRead = (data: any) => {
    return request({
        url: `/im/notice/${data.noticeId}/${data.type}/read`,
        method: 'put',
    });
};
/**
 * 修改用户通知
 * @param data
 */
export const updateNoticeUnRead = (data: any) => {
    return request({
        url: `/im/notice/${data}/unread`,
        method: 'put',
    });
};


/**
 * 删除用户通知
 * @param id
 */
export const delNotice = (id: string | number | Array<string | number>) => {
    return request({
        url: '/im/notice/' + id,
        method: 'delete'
    });
};

export const notifyAll = (data: NoticeForm) => {
    return request({
        url: '/im/notice/all',
        method: 'post',
        data: data
    });
};

export const notifyOne = (data: NoticeForm) => {
    return request({
        url: '/im/notice/One',
        method: 'post',
        data: data
    });
};
