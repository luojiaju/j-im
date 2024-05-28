import {SysUserVo} from "@/api/login/login.data";
import {MsgActionVO} from "@/api/im/msgAction/types";

export interface MsgRecordVO {
    /**
     * id消息
     */
    id: string | number | BigInt;

    /**
     * 发送者
     */
    senderId: string | number | BigInt;

    /**
     * 接收者
     */
    toId: string | number;

    /**
     * 群聊
     */
    toGroupId: string | number;

    /**
     * 频道
     */
    toAppId: string | number;

    /**
     * 消息内容
     */
    content: string | Content;

    /**
     * 内容类型
     */
    msgType: number;

    /**
     * 消息类型
     */
    toType: number;

    /**
     * 消息状态
     */
    status: number;

    /**
     * 读取状态
     */
    unread: number;

    /**
     * 引用|回复消息id
     */
    refMsgId: string | number;

    /**
     * 备注
     */
    remark: string;
    createTime: string,
    rtcType: number | string;
    author: SysUserVo;
    action: MsgActionVO;

}

export interface MsgRecordForm extends BaseEntity {
    /**
     * id消息
     */
    id?: string | number;

    /**
     * 发送者
     */
    senderId?: string | number;

    /**
     * 接收者
     */
    toId?: string | number;

    /**
     * 群聊
     */
    toGroupId?: string | number;

    /**
     * 频道
     */
    toAppId?: string | number;

    /**
     * 消息内容
     */
    content?: string;

    /**
     * 内容类型
     */
    msgType?: number;

    /**
     * 消息类型
     */
    toType?: number;

    /**
     * 消息状态
     */
    status?: number;

    /**
     * 读取状态
     */
    unread?: number;

    /**
     * 引用|回复消息id
     */
    refMsgId?: string | number;

    /**
     * 备注
     */
    remark?: string;

}

export interface MsgRecordQuery extends PageQuery {

    /**
     * 发送者
     */
    senderId?: string | number;

    /**
     * 接收者
     */
    toId?: string | number;

    /**
     * 群聊
     */
    toGroupId?: string | number;

    /**
     * 频道
     */
    toAppId?: string | number;

    /**
     * 消息内容
     */
    content?: Content;

    /**
     * 内容类型
     */
    msgType?: number;

    /**
     * 消息类型
     */
    toType?: number;

    /**
     * 消息状态
     */
    status?: number;

    /**
     * 读取状态
     */
    unread?: number;

    /**
     * 引用|回复消息id
     */
    refMsgId?: string | number;
    /**
     * 被@的人
     */
    atSelected: [] | string[];

    /**
     * 日期范围参数
     */
    params?: any;
}


export interface Content {
    files: { name: string, url: string }[];
    text: string;
    audio: string;
    video: string[]
}


export enum MsgRecordMsgType {
    TEXT = 1,
    IMAGE = 2,
    VIDEO = 3,
    AUDIO = 4,
    COMPOSITE = 5,
    START_CALL = 6,
    INVITE = 7
}
