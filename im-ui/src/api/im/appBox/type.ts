/**
 * TypeScript接口对应于Java的AppboxApplicationVo类
 */
export interface AppboxApplicationVo {

    /**
     * id
     */
    id?: number; // Java中的Long在TypeScript中通常映射为number

    /**
     * 上级id
     */
    parentId?: number; // 同上

    /**
     * 名称
     */
    appName?: string; // Java中的String在TypeScript中映射为string

    /**
     * 图标
     */
    iconUrl?: string; // 同上

    /**
     * 热度
     */
    popularity?: number; // 同上

    /**
     * 页面访问量
     */
    pv?: number; // 同上

    /**
     * 独立访客数
     */
    uv?: number; // 同上

    /**
     * 频道状态:1应用 2频道
     */
    channelStatus?: number; // 同上

    /**
     * 应用简介
     */
    remark?: string; // 同上

    /**
     * 创建时间
     */
    createTime?: Date; // Java中的Date在TypeScript中映射为Date

    /**
     * 封面
     */
    cover?: string;

    coverUrl?: string;
    /**
     * 频道类型
     */
    channelType: number;

    /**
     * 频道内容限制
     */
    assign: number;

    /**
     * 权限
     */
    permission:number;

    exteInfo:{
        /**
         * 角色
         */
        role: number;

        /**
         * 状态
         */
        status: number;

        /**
         * 封禁时间
         */
        bannedTime?: Date;
    }

}


// @ts-ignore
export interface AppboxApplicationFrom extends BaseEntity {

    /**
     * id
     */
    id?: number;

    /**
     * 上级id
     */
    parentId?: number;

    /**
     * 名称
     */
    appName?: string;

    /**
     * 分类id
     */
    classesId?: string,

    /**
     * 图标
     */
    iconUrl?: string;
    /**
     * 热度
     */
    popularity?: number;
    /**
     * 页面访问量
     */
    pv?: number;
    /**
     * 独立访客数
     */
    uv?: number;
    /**
     * 频道状态:1应用 2频道
     */
    channelStatus?: number;
    /**
     * 应用简介
     */
    remark?: string;
    /**
     * 创建时间
     */
    createTime?: Date;

    /**
     * 封面
     */
    cover: string;

    /**
     * 应用的用户配置
     */
    userIds: [],
    /**
     * 频道类型
     */
    channelType: number;

    /**
     * 频道内容限制
     */
    assign: number;

    /**
     * 权限
     */
    permission:number;

    exteInfo:{
        /**
         * 角色
         */
        role: number;

        /**
         * 状态
         */
        status: number;

        /**
         * 封禁时间
         */
        bannedTime?: Date;
    }
}


export interface AppboxapplicationQuery {
    /**
     * 上级id
     */
    parentId?: string | number;

    /**
     * 配置id
     */
    configId?: string | number;

    /**
     * 分类id
     */
    classesId?: string | number;

    /**
     * 名称
     */
    appName?: string;

    /**
     * 图标
     */
    iconUrl?: string;

    /**
     * 热度
     */
    popularity?: number;

    /**
     * 页面访问量
     */
    pv?: number;

    /**
     *  独立访客数
     */
    uv?: number;

    /**
     * 频道状态:1应用 2频道
     */
    channelStatus?: number;

    /**
     * 创建时间
     */
    createTime?: string;
    /**
     * 频道类型
     */
    channelType: number;

    /**
     * 频道内容限制
     */
    assign: number;


    /**
     * 权限
     */
    permission:number;

    exteInfo:{
        /**
         * 角色
         */
        role: number;

        /**
         * 状态
         */
        status: number;

        /**
         * 封禁时间
         */
        bannedTime?: Date;
    }
}


export interface AppboxapplicationVO {
    /**
     * id
     */
    id: string | number;

    /**
     * 上级id
     */
    parentId: string | number;

    /**
     * 配置id
     */
    configId: string | number;

    /**
     * 名称
     */
    appName: string;


    /**
     * 图标
     */
    iconUrl: string;
    /**
     * 封面
     */
    cover: string;

    /**
     * 热度
     */
    popularity: number;

    /**
     * 页面访问量
     */
    pv: number;

    /**
     *  独立访客数
     */
    uv: number;

    /**
     * 频道状态:1应用 2频道
     */
    channelStatus: number;

    /**
     * 应用简介
     */
    remark: string;

    /**
     * 子对象
     */
    children: AppboxapplicationVO[];

    /**
     * 频道类型
     */
    channelType: number;

    /**
     * 频道内容限制
     */
    assign: number;

    /**
     * 权限
     */
    permission:number;

    exteInfo:{
        /**
         * 角色
         */
        role: number;

        /**
         * 状态
         */
        status: number;

        /**
         * 封禁时间
         */
        bannedTime?: Date;
    }
}


// 频道分配
export enum AssignType {
    UpdateNotice = 0, // 更新通知
    Video = 1,        // 视频
    OfficialNotice = 2, // 官方通知
    FeedbackCollection = 3, // 反馈收集
    Voice = 4,        // 语音
    GroupChat = 5,    // 群聊
    Role = 6,         // 角色
    Other = 99        // 其他
}
