<template>
    <q-drawer v-model="rgDrawerOpen" side="right" class="global-dark" show-if-above
              :width="250">

        <!--            用户列表-->
        <userComponents v-if="groupUserList.length > 0" :user-list="groupUserList" title="用户列表" show/>
        <!--            用户卡片-->
        <ShopCard v-else :current-chat-user="currentChatUser"></ShopCard>
    </q-drawer>
</template>
<script lang="ts" setup>
import userComponents from "@/components/user/userlist/index.vue"
import ShopCard from "@/components/card/ShopCard.vue";
import {bus} from "@/boot/eventbus";
import {SysUserVo} from "@/api/login/login.data";


const rgDrawerOpen = ref(true)


const groupUserList = ref<Array<any>>([])

let currentChatUser = reactive({})


onMounted(() => {
    bus.on("ACTIVE_CURRENT_USER", (data: SysUserVo) => {
        if (data) {
            currentChatUser = {...data}
            groupUserList.value = []
        }
    })
    // 监听当前活动用户列表
    bus.on("ACTIVE_USER_LIST", (data: SysUserVo[]) => {
        if (data && data.length > 0) {
            groupUserList.value = data
            currentChatUser = {}
        }
    })

    // 是否显示当前活动用户
    bus.on("ACTIVE_USER_SHOW", () => {
        rgDrawerOpen.value = true
    })

    // 是否显示当前活动用户
    bus.on("ACTIVE_USER_CLOSE", () => {
        rgDrawerOpen.value = false
    })

    bus.on("ACTIVE_USER_TOGGLE", () => {
        rgDrawerOpen.value = !rgDrawerOpen.value
    })

    bus.on("DELETE_CLEAR_GLOBAL", () => {
        groupUserList.value = []
        currentChatUser = {}
    })
})

onUnmounted(() => {
    bus.off("ACTIVE_CURRENT_USER")
    bus.off("ACTIVE_USER_LIST")
    bus.off("ACTIVE_USER_SHOW")
    bus.off("ACTIVE_USER_CLOSE")
    bus.off("ACTIVE_USER_TOGGLE")
})

</script>


<style scoped lang="scss">
@import "@/css/card/def";


</style>
