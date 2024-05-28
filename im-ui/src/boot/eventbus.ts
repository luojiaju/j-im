import {EventBus} from 'quasar'
import {boot} from 'quasar/wrappers'

export const bus = new EventBus()

export default boot(({app}) => {


    // 用于Options API
    app.config.globalProperties.$bus = bus

    // 用于Composition API
    app.provide('bus', bus)

})


// 获取好友聊天记录
export const UPDATE_FRIEND_MSG = "UPDATE_FRIEND_MSG"

// 清理消息点
export const CLEAR_MSG_DOT = "CLEAR_MSG_DOT"

// 更新好友消息点
export const UPDATE_FRIEND_MSG_DOT = "UPDATE_FRIEND_MSG_DOT"
// 更新好友列表
export const UPDATE_FRIEND_LIST = "UPDATE_FRIEND_LIST"

// 清空聊天记录
export const DELETE_CLEAR_RECORD = "DELETE_CLEAR_RECORD"

// 顶部消息
export const TOP_HEAD_DATA = "TOP_HEAD_DATA"

// 精选通知
export const CHOICE_MSG = "CHOICE_MSG"
// 普通通知
export const OTHER_MSG = "OTHER_MSG"
// 提及通知
export const MENTION_MSG = "MENTION_MSG"

export const ACTIVE_CURRENT_USER = "ACTIVE_CURRENT_USER"
// 活动用户列表
export const ACTIVE_USER_LIST = "ACTIVE_USER_LIST"
// 显示
export const ACTIVE_USER_SHOW = "ACTIVE_USER_SHOW"
// 隐藏
export const ACTIVE_USER_CLOSE = "ACTIVE_USER_CLOSE"
// 切换
export const ACTIVE_USER_TOGGLE = "ACTIVE_USER_TOGGLE"
// 清空消息
export const DELETE_CLEAR_GLOBAL = "DELETE_CLEAR_GLOBAL"

// 发送应用卡片
export const SEND_APP_CARD = "SEND_APP_CARD"


// 导出消息
export const EXPORT_MSG_LIST = "EXPORT_MSG_LIST";
