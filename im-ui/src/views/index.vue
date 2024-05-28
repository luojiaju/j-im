<!--suppress ALL -->
<template>
    <div>
        <div v-if="appBoxPop" class="bg-[#3f51b5] flex items-center justify-between  text-white w-full ">
            <div class="flex items-center space-x-2">
                <q-btn @click="breakToGuildList" class="q-ma-xs" outline dense label="后退"
                       icon="mdi-arrow-left"></q-btn>
            </div>
            <div>
                <span>您当前正处于预览模式。加入该应用开始聊天吧！</span>
            </div>
            <q-btn @click="handelAddAppBox" outline dense
                   class="q-ma-xs hover:bg-primary/90 h-10 px-4 py-2 bg-white text-[#3f51b5]">
                加入此频道
            </q-btn>
        </div>
        <q-layout view="lHh lpR lFr" container :style="style">
            <q-header elevated bordered reveal>
                <q-toolbar class="global-dark">
                    <!-- 顶部显示聊天主题-->
                    <div v-if="topFlag === 1">
                        <q-avatar><img :src="userTopData.avatar"/></q-avatar>
                        {{ userTopData.nickName || '' }}
                    </div>
                    <!-- 如果是群聊-->
                    <div v-else-if="topFlag === 2">
                        <span class="q-subtitle-1 q-pl-md">{{ groupTopData.groupName || '' }}</span>
                    </div>
                    <div v-else-if="topFlag === 3">
                        <span class="q-subtitle-1 q-pl-md">{{ appTopData.appName || '' }}</span>
                    </div>
                    <q-space/>
                    <q-btn-dropdown dropdown-icon="notifications" flat>
                        <notifications  ></notifications>
                        <template #label>
                            <div>
                                <q-badge color="grey" text-color="grey"  rounded  floating></q-badge>
                            </div>
                        </template>
                    </q-btn-dropdown>
                    <q-btn round flat icon="account_circle" @click="accountVisible">
                        <q-tooltip>隐藏|显示用户个人资料</q-tooltip>
                    </q-btn>
                    <q-btn round flat icon="attachment" v-show="false">
                        <q-tooltip>分享连接</q-tooltip>
                    </q-btn>
                </q-toolbar>
            </q-header>
            <!--           应用Drawer-->
            <GlobalLeft v-if="appId !== '0'"></GlobalLeft>
            <!--           好友群聊关系Drawer-->
            <RealDrawer v-else></RealDrawer>
            <!--           当前活动Drawer-->
            <GlobaRight></GlobaRight>
            <q-page-container>
                <q-page>
                    <!--                  <router-view name="msg"/>-->
                    <msg ref="msgRecordRef"></msg>
                </q-page>
            </q-page-container>
            <!--           我的弹窗-->
            <MeInfo ref="meInfoRef"></MeInfo>
            <!--           添加搜索好友弹窗-->
            <searchFriend v-if="isShowSearchFriend"></searchFriend>
            <!--           添加群聊-->
            <addGroupDialog ref="addGroupChatDialogRef"></addGroupDialog>
        </q-layout>
    </div>
</template>

<script lang="ts" setup>
import 'vue3-emoji/dist/style.css'
import useUserStore from "@/stores/modules/user";
import {useHome} from "./index.data"

import searchFriend from "@/views/friend/searchFriend/index.vue"
import MeInfo from "@/views/me/index.vue"
import addGroupDialog from "@/views/group/view/addDialog.vue"

import {AppboxApplicationVo} from "@/api/im/appBox/type";
import {useAppBoxs} from "@/stores/modules/appBox";
import {addAppBox, queryAppByUser} from "@/api/im/appBox";
import {useFriendStore} from "@/stores/modules/friend";
import {useGroupStore} from "@/stores/modules/group";
import {bus} from "@/boot/eventbus"
import {SysUserVo} from "@/api/login/login.data";
import {ChatGroupData} from "@/api/im/chatGroup/type";
import {useWs} from "@/stores/modules/ws";
import {getToken} from "@/utils/auth";

const route = useRoute()
const router = useRouter()
const $q = useQuasar();
const instance = getCurrentInstance()
const useUser = useUserStore();
const useWsStore = useWs();

const {addPop, previewAppBox, setCurrAppBoxList, setActive} = useAppBoxs()
const appStore = useAppBoxs()

const {isShowSearchFriend} = useFriendStore()

const {
    leftDrawerOpen, rigthDrawerOpen, search, message, panels, currentFriendId,
    setCurrentConversation
} = useHome()


// ref
const meInfoRef = ref<any>(null)
const addGroupChatDialogRef = ref<any>(null)
const msgRecordRef = ref<any>(null)

const appId = ref<string>('0')
const appBoxPop = ref(addPop)
const unReadCount = ref(0)

// 头部信息
const topFlag = ref(0)
const userTopData = ref<SysUserVo>({})
const groupTopData = ref<ChatGroupData>({})
const appTopData = ref<AppboxApplicationVo>({})

// // 监听路由参数
watch(() => route.params, (newParams: any, oldParams: any) => {
        appId.value = newParams.id
    }, {immediate: true} // 立即执行一次 handler
);

const style = computed(() => ({
    height: $q.screen.height + "px",
}));
const channData = ref<AppboxApplicationVo>();
let cursorPos = 0

function accountVisible() {
    bus.emit("ACTIVE_USER_TOGGLE")
}


// 路由跳转/guild-list
function breakToGuildList() {
    // 清除预览应用
    appBoxPop.value = false
    router.replace("/guild-list")
}


// 添加应用
function handelAddAppBox() {
    instance?.proxy?.$log_confirm("加入应用", "确定加入此应用吗?", () => {
        let appId = previewAppBox?.id
        // 添加应用
        addAppBox(appId).then((res: any) => {
            if (res.code === 200) {
                appBoxPop.value = false
                queryAppByUser().then((res: any) => {
                    if (res.code == 200)
                        setCurrAppBoxList(res.data)
                })
                setActive(appId)
            }
            useAppBoxs().previewAppBox = undefined
        })

    })
}


onMounted(async () => {
    useWsStore.initWs(`ws://${import.meta.env.VITE_APP_BASE_API_IP}:8080/im-websocket?token=${getToken()}`)

    await useUser.getUserinfo()
    appStore.currAppBoxList.forEach(item => {
        if (item.id === appStore.previewAppBox?.id) {
            appBoxPop.value = false
        } else {
        }
    })
})

bus.on("UNREAD_COUNT", (data: number) => {
    unReadCount.value += data
})

// 页面顶部聊天头部数据
bus.on("TOP_HEAD_DATA", (data: any) => {
    // 如果时用户,如果时群聊,如果时群聊
    if (data.nickName) {
        topFlag.value = 1
        userTopData.value = data
    } else if (data.groupName) {
        topFlag.value = 2
        groupTopData.value = data
    } else if (data.appName) {
        topFlag.value = 3
        appTopData.value = data
    } else {
        return
    }
});


onUnmounted(() => {
    bus.off("TOP_HEAD_DATA")
    bus.off("UNREAD_COUNT")
})

</script>


