<template>
    <div class="q-ma-xs">
        <q-list class="">
            <!--            私信-->
            <q-item clickable v-ripple
                    @click="changeApp(0)">
                <q-item-section avatar>
                    <q-avatar :size="appBoxStore.active===0 ?'50px':'40px'">
                        <q-img src="@/assets/def_0.png"></q-img>
                    </q-avatar>
                    <q-tooltip anchor="center right" :offset="[14,-20]">私信</q-tooltip>
                </q-item-section>
            </q-item>

            <q-separator class="rounded"
                         v-if="appBoxStore.previewAppBox" size="5px" color="purple" spaced/>

            <!--            预览-->
            <q-item v-if="appBoxStore.previewAppBox" clickable v-ripple>
                <q-item-section avatar>
                    <div class="" v-show="appBoxStore.previewAppBox.id === appBoxStore.active"></div>
                    <q-avatar rounded>
                        <div v-html="appBoxStore.previewAppBox.iconUrl"></div>
                    </q-avatar>
                </q-item-section>
                <q-item-section> {{ appBoxStore.previewAppBox.appName }}</q-item-section>
                <q-tooltip>{{ appBoxStore.previewAppBox.appName }}</q-tooltip>
            </q-item>

            <q-separator v-if="appBoxStore.currAppBoxList.length>0" size="5px" color="purple" spaced class="rounded"/>


            <!--            应用-->
            <q-item clickable v-ripple class="hover:bg-gray-800"
                    v-for="(app,index) in appBoxArray" :key="app.id"
                    @click="changeApp(app)">
                <q-item-section avatar>
                    <div class="right-line" v-show="appBoxStore.active === app.id"></div>
                    <div class="" v-html="app.iconUrl"></div>
                </q-item-section>
                <q-item-section> {{ app.appName }}</q-item-section>
                <q-tooltip anchor="top right" self="top middle">
                    {{ app.appName }}
                </q-tooltip>
                <PopupMenu>
                    <template #listItem>
                        <q-list class="global-dark">
                            <q-item>
                                <span>暂无更多操作</span>
                            </q-item>
                            <q-separator color="white" size="2px"></q-separator>
                            <q-item v-show="userStore.currUser.userId == app.createBy">
                                <q-btn @click="deleteAppBox(app.id)" flat dense label="删除此应用">
                                    <template #default>
                                        <svg t="1714134796719" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                             xmlns="http://www.w3.org/2000/svg"
                                             p-id="5603" width="16" height="16">
                                            <path
                                                d="M285.184 869.888L158.72 573.44c-45.056-25.088-74.752-73.728-73.216-128 2.048-77.824 64.512-138.752 142.336-138.752 80.896 2.048 142.336 65.536 142.336 142.336 9.216 0 20.48-5.12 25.6-16.384l1.536-50.688c-17.408-23.552-27.136-52.224-27.136-82.432C370.688 221.184 434.176 158.72 511.488 158.72c78.336 0 141.824 62.976 142.848 139.776 0 31.232-10.24 60.416-27.648 84.48l1.024 47.104c2.048 6.144 6.144 10.752 11.264 14.336 4.608 3.072 9.728 4.608 15.36 4.608 2.048-77.824 65.024-139.264 141.312-139.264 75.776 0 137.728 57.856 142.336 131.584 2.56 37.376-10.24 73.728-34.816 101.888-11.264 12.8-24.064 23.04-38.912 30.72l-125.952 295.424H285.184z"
                                                fill="#FFC53A" p-id="5604"></path>
                                            <path
                                                d="M513.024 241.664c30.72 0 55.808 24.576 56.32 55.808 0 19.968-10.752 38.4-28.672 47.616l2.048 97.792 2.048 5.632c7.168 27.136 24.576 50.176 48.128 65.536 18.944 12.288 40.448 18.432 61.44 18.432 37.376 0 73.728-18.432 95.744-52.224-6.656-9.216-10.24-20.48-10.24-31.744 0.512-30.72 25.6-55.296 56.32-55.296H797.696c29.184 0 53.248 22.528 55.296 51.712 2.048 30.72-20.992 56.832-51.712 58.88l-118.784 279.04H341.504L222.72 503.296c-30.208-2.56-52.736-28.16-51.712-58.368 1.024-30.72 26.112-55.296 56.832-55.296h1.536c31.232 1.024 56.32 27.136 55.296 58.368 0 11.264-3.584 22.528-10.24 31.744 15.36 23.552 38.4 40.448 65.536 48.128 10.24 2.56 19.968 4.096 30.208 4.096 50.176 0 95.744-33.28 109.568-83.968l1.024-4.608 3.072-98.304c-17.408-9.728-28.16-28.16-28.672-47.616 0.512-30.72 25.6-55.808 56.32-55.808H513.024m0-170.496h-2.56c-99.84 0-185.856 67.072-215.04 158.72a223.744 223.744 0 0 0-61.44-10.24h-5.632C103.936 219.648 4.096 316.928 1.024 441.344c-2.048 75.776 33.792 144.384 90.112 187.392L184.32 849.408l44.032 103.936h566.784l44.032-103.936 93.696-220.16c58.88-44.544 95.232-116.224 90.112-195.584-7.68-118.784-106.496-211.968-225.792-211.968-23.552 0-46.592 3.584-68.096 10.24-28.16-93.696-115.2-160.768-216.064-160.768z"
                                                fill="#1C1C1E" p-id="5605"></path>
                                        </svg>
                                    </template>
                                </q-btn>
                            </q-item>

                        </q-list>
                    </template>
                </PopupMenu>
            </q-item>

            <q-separator size="5px" color="purple" spaced class="rounded"/>

            <!--            其他-->
            <q-item clickable v-ripple
                    active-class="bg-fuchsia-600"
                    @click="handelToMore" :active="appBoxStore.active===2">
                <q-item-section avatar>
                    <q-icon size="md" name="expand_circle_down"/>
                    <q-tooltip anchor="bottom left" self="center left">
                        更多频道
                    </q-tooltip>
                </q-item-section>

                <q-item-section> Send</q-item-section>
            </q-item>

            <q-separator/>

            <q-item clickable v-ripple @click="createAppBox.show()">
                <q-item-section avatar>
                    <q-icon size="md" name="add"/>
                    <q-tooltip anchor="center right" self="center left">
                        添加应用
                    </q-tooltip>
                </q-item-section>
                <q-item-section> Drafts</q-item-section>
            </q-item>
        </q-list>

        <CreateAppBox ref="createAppBox"></CreateAppBox>
    </div>
</template>
<script setup lang="ts">
import {useAppBox} from "./appbox.data"
import {useScrollArea} from "@/components/scrollarea"
import {useAppBoxs} from "@/stores/modules/appBox";
import {getAppBoxUserList, removeAppBox, visitorPv, visitorUv} from "@/api/im/appBox";
import {bus} from "@/boot/eventbus";
import CreateAppBox from "@/layouts/appbox/Create-AppBox.vue";
import {AppboxApplicationVo} from "@/api/im/appBox/type";
import PopupMenu from "@/views/group/view/PopupMenu.vue";
import useUserStore from "@/stores/modules/user";
import {UnwrapRef} from "vue";

const appBoxStore = useAppBoxs()
const userStore = useUserStore()
const {appBoxArray, fatchAppBoxList} = useAppBox()
const router = useRouter()
const createAppBox = ref(null)
const instance = getCurrentInstance()
const {thumbStyle, barStyle} = useScrollArea()


function deleteAppBox(appId: number | string) {
    instance?.proxy?.$log_confirm("此操作将永久删除该应用, 是否继续?", "", () => {
        removeAppBox(appId).then(async (res: any) => {
            if (res.code === 200) {
                window.location.reload()

            }
        })
    })

}

// 改变容器
function changeApp(app: AppboxApplicationVo) {
    if (app == 0) {
        appBoxStore.active = 0
    } else {
        appBoxStore.active = app.id
    }
    appBoxStore.currAppBox = app
    appBoxStore.currExteInfo = app.exteInfo
    if (appBoxStore.previewAppBox && appBoxStore.active !== appBoxStore.previewAppBox.id) {
        appBoxStore.previewAppBox = undefined
        appBoxStore.addPop = false
    }

    // 获取当前应用的用户列表
    getAppBoxUserList().then((res: any) => {
        if (res.code === 200) {
            // 设置当前聊天类型为应用
            appBoxStore.chatType = 3
            // 设置到store
            appBoxStore.currAppBoxUserList = res.rows
            // 获取当前应用的用户列表
            bus.emit("ACTIVE_USER_LIST", res.rows)
        }
    })
    // 发射清空当前聊天记录
    bus.emit("DELETE_CLEAR_RECORD")
    // 发送清空右侧内容
    bus.emit("DELETE_CLEAR_GLOBAL")

    // 触发pv行为
    visitorPv(app.id)


    if (app == 0) {
        router.push({path: `/home/0`})
    } else {
        router.push({path: `/home/${app.id}`})
    }
}

function handelToMore() {
    appBoxStore.active = 2
    router.push('/guild-list')
}

onMounted(async () => {
    await fatchAppBoxList()
    if (appBoxStore.previewAppBox) {
        appBoxStore.active = appBoxStore.previewAppBox.id
    } else {
        if (appBoxStore.currAppBox) {
            appBoxStore.active = appBoxStore.currAppBox.id
        } else {
            appBoxStore.active = 0
        }
    }
})

onUnmounted(() => {
    appBoxStore.active = appBoxStore.currAppBox?.id | 0
})


</script>
<style lang="scss">

.active-bar {
    position: absolute;
    left: 10px;
    top: 10px;
    height: 100px;
    width: 20px;
    background-color: white;
}

.right-line {
    display: inline-block;
    position: absolute;
    width: 3px;
    height: 25px;
    background-color: white;

}
</style>
