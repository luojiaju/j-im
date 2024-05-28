import {api as request} from '@/boot/axios';
import {ChatGroupData, ChatGroupForm, GroupUserChatForm} from "@/api/im/chatGroup/type";


/**
 * 获取用户群聊信息
 * @param query
 */
export const getUserList = (query?: ChatGroupForm): any => {
    return request({
        url: '/im/groupChat/userList',
        method: 'get',
        params: query
    });
};

/**
 * 获取用户群聊信息
 * @param query
 */
export const getGroupChatList = (query?: ChatGroupForm): any => {
    return request({
        url: '/im/groupChat/groupChatList',
        method: 'get',
        params: query
    });
};


/**
 * 获取用户群聊信息
 * @param query
 */
export const getUserChatGroup = (query?: ChatGroupData): any => {
    return request({
        url: '/im/groupChat/list',
        method: 'get',
        params: query
    });
};

/**
 * 获取群聊信息
 * @param id
 */
export const getChatGroupInfo = (id: string | number): any => {
    return request({
        url: '/im/groupChat/' + id,
        method: 'get',
    });
};

/**
 * 添加群聊信息
 * @param data
 */
export const addInvite = (data: GroupUserChatForm): any => {
    return request({
        url: '/im/groupChat/invite',
        method: 'post',
        data: data
    });
};

/**
 * 添加群聊信息
 * @param data
 */
export const addChatGroup = (data: ChatGroupData): any => {
    return request({
        url: '/im/groupChat',
        method: 'post',
        data: data
    });
};

/**
 * 置顶群聊
 */
export const editRole = (data: { userId: string | number, groupId: string | number, role: string | number }): any => {
    return request({
        url: `/im/groupChat/role?userId=${data.userId}&groupId=${data.groupId}&role=${data.role}`,
        headers: {
            "Content-Type": 'application/x-www-form-urlencoded'
        },
        method: 'delete',
    });
};

/**
 * 置顶群聊
 */
export const editTop = (data: { groupId: string | number, top: string | number }): any => {
    return request({
        url: `im/groupChat/top?groupId=${data.groupId}&top=${data.top}'`,
        headers: {
            "Content-Type": 'application/x-www-form-urlencoded'
        },
        method: 'delete',
    });
};

/**
 * 退出群聊
 */
export const exitGroup = (groupId: string | number, userId: string | number): any => {
    return request({
        url: `/im/groupChat/exit/${groupId}/${userId}`,
        method: 'delete',
    });
};


export const deleteGroup = (groupId: string | number, userId: string | number): any => {
    return request({
        url: `/im/groupChat/${groupId}/${userId}`,
        method: 'delete',
    });
};
