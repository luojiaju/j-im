<!--
  @FileDescription:  ,
  @Date: 2024/2/18 18:10 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div>
        <q-drawer v-model="leftDrawerOpen"
                  class="global-dark q-pa-xs" show-if-above :breakpoint="690"
                  :mini-width="250" :width="250">
            <q-toolbar>
                <div>{{ useUser.currUser?.nickName }}</div>
                <q-space/>
                <im-avatar :src="useUser.currUser.avatar" is-online></im-avatar>
                <q-btn round flat icon="more_vert">
                    <q-menu auto-close :offset="[-30, 8]">
                        <q-list class="global-dark">
                            <q-item clickable>
                                <q-item-section @click="()=>{useUser.logOut();$router.push('/login')}">
                                    退出
                                </q-item-section>
                            </q-item>
                            <q-item clickable @click="showMeInfo">
                                个人资料
                            </q-item>
                        </q-list>
                    </q-menu>
                </q-btn>
            </q-toolbar>
            <q-list bordered separator class="">
                <q-item clickable v-ripple :active="true" active-class="bg-grey-5">
                    <q-item-section avatar>
                        <q-icon name="people"/>
                    </q-item-section>
                    <q-item-section>好友</q-item-section>
                    <q-item-section side></q-item-section>
                </q-item>

                <q-item v-ripple active-class=" text-grey-8" disable>
                    <q-tooltip>未开放</q-tooltip>
                    <q-item-section avatar>
                        <q-icon name="shop"/>
                    </q-item-section>
                    <q-item-section>商店</q-item-section>
                    <q-item-section side></q-item-section>
                </q-item>
            </q-list>
            <q-separator size="0.5rem"></q-separator>
            <q-banner inline-actions rounded dark class="" size="sm">
                <q-btn flat size="1rem" label="聊天">
                </q-btn>
                <template v-slot:action>
                    <q-btn-dropdown dense flat dropdown-icon="add">
                        <q-list class="global-dark">
                            <q-item>
                                <q-btn flat label="新建私聊" @click="addGroupChatDialogRef?.show()"></q-btn>
                            </q-item>
                            <q-item>
                                <q-btn flat label="添加好友" @click="()=>{
                                friendStore.isShowSearchFriend = true;
                                searchFriendRef?.show()
                            }"></q-btn>
                            </q-item>
                        </q-list>
                    </q-btn-dropdown>
                </template>
            </q-banner>

            <q-tabs align="left" no-caps v-model="tabs" class="shadow-2">
                <q-tab name="私信" label="私信"/>
                <q-tab name="好友" label="好友" @click="()=>{friendQueryForm.relationStatus = 2;fatchFriendList()}"/>
                <q-tab name="待验证" label="待验证"
                       @click="()=>{friendQueryForm.relationStatus = 1;fatchFriendList()}"/>
                <q-tab name="屏蔽" label="屏蔽" @click="()=>{friendQueryForm.relationStatus = 3;fatchFriendList()}"/>
            </q-tabs>
            <q-tab-panels v-model="tabs" class="global-dark" animated swipeable
                          vertical transition-prev="jump-up" transition-next="jump-up">
                <q-tab-panel name="私信">
                    <group :ref="groupRef"></group>
                </q-tab-panel>
                <q-tab-panel name="好友">
                    <q-toolbar>
                        <q-input rounded outlined dense class="WAL__field full-width"
                                 bg-color="white"
                                 v-model="friendQueryForm.userName"
                                 @keydown.enter="handelSearchFriend"
                                 placeholder="搜索好友用户名称 开始新对话">
                            <template v-slot:append>
                                <q-btn flat dense icon="search"
                                       @click="handelSearchFriend"/>
                            </template>
                        </q-input>
                    </q-toolbar>
                    <q-list>
                        <q-item v-for="(friend, index) in friendList" :key="friend.userId" clickable
                                @click="clickCurrFriendUser(friend)"
                                @click.right.prevent="(e)=>{rightClick(e,friend)}">
                            <PopupMenu :friend="friend">
                                <template #listItem>
                                    <q-list class="global-dark">
                                        <q-item class=" ">
                                            <q-item-section class="">
                                                <q-btn color="blue" text-color="white" @click="handelFriendInfo(friend)"
                                                       dense flat square label="个人资料"></q-btn>
                                            </q-item-section>
                                        </q-item>
                                        <q-separator size="1px" color="gray"/>
                                        <q-item class="">
                                            <q-item-section class="">
                                                <q-btn color="red" text-color="white"
                                                       @click="handelFriendVideo(friend?.userId)"
                                                       dense flat square label="呼叫">
                                                    <q-tooltip>试验阶段</q-tooltip>
                                                </q-btn>
                                            </q-item-section>
                                        </q-item>
                                        <q-item class=" ">
                                            <q-item-section class="">
                                                <q-btn color="blue" text-color="white" flat label="邀请至应用"
                                                       icon-right="keyboard_arrow_right">
                                                    <q-menu anchor="top end" self="top start">
                                                        <q-list class="global-dark q-gutter-xs">
                                                            <q-item class="hover:bg-blue-400" @click=""
                                                                    v-for="(app,index) in userAppBoxList"
                                                                    :key="app.id">
                                                                <q-item-section>
                                                                    <span v-text="app.appName"
                                                                          @click="handelInvite(friend,app)"></span>
                                                                </q-item-section>
                                                            </q-item>
                                                        </q-list>
                                                    </q-menu>
                                                </q-btn>
                                            </q-item-section>
                                        </q-item>

                                        <q-item class=" " v-show="userGroupList.length>0">
                                            <q-item-section class="">
                                                <q-btn color="blue" text-color="white" flat label="邀请至私聊"
                                                       icon-right="keyboard_arrow_right">
                                                    <q-menu anchor="top end" self="top start">
                                                        <q-list class="global-dark q-gutter-xs">
                                                            <q-item class="hover:bg-blue-400"
                                                                    v-for="(group,index) in userGroupList"
                                                                    :key="group.id">
                                                                <q-item-section>
                                                                    <span v-text="group.groupName"
                                                                          @click="handelGroupInvite(friend,group)"></span>
                                                                </q-item-section>
                                                            </q-item>
                                                        </q-list>
                                                    </q-menu>
                                                </q-btn>
                                            </q-item-section>
                                        </q-item>

                                        <q-separator size="1px" color="gray"/>
                                        <q-item class=" ">
                                            <q-item-section>
                                                <q-btn color="blue" text-color="white" dense flat square
                                                       label="屏蔽此人"
                                                       @click="handelShield(friend)"></q-btn>
                                            </q-item-section>
                                        </q-item>

                                        <q-item class=" ">
                                            <q-item-section>
                                                <q-btn color="blue" text-color="white" dense flat square
                                                       label="删除此人"
                                                       @click="handelDel(friend)"></q-btn>
                                            </q-item-section>
                                        </q-item>
                                        <q-separator size="1px" color="gray"/>
                                        <q-item @click="" v-show="false">
                                            <q-btn color="blue" text-color="white" flat dense label="导出聊天记录"
                                                   icon-right="keyboard_arrow_right">
                                                <q-menu anchor="top end" self="top start">
                                                    <q-list class="global-dark">
                                                        <q-item @click="">
                                                            <q-item-section>
                                                                <q-btn color="blue" text-color="white" flat dense
                                                                       label="PDF"
                                                                       @click="handelExport('PDF')"></q-btn>
                                                            </q-item-section>
                                                        </q-item>

                                                        <q-item @click="">
                                                            <q-item-section>
                                                                <q-btn color="blue" text-color="white" flat dense
                                                                       label="Image"
                                                                       @click="handelExport('Image')"
                                                                ></q-btn>
                                                            </q-item-section>
                                                        </q-item>
                                                    </q-list>
                                                </q-menu>

                                            </q-btn>

                                        </q-item>
                                    </q-list>
                                </template>
                            </PopupMenu>
                            <q-item-section avatar>

                                <im-avatar :src="friend.avatar" :is-online="friend.online" :unread="friend?.unread">
                                </im-avatar>

                            </q-item-section>

                            <q-item-section>
                                <q-item-label lines="1">
                                    {{ friend.notes || friend?.nickName }}
                                </q-item-label>
                                <q-item-label class="conversation__summary" caption>
                                    <span class="text-grey-8">
                                        {{ friend?.userType }}
                                    </span>
                                </q-item-label>
                                <q-item-label>
                                      <span class="ellipsis text-gray-400" style="font-size: 10px">
                                        {{ friend.attachMsg || "上次登录:" + formatDate(friend.loginDate, "MM-DD") }}
                                    </span>
                                </q-item-label>
                            </q-item-section>
                            <q-space/>
                        </q-item>
                    </q-list>
                </q-tab-panel>
                <q-tab-panel name="待验证">
                    <q-toolbar>
                        <q-input rounded outlined dense class="WAL__field full-width"
                                 bg-color="white" v-model="friendQueryForm.userName"
                                 @keydown.enter="handelSearchFriend"
                                 placeholder="搜索好友用户名称 开始新对话">
                            <template v-slot:append>
                                <q-btn dense icon="search"
                                       @click="handelSearchFriend"/>
                            </template>
                        </q-input>
                    </q-toolbar>
                    <q-list>
                        <q-item
                            v-for="(friend, index) in friendList" :key="friend.userId"
                            clickable
                            @dblclick="()=>{setCurrentConversation(index); currentFriendId = friend.userId; panels = friend.userId}">
                            <q-item-section avatar>
                                <im-avatar :src="friend.avatar" :is-online="friend.online"></im-avatar>
                            </q-item-section>

                            <q-item-section>
                                <q-item-label lines="1">
                                    {{ friend ? friend.nickName : '' }}
                                </q-item-label>
                                <q-item-label class="conversation__summary" caption>
                                    <q-icon name="check"/>
                                    {{ friend ? friend.userType : '' }}
                                </q-item-label>
                            </q-item-section>
                            <q-space/>
                            <q-item-section>
                                <q-btn dense class="text-green-5" @click="handelAccept(friend)">同意</q-btn>
                                <q-btn dense class="text-red-400" @click="handelReject(friend)">拒绝
                                </q-btn>
                            </q-item-section>
                        </q-item>
                    </q-list>
                </q-tab-panel>
                <q-tab-panel name="屏蔽">
                    <q-toolbar>
                        <q-input rounded outlined dense class="WAL__field full-width"
                                 bg-color="white" v-model="friendQueryForm.userName"
                                 @keydown.enter="handelSearchFriend"
                                 placeholder="搜索好友用户名称 开始新对话">
                            <template v-slot:append>
                                <q-btn dense icon="search"
                                       @click="handelSearchFriend"/>
                            </template>
                        </q-input>
                    </q-toolbar>
                    <q-list>
                        <q-item
                            v-for="(friend, index) in friendList"
                            :key="friend.userId"

                            @dblclick="()=>{setCurrentConversation(index);
                currentFriendId = friend.userId;
                panels = friend.userId
              }"
                        >
                            <q-item-section avatar>
                                <im-avatar :src="friend.avatar" :is-online="friend.online"></im-avatar>

                            </q-item-section>

                            <q-item-section>
                                <q-item-label lines="1">
                                    {{ friend ? friend.nickName : '' }}
                                </q-item-label>
                                <q-item-label class="conversation__summary" caption>
                                    <q-icon name="check"/>
                                    {{ friend ? friend.userType : '' }}
                                </q-item-label>
                            </q-item-section>
                            <q-space/>
                            <q-item-section side>
                                <q-btn dense class="text-red-400" @click="handelDel(friend)">解除
                                </q-btn>
                            </q-item-section>
                        </q-item>
                    </q-list>
                </q-tab-panel>

            </q-tab-panels>

        </q-drawer>
        <addGroupDialog ref="addGroupChatDialogRef"></addGroupDialog>
        <searchFriend ref="searchFriendRef"></searchFriend>
        <meInfo ref="meInfoRef"></meInfo>

        <DialogUserinfo ref="dialogUserinfoRef"></DialogUserinfo>
    </div>
</template>
<script lang="ts" setup>
import addGroupDialog from "@/views/group/view/addDialog.vue"
import group from "@/views/group/index.vue"
import meInfo from "@/views/me/index.vue"

import useUserStore from "@/stores/modules/user";
import {useHome} from "@/views/index.data";
import {useFriendStore} from "@/stores/modules/friend";
import {SysUserVo} from "@/api/login/login.data";
import {listFriend} from "@/api/im/friend";
import {getUserByUserId} from "@/api/user";
import {listMsgRecord, updateMsgRead} from "@/api/im/msgRecord";
import {useAppBoxs} from "@/stores/modules/appBox";
import {bus} from "@/boot/eventbus";
import {MsgRecordVO} from "@/api/im/msgRecord/types";
import PopupMenu from "@/views/left-drawer/view/PopupMenu.vue";
import DialogUserinfo from "@/components/user/DialogUserinfo/DialogUserinfo.vue";
import {AppboxApplicationVo} from "@/api/im/appBox/type";
import ImAvatar from "@/components/global/im-avatar.vue";
import {formatDate, parseTime} from "@/utils/ruoyi";
import {useGroupStore} from "@/stores/modules/group";
import {ChatGroupData, ChatGroupForm, GroupUserChatForm} from "@/api/im/chatGroup/type";
import {addInvite} from "@/api/im/chatGroup";

const friendList = ref<SysUserVo[]>([])

const {
    leftDrawerOpen, panels, currentFriendId,
    setCurrentConversation
} = useHome()

const {
    friendQueryForm, setCurrFriendUser,
    handelSearchFriend, handelDeleteFriend, handelAcceptFriend, handelShieldFriend
} = useFriendStore()
const friendStore = useFriendStore()

const useUser = useUserStore()


const $q = useQuasar();
const instance = getCurrentInstance()

const tabs = ref('好友');

// ref
const addGroupChatDialogRef = ref(null)
const groupRef = ref<any>(null)
const searchFriendRef = ref(null)
const dialogUserinfoRef = ref(null)
const meInfoRef = ref(null)

//store
const appBox = useAppBoxs()
const groupStore = useGroupStore()

const userAppBoxList = computed(() => {
    return appBox.currAppBoxList
})

const userGroupList = computed(() => {
    return groupStore.groupList
})

const style = computed(() => ({
    height: $q.screen.height + "px",
}));

// 群组邀请
function handelGroupInvite(friend: SysUserVo, group: ChatGroupData) {
    instance?.proxy?.$log_confirm("邀请好友", `是否将好友${friend.nickName}邀请到群聊${group.groupName}？`, async () => {
        let param: GroupUserChatForm = {
            groupId: group.id,
            userId: friend.userId,
            role: 3,
            groupNickname: "",
            top: 0,
            unread: 0
        }
        await addInvite(param)
    })

}

// 弹出个人信息
function handelFriendInfo(friend: SysUserVo) {
    if (dialogUserinfoRef.value) {
        dialogUserinfoRef.value.showDialog(friend)
    }

}

// 右键菜单
function rightClick(e: Event, friend: SysUserVo) {
    e.preventDefault()
}

function showMeInfo() {
    if (meInfoRef.value) {
        meInfoRef.value?.show()
    }
}

// 屏蔽好友
function handelShield(friend: SysUserVo) {
    instance?.proxy?.$log_confirm("屏蔽好友", "", async () => {
        await handelShieldFriend(friend)
        friendList.value = friendList.value.slice(0, friendList.value.indexOf(friend))

    })
}

// 删除好友
function handelDel(friend: SysUserVo) {
    instance?.proxy?.$log_confirm("删除好友", "", async () => {
        await handelDeleteFriend(friend.userId)
        friendList.value = friendList.value.slice(0, friendList.value.indexOf(friend))
    })

}

// 接受好友
function handelAccept(friend: SysUserVo) {
    handelAcceptFriend(friend)
    // 更新好友列表
    friendList.value = friendList.value.filter((item: SysUserVo) => {
        return item.userId != friend.userId
    })
}

// 拒绝添加好友
function handelReject(friend: SysUserVo) {
    handelDeleteFriend(friend.userId)
    // 更新好友列表
    friendList.value = friendList.value.filter((item: SysUserVo) => {
        return item.userId != friend.userId
    })
}

// 获取好友信息
async function clickCurrFriendUser(friend: SysUserVo) {
    let param: any = {}
    getUserByUserId(friend.userId).then((res: any) => {
        if (res.code == 200) {
            // 得到好友信息
            friendStore.currFriendUser = res.data.user
            param = {
                senderId: useUser.currUser?.userId,
                toId: friendStore.currFriendUser.userId,
                toType: 1,
                msgType: 1,
                pageSize: 50,
                pageNum: 0
            }
            // 得到好友聊天记录
            listMsgRecord(param).then((res: any) => {
                if (res.code == 200) {
                    useAppBoxs().chatType = 1
                    bus.emit("UPDATE_FRIEND_MSG", res.rows)
                }
            }).finally(() => {
                // 如果有未读消息, 则更新未读消息
                updateMsgRead(param).then((res: any) => {
                    if (res.code === 200) {
                        friendList.value = friendList.value.map(item => {
                            if (`${item.userId}` === `${param.toId}`) {
                                item.unread = 0
                            }
                            return item;
                        })
                    }
                })
            })
        }
    })
    // 更新头部信息
    bus.emit("TOP_HEAD_DATA", friend)
    // 更新当前聊天用户
    bus.emit("ACTIVE_CURRENT_USER", friend)
}

// 获取好友列表
async function fatchFriendList() {
    // 获取当前用户id
    friendQueryForm.userId = useUser.currUser?.userId

    let res = await listFriend(friendQueryForm)
    if (res.code === 200) {
        friendList.value = res.rows
    }

}

// 邀请好友到应用
function handelInvite(friend: SysUserVo, app: AppboxApplicationVo) {
    // 发送应用卡片
    console.log("handelInvite", friend, app)
    bus.emit("SEND_APP_CARD", friend, app)

}

// 好友视频
function handelFriendVideo(userId: any) {
    bus.emit("startCall", [userId])
    bus.emit("ACTIVE_USER_CLOSE")
}


// 导出
function handelExport(type) {
    bus.emit("EXPORT_MSG_LIST", type)
}


onMounted(async () => {
    await useUser.getUserinfo()
    await fatchFriendList()
})

// 好友有新消息
bus.on("UPDATE_FRIEND_MSG_DOT", (msgVo: MsgRecordVO) => {
    // 好友有新消息
    friendList.value = friendList.value.map(item => {
        if (`${item.userId}` === `${msgVo.senderId}`) {
            item.unread += 1;
        }
        return item;
    })
})

// 刷新好友列表
bus.on("UPDATE_FRIEND_LIST", (friend: SysUserVo) => {
    fatchFriendList()

})


onUnmounted(() => {
    bus.off("UPDATE_FRIEND_MSG_DOT")
    bus.off("UPDATE_FRIEND_LIST")
})

</script>


<style scoped>

</style>
