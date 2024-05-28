<template>
    <q-drawer v-model="rgDrawerOpen" :width="250"
              side="left" class="global-dark" show-if-above :breakpoint="690">
        <q-scroll-area style="height: calc(100% - 150px); margin-top: 150px;">
            <q-list padding>
                <q-item clickable v-ripple class="bi-menu-button"
                        v-for="(info,index) in applicationInfo" :key="info.id"
                        @click="channerClickHandel(info)" :active="active === info.id">
                    <q-item-section avatar>
                    </q-item-section>
                    <q-item-section>
                        {{ info.appName }}
                        <!-- 如果是视频 -->
                        <!-- 如果是发布通知 -->
                        <!-- 如果是语音 -->
                        <!-- 如果是群聊 -->
                        <!-- 如果是反馈 -->
                    </q-item-section>
                    <q-item-section side>
                        <q-icon name="person_add"></q-icon>
                    </q-item-section>
                </q-item>
            </q-list>
        </q-scroll-area>
        <q-img class="absolute-top"
               :src="appInfo.coverUrl" style="height: 150px">
            <div class="absolute-top text-center">
                <div class="column">
                    <div class="text-weight-bold">
                        <q-btn dense icon="local_fire_department" color="red" flat>{{ appInfo.popularity }}</q-btn>
                        {{ appInfo.appName }}
                    </div>
                    <q-btn-dropdown class="absolute-right " flat>
                        <q-list class="global-dark">
                            <q-item clickable dense @click="exitAppBox(appInfo.id)">
                                <q-item-section>
                                    退出应用
                                </q-item-section>
                            </q-item>
                            <q-item clickable dense @click="appBoxEditRef.show()" v-show="false">
                                <q-item-section>
                                    应用设置
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </q-btn-dropdown>
                </div>
            </div>
            <template #error>
                <q-img src="https://cdn.quasar.dev/img/parallax1.jpg" style="height: 150px"/>
            </template>
        </q-img>

        <GlobalBottomMe></GlobalBottomMe>
        <app-box-edit ref="appBoxEditRef">
            <template #default>
                <div class="q-pa-md">
                    {{ appBoxStore.currAppBox.appName || '' }} 设置
                    <q-card-section>
                        <!--创建者可配置应用和频道-->
                        <div v-if="appBoxStore.currAppBox.createBy == userStore.currUser.userId">
                            <!-- 顶级 应用配置 -->
                            <app-box-master v-if="appBoxStore.currAppBox.parentId==0">
                            </app-box-master>
                            <!-- 子应用配置 -->
                            <app-box-child v-else></app-box-child>
                        </div>
                        <div v-else>
                        <!-- 普通用户 -->

                        </div>
                    </q-card-section>
                </div>
            </template>
        </app-box-edit>
    </q-drawer>
</template>
<script lang="ts" setup>

import {useApplicationInfo} from "@/components/global/left/left.data";
import {useAppBoxs} from "@/stores/modules/appBox";
import {AppboxApplicationVo} from "@/api/im/appBox/type";
import useUserStore from "@/stores/modules/user";
import {bus} from "@/boot/eventbus";
import {listMsgRecord} from "@/api/im/msgRecord";
import {Notify} from "quasar";
import {removeAppBox} from "@/api/im/appBox";
import AppBoxEdit from "@/components/global/left/view/app-box-edit.vue";
import AppBoxMaster from "@/components/global/left/view/app-box-master.vue";
import AppBoxChild from "@/components/global/left/view/app-box-child.vue";

const route = useRoute()
const instance = getCurrentInstance()
const appBoxStore = useAppBoxs()
const userStore = useUserStore()
const {fetchApplicationInfo, appInfo, applicationInfo} = useApplicationInfo()

const appBoxEditRef = ref(null)

const rgDrawerOpen = ref(true)
const appId = ref('')
const active = ref<any>(appInfo.value.id)



// 监听路由参数
watch(() => route.params,
    (newParams, oldParams) => {
        appId.value = <string>newParams.id
    }, {immediate: true} // 立即执行一次 handler
);

// 监听当前应用变化
watch(() => appBoxStore.active, (newVal, oldVal) => {
        active.value = newVal ? newVal : 0
        if (newVal) {
            fetchApplicationInfo(active.value)
        } else {

        }

    }, {immediate: true}
);

// 点击频道
async function channerClickHandel(info: AppboxApplicationVo) {
    // 更新头部信息
    bus.emit("TOP_HEAD_DATA", info)
    // [1]点击激活
    active.value = info.id
    // [2]激活变更网页头部内容
    appBoxStore.hitTitleBar = {
        isAppBox: true,
        id: info.id,
        title: info.appName,
        iconUrl: info.iconUrl,
        avatar: undefined,
        remark: info.remark
    }
    // [3]得到当前频道的聊天列表
    let {code, rows, msg} = await listMsgRecord({toAppId: info.id, toType: 3});
    // [4]更新聊天列表
    if (code == 200) {
        // 更新当前应用信息
        appBoxStore.currAppBox = {...info}
        appBoxStore.chatType = 3
        // 发射内容到消息页面
        bus.emit("UPDATE_FRIEND_MSG", rows)
        flag()
    } else {
        Notify.create({
            message: "该频道可能在维护,暂无内容哦!",
            color: 'warning',
            position: 'top',
            timeout: 3000,
        })
    }
}

function flag(){
    let role = appBoxStore.currExteInfo?.role // 得到当前角色 0 创建者 1 管理者 3 成员
    if(role == 0){
        bus.emit("DISABLE_ALL",false)
    } else if(role == 1 && (appBoxStore.currAppBox.permission == 1 || appBoxStore.currAppBox.permission == 3)){
        bus.emit("DISABLE_ALL",false)
    }else if(role == 3 && appBoxStore.currAppBox.permission == 3){
        bus.emit("DISABLE_ALL",false)
    }else{
        bus.emit("DISABLE_ALL",true)
    }
}

function exitAppBox(appId: string) {
    instance?.proxy?.$log_confirm("退出应用", "确定退出此应用吗?", async () => {
        let {code, msg} = await removeAppBox(appId)
        if (code == 200) {
            // 页面刷新
            window.location.reload()
        }
    })
}


onMounted(() => {
    fetchApplicationInfo(appId.value)

})

defineExpose({
    // init
})
</script>


<style scoped>

</style>
