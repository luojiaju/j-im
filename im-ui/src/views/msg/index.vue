<template>
    <div class=" row justify-between ">
        <div style="width: 100%; " id="parentBox">
            <q-scroll-area :style="{ height: `calc(${$q.screen.height}px - 100px` }" ref="scrollAreaRef"
                           :bar-style="barStyle" :thumb-style="thumbStyle"
                           class="global-dark custom-scroll-area">
                <Communicate class=""></Communicate>
                <div class="text-white p-4 " v-if="recordList.length > 0" v-show="!loading">
                    <!--                     消息记录列表-->
                    <div class="space-y-6 q-pa-md " v-for="(record,index) in recordList" :key="record.id">
                        <div class=" border-[#bd1e59] pl-4 hover:bg-black q-pa-xs rounded "
                             @click.right="(e)=>rightClick(e,record)" :id="record.id"
                             :class="replyActive==record.id?'border-4':'border-l-4'">
                            <PopupMenu :data="record" :context-menu="true" @reply="reply"></PopupMenu>

                            <div class="flex items-center justify-between ">
                                <div class="flex items-center space-x-2">
                                  <span
                                      class="relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full">
                                      <span
                                          class="relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full ring-2 ring-offset-2 ring-offset-[#6e00b3] ring-white">
                                            <img v-if="record.author.avatar" class="aspect-square h-full w-full"
                                                 :src="record.author.avatar">
                                      </span>
                                    <span class="flex h-full w-full items-center justify-center rounded-full bg-muted">RS
                                   </span>
                                  </span>
                                    <div><p class="font-bold">{{ record.author?.nickName }}</p>
                                        <p class="text-xs text-[#b0b3b8]">{{ formatDate(record?.createTime) }}</p>
                                    </div>
                                </div>
                                <!-- 消息更多下拉-->
                                <q-btn size="md" icon="more" class="p-1.5">
                                    <PopupMenu :data="record" :context-menu="false"></PopupMenu>
                                </q-btn>

                            </div>
                            <!-- 内容处理 -->
                            <div class="pl-9 mt-2 discord-font q-ma-md">
                                <!-- 邀请卡片 -->
                                <div v-if="record.msgType == MsgRecordMsgType.INVITE">
                                    <invite-card :json="record.contents.text"></invite-card>
                                </div>

                                <div v-else>
                                    <!-- 文件 -->
                                    <div v-if=" record.contents?.files">
                                        <div v-for="(item,index) in record.contents?.files" :key="index"
                                             class="q-gutter-md row items-start">
                                            <q-img v-if="isImage(item.url)" spinner-color="white"
                                                   :ratio="4/3" fit="contain"
                                                   class="rounded-borders hover:scale-110 transition-transform
                                                    w-2/12 max-w-6/12 max-h-6/12"
                                                   @click="()=>{
                                                       showImageUrl=item.url;
                                                       showImage=true;
                                                   }"
                                                   :src="item.url">
                                            </q-img>
                                            <q-video v-else
                                                     frameborder="0"
                                                     allowfullscreen
                                                     :src="item.url"></q-video>
                                        </div>

                                    </div>

                                    <!-- 音频 -->
                                    <div v-if="record.contents?.audio" class="q-pa-md">
                                        <audio controls :src="record.contents?.audio"></audio>
                                    </div>

                                    <!--普通文本-->
                                    <div class="q-pa-md">
                                        {{ record.contents?.text }}
                                    </div>
                                </div>

                            </div>
                            <!--              反应  -->
                            <div class="flex items-center space-x-2 mt-2 pl-13" v-if="record.action">
                                <q-bar v-if="record.action?.likeCount">
                                    <q-icon size="xs" color="yellow" name="thumb_down"/>
                                    <span class="pl-2">{{ record.action.likeCount }}</span>
                                </q-bar>
                                <q-bar v-if="record.action?.fullCount">
                                    <q-icon size="xs" color="yellow" name="sentiment_satisfied"/>
                                    <span class="pl-2">{{ record.action.fullCount }}</span>
                                </q-bar>
                                <q-bar v-if="record.action?.endorseCount">
                                    <q-icon size="xs" color="yellow" name="thumb_up"/>
                                    <span class="pl-2">{{ record.action.endorseCount }}</span>
                                </q-bar>
                            </div>
                        </div>

                        <!-- 有回复的话 -->
                        <div v-if="record.chiller?.length>0">
                            <Reply :records="record.chiller"></Reply>
                        </div>
                    </div>

                </div>
                <!--                    没有消息显示默认内容-->
                <div v-else>
                    <div class="text-center q-pa-md" v-show="!loading">
                        <q-icon name="sentiment_dissatisfied" size="50px" color="grey-5"/>
                        <p class="text-center text-grey-5">暂无消息</p>
                    </div>
                </div>
                <q-inner-loading :showing="loading" dark size="lg">
                    <div class="flex items-center space-x-2">
                        <div class="animate-pulse rounded-full bg-gray-500 h-12 w-12 rounded-full"></div>
                        <div class="space-y-2">
                            <div class="animate-pulse rounded-md bg-gray-500 h-4 w-[200px]"></div>
                            <div class="animate-pulse rounded-md bg-gray-500 h-4 w-[170px]"></div>
                        </div>
                    </div>
                </q-inner-loading>
            </q-scroll-area>
            <AtPopup v-if="atPopup"
                     @closePopup="closePopup"
                     class="global-dark absolute-bottom"></AtPopup>
            <q-footer class="text-white global-dark ">
                <div class="bg-gray-800 flex justify-between  items-center"
                     v-show="replyMsg.nickName && replyMsg.nickName.length>0">
                    <div>
                        <a @click.prevent="(e)=>{toView(e,replyMsg.id)}" :href="'#'+replyMsg.id">正在回复至
                            {{ replyMsg.nickName }}</a>
                    </div>
                    <q-icon name="clear" @click="clearReply"></q-icon>
                </div>
                <q-separator/>
                <q-form v-model="msgFrom" @submit="handleSubmit">
                    <div class="absolute bottom-10 w-full z-10 global-dark  q-pa-md" v-if="fileListBar">
                        <upload v-if="fileListBar" class="full-width" v-model="msgFrom.content.files"/>
                    </div>
                    <q-toolbar class="row">
                        <recordAudio :disable="sendDisable" v-model="msgFrom.content.audio"
                                     @handleSubmit="handleSubmit"/>
                        <q-btn :disable="sendDisable" dense round flat icon="perm_media" class="q-mr-sm"
                               @click="fileListBar=!fileListBar"></q-btn>
                        <q-input dense
                                 ref="inputRef"
                                 clearable standout
                                 filled autofocus
                                 maxlength="1200"
                                 autogrow
                                 class="col-grow text-white"
                                 :bg-color="sendDisable?'blue-grey-11':'white'"
                                 :placeholder="hitStr"
                                 :disable="sendDisable"
                                 type="textarea"
                                 v-model="msgFrom.content.text"
                                 @keyup.ctrl.enter="handleSubmit"
                                 @update:modelValue="handelInput">
                            <!-- at功能 -->
                            <template #prepend>
                                <div class="flex justify-center items-center  no-wrap mb-2 "
                                     v-for="(user,index) in atSelected" :key="user.userId">
                                    <div>
                                        <q-icon name="alternate_email" size="xs"/>
                                    </div>
                                    <div class="hover:accent-blue-300">
                                            <span class="text-sm">{{ user.nickName }}
                                            </span>
                                    </div>
                                    <div>
                                        <q-badge color="red" align="top" rounded label="x"
                                                 @click="atSelected.splice(index, 1)"></q-badge>
                                    </div>
                                </div>
                            </template>
                            <template #append>
                                <span class="text-sm">{{
                                        msgFrom.content.text?.length || ''
                                    }}</span>
                            </template>
                        </q-input>
                        <q-btn dense round flat icon="send" type="submit"
                               :disable="sendDisable"/>
                        <V3Emoji :recent="true"
                                 @clickEmoji="handleClickEmoji"
                                 :fulldata="true"
                                 style="width: 50px"
                                 :size="'mid'">
                            <q-btn :disable="sendDisable" round flat icon="insert_emoticon" class="q-mr-sm"/>
                        </V3Emoji>

                    </q-toolbar>
                </q-form>
            </q-footer>
        </div>

        <!--        查看图片弹出-->
        <q-dialog v-model="showImage" persistent>
            <q-card class="q-pa-lg global-dark">
                <img :src="showImageUrl" width="300px" height="300px" @click="showImage=false">
            </q-card>
        </q-dialog>
    </div>
</template>
<script lang="ts" setup>
import upload from "@/components/upload/index.vue"
import PopupMenu from "@/components/PopupMenu/index.vue"
import Reply from "@/views/msg/views/reply.vue";
import InviteCard from "@/views/msg/views/invite-card.vue";
import Communicate from "@/components/communicate/Communicate.vue";
import AtPopup from "@/views/msg/views/at-popup.vue";


import {useFriendStore} from "@/stores/modules/friend";
import {useAppBoxs} from "@/stores/modules/appBox";
import {useGroupStore} from "@/stores/modules/group";
import useUserStore from "@/stores/modules/user";
import {MsgRecordMsgType, MsgRecordQuery, MsgRecordVO} from "@/api/im/msgRecord/types";
import {useScrollArea} from "@/components/scrollarea";
import {addMsgRecord, updateMsgRead} from "@/api/im/msgRecord";
import {useWs} from "@/stores/modules/ws";
import {bus, OTHER_MSG} from "@/boot/eventbus";
import V3Emoji from "vue3-emoji";
import {formatDate, insertContent, isImage} from "@/utils/verify";
import {playAudio} from "@/utils/ruoyi";
import {SysUserVo} from "@/api/login/login.data";
import {AppboxApplicationVo} from "@/api/im/appBox/type";


const $q = useQuasar()

const useWsStore = useWs();
const friendStore = useFriendStore()
const groupStore = useGroupStore()
const useStore = useUserStore()
const appStore = useAppBoxs()

// ref
const scrollAreaRef = ref<any>(null) // 滚动条
const inputRef = ref() // 输入框
const atPopupRef = ref<any>(null) //atPopupRef

const atPopup = ref(false) // atPopup控制
const atSelected = ref<any>([]) // at选中
const showImage = ref(false) // 图片显示控制
const showImageUrl = ref('')
const replyActive = ref(0) // 回复选中
// 回复显示消息
const replyMsg = ref({
    id: '',// 消息id
    refMsgId: '',  // 回复消息id
    nickName: '', // 回复的用户昵称
    content: '', // 回复的消息内容
})

const loading = ref(false); // 加载控制
const preRecord = ref<number>(); // 上一次记录
const recordList = ref<MsgRecordVO[]>([]);// 聊天记录

const position = ref($q.screen.height) // 当前高度
const {thumbStyle, barStyle} = useScrollArea() // 滚动条样式

const fileListBar = ref(false) // 上传文件列表控制
const popupMenuShow = ref(false) // 弹出右键菜单控制
const isCommunicat = ref(false) // 通话显示控制
const sendDisable = ref(false) // 发送安排控制
const hitStr = ref("请输入内容，ctrl+enter 发送")// 提示框内容
const msgFrom = ref<MsgRecordQuery>({pageNum: 0, pageSize: 50, content: {video: [], text: '', files: [], audio: ''}})


// 关闭弹出菜单
function closePopup(user: SysUserVo) {
    if (atSelected.value.length >= 5) {
        $q.notify({message: '最多只能选择5位用户哦', color: 'negative', position: 'top', timeout: 1000,})
        return
    }
    atPopup.value = false
    atSelected.value.push(user)
}

// 处理输入框@
function handelInput(val) {
    // 只有应用才能 @
    if (appStore.chatType == 3) {
        if (val.endsWith('@')) {
            // 删除最后一个元素
            atPopup.value = !atPopup.value
            msgFrom.value.content.text = val.substring(0, val.length - 1)
        }
    }
}

// 清除回复
function clearReply() {
    replyMsg.value = {content: "", id: "", nickName: "", refMsgId: ""}
    replyActive.value = 0
    msgFrom.value.refMsgId = ''
}

// 初始化
function reset() {

    replyMsg.value = {content: "", id: "", nickName: "", refMsgId: ""}
    msgFrom.value = {pageNum: 0, pageSize: 50, content: {video: [], text: '', files: [], audio: ''}}
    atSelected.value = []
    replyActive.value = 0
    fileListBar.value = false
    sendDisable.value = false
    atPopup.value = false
    fileListBar.value = false
}

// 点击消息
function toView(e, id: string) {
    console.log("点击了", id)
    e.preventDefault();

    let element = document.getElementById(id)
    if (element) {
        // 边框给上紫色
        element.classList.add('reply-active')
        element.scrollIntoView({
            behavior: 'smooth',
            block: 'start',
        });
    }
}

// @回复
function reply(param) {
    replyActive.value = param.id
    replyMsg.value = param
    msgFrom.value.refMsgId = replyMsg.value.refMsgId
    console.log(param)
}

// 选中表情
function handleClickEmoji(v: any) {

    if (!msgFrom.value.content.text) msgFrom.value.content.text = ''
    nextTick(() => {
        let input = inputRef.value.$el.querySelector('textarea')
        msgFrom.value.content.text = insertContent(msgFrom.value.content.text, v.emoji, input.selectionStart)
    })
}


// 发送消息
function handleSubmit() {
    sendDisable.value = true
    switch (appStore.chatType) {
        case 1:
            msgFrom.value.toId = friendStore.currFriendUser.userId
            msgFrom.value.toAppId = undefined
            msgFrom.value.toGroupId = undefined
            break;
        case 2:
            msgFrom.value.toGroupId = groupStore.currGroupInfo.id
            msgFrom.value.toId = undefined
            msgFrom.value.toAppId = undefined
            break;
        case 3:
            msgFrom.value.toAppId = appStore.currAppBox.id
            msgFrom.value.toId = undefined
            msgFrom.value.toGroupId = undefined
            break;
        default:
            console.log("不支持的聊天类型")
    }
    // 是否存在被at的人，去重处理
    if (atSelected.value.length > 0) {
        msgFrom.value.atSelected = []
        let atUsers = new Set(atSelected.value)
        let atStr = ""
        atUsers.forEach(item => {
            atStr += " @" + item.nickName + " "
            msgFrom.value.atSelected.push(item.userId)
        })
        atStr += msgFrom.value.content.text
        msgFrom.value.content.text = atStr
    }

    msgFrom.value.toType = appStore.chatType;
    msgFrom.value.senderId = useStore.currUser?.userId
    msgFrom.value.content = JSON.stringify(msgFrom.value.content)
    console.log("提交的表单=》", msgFrom.value)
    // 发送消息
    addMsgRecord(msgFrom.value).then((res: any) => {
        if (res.code === 200) {
            res.data.content = JSON.parse(res.data.content)
            isInsertMsgRef(res.data)
            reset()
            scrollToBottom()
        } else {
            $q.notify({
                type: 'negative',
                message: res.msg
            })
        }
    }).finally(() => {
        sendDisable.value = false
    })
}

// 插入回复层
function isInsertMsgRef(data: any) {
    if (replyActive.value !== 0) {
        recordList.value.forEach(item => {
            if (item.id == replyMsg.value.id) {
                item.chiller.push(data)
            }
        })
    } else if (data.refMsgId) {
        recordList.value.forEach(item => {
            if (item.id == data.refMsgId) {
                item.chiller.push(data)
            }
        })
    } else {
        recordList.value?.push(data)

    }

}


// 下拉滚动到底部
function scrollToBottom() {
    position.value = position.value + (250 * recordList.value.length);
    nextTick(() => {
        if (scrollAreaRef.value) {
            scrollAreaRef.value.setScrollPosition('vertical', position.value, 300);
        }
    })

}

// 鼠标右击
function rightClick(e: any, data: any) {
    e.preventDefault();

}


/**
 * 设置消息已读
 * @param param
 */
async function setMsgReadStatus(param: any) {
    await updateMsgRead(param).then((res: any) => {
        if (res.code === 200) {
            // 设置消息已读
        }
    })

}


// 清空聊天记录
function clearRecord() {
    reset()
    recordList.value = []
}


// 处理通话消息
const handleCallMessage = async (msgVo: MsgRecordVO) => {
    if (msgVo.msgType === 6) {
        // 通话消息交换SDP
        try {
            const content = JSON.parse(msgVo.content);
            isCommunicat.value = true
            msgVo.content = content
            if (msgVo.rtcType === "answer") {
                bus.emit('answer', msgVo)
                return true
            }
            if (msgVo.rtcType === "offer") {
                bus.emit('offer', msgVo)
                return true
            } else if (msgVo.rtcType === 'candidate') {
                console.log("接收到candidate:", msgVo)
                bus.emit('candidate', msgVo)
                return true
            }
            console.log("未知的选项")
            return false
        } catch (e) {
            console.error("解析消息内容时出错:", e);
            return false;
        }
    }
};


// 处理消息
const handelMessage = async (msgVo: MsgRecordVO) => {
    let friendId = friendStore.currFriendUser.userId;
    //如果是当前对话
    if (msgVo.senderId == friendId && msgVo.toType == 1) {
        // 设置消息为已读
        await setMsgReadStatus({toType: msgVo.toType, id: msgVo.id})
        // 添加到当前对话
        isInsertMsgRef(msgVo)
        // recordList.value.push(msgVo);
        scrollToBottom();
    } else {
        // 其他用户发来消息
        // 更新消息列表
        bus.emit("UPDATE_FRIEND_MSG_DOT", msgVo)
    }
};

// 连接websocket
function connWs() {
    useWsStore.websocket.addEventListener("message", async (e: MessageEvent) => {
        console.log("接收到消息=>", e)
        try {
            let res = JSON.parse(e.data);
            let msgVo: MsgRecordVO = res.data;
            console.log("实际消息=>", msgVo)
            // 处理接收到的消息
            switch (msgVo?.toType) {
                case 1: // 用户消息
                    // 播放提示音
                    playAudio()
                    //如果是通话消息
                    if (await handleCallMessage(msgVo)) {

                    } else {
                        // 用户常规聊天消息
                        await handelMessage(msgVo)
                    }
                    break;
                case 2: // 群聊消息
                    if (msgVo.senderId == useStore.currUser?.userId) {

                    } else if (msgVo.toGroupId == groupStore.currGroupInfo.id && msgVo.toType == 2) {
                        // 添加到当前对话
                        isInsertMsgRef(msgVo)
                        scrollToBottom();
                    } else {

                    }
                    break
                case 3: // 应用频道消息
                    if (msgVo.senderId == useStore.currUser?.userId) {

                    } else if (msgVo.toAppId == appStore.currAppBox?.id && msgVo.toType == 3) {
                        // 添加到当前对话
                        isInsertMsgRef(msgVo)
                        scrollToBottom();
                    } else {

                    }
                    break;
                case 4: // 精选通知
                    // 发射精选通知
                    if (typeof msgVo.content === "string") {
                        bus.emit("CHOICE_MSG", JSON.parse(msgVo.content))
                    }
                    break;
                case 5: // 其他消息
                    if (typeof msgVo.content === "string") {
                        bus.emit("OTHER_MSG", JSON.parse(msgVo.content))
                    }

                    break;
                case 6: // @消息
                    if (typeof msgVo.content === "string") {
                        bus.emit("MENTION_MSG", JSON.parse(msgVo.content))
                    }
                    break;
                default:
                    console.log("未知的消息类型")

            }
        } catch (error) {
            console.error('Error parsing JSON:', error);
        }
    });
}

// 初始化
nextTick(() => {
    connWs()
})

// 清除聊天标识数量
bus.on('DELETE_CLEAR_RECORD', () => {
    clearRecord()
})


// 监听聊天数据,聊天内容转为json格式
bus.on("UPDATE_FRIEND_MSG", async (data: MsgRecordVO[]) => {
    if (data.length <= 0) {
        loading.value = true
        setTimeout(() => loading.value = false, 1500)
    }
    clearRecord();
    recordList.value = data
    scrollToBottom();

});

bus.on("DISABLE_ALL", (flag) => {
    sendDisable.value = flag
    if (flag) {
        hitStr.value = "您无权在此频道发送消息"
    } else {
        hitStr.value = "请输入内容，ctrl+enter 发送"
    }
})

// 发送邀请加入应用
bus.on("SEND_APP_CARD", (friend: SysUserVo, app: AppboxApplicationVo) => {
    console.log("发送应用卡片", friend.avatar, app.appName)
    msgFrom.value.msgType = MsgRecordMsgType.INVITE
    msgFrom.value.content = {
        text: JSON.stringify(app)
    }
    handleSubmit()
})

// 卸载
onUnmounted(() => {
    bus.off("UPDATE_FRIEND_MSG_DOT")
    // bus.off("UPDATE_FRIEND_MSG")
    bus.off("DELETE_CLEAR_RECORD")
    bus.off("SEND_APP_CARD")

})

</script>


<style lang="scss">
.my-emoji {
    vertical-align: middle;
    height: 2em;
    width: 2em;
}

/* 设置字体和大小 */
.discord-font {
    font-family: 'Whitney', 'Helvetica Neue', Helvetica, Arial, sans-serif;
    font-size: 14px;
    line-height: 1.4;
    color: #dcddde;
}
</style>
