export interface ChatGroupData {
    id: string; // 请注意，这里使用字符串代替Long，因为TypeScript中没有内置的Long类型。
    groupLeaderId: string; // 同上，使用字符串作为ID类型。
    groupName: string;
    remark: string;
    userIds: string[]; // 用户ID列表，使用字符串类型。
    avatar: string;
    recentMessage: string;
    role:number;
    groupNickname:string;
    unread:number;
    top:number;
}


export interface ChatGroupForm extends PageQuery {
    id: string;
    groupLeaderId: string;
    groupName: string;
    remark: string;
    userIds: string[];
    userName: string;
    roleName: number;
    toped: number;
    avatar: string;
    recentMessage: string;
    role:number;
    groupNickname:string;
    unread:number;
    top:number;
}


export interface GroupUserChatForm {
    /**
     * 用户id
     */
    userId: string;

    /**
     * 群聊id
     */
    groupId: string;

    /**
     * 群昵称
     */
    groupNickname?: string;

    /**
     * 未读消息数量
     */
    unread?: number;

    /**
     * 聊天置顶: 1置顶 2置底
     */
    top?: number;

    /**
     * 群聊角色: 1群主 2管理员 3普通成员
     */
    role?: number;
}
