<!--
  @FileDescription:  ,
  @Date: 2024/3/30 22:41 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div v-if="currentChatUser&& currentChatUser.userId">
        <div class=" text-white rounded-lg p-4 m-4 " :class="classes">
            <div class="flex justify-center items-center relative"><span
                class="relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full ring-2 ring-offset-2 ring-offset-[#6e00b3] ring-white">
<!--                    <img v-if="currentChatUser.avatar" class="aspect-square h-full w-full"-->
<!--                         :src="currentChatUser.avatar">-->
                <im-avatar :src="currentChatUser.avatar" :is-online="currentChatUser.online"></im-avatar>
                </span>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="absolute bottom-0 right-0 bg-white text-[#6e00b3] rounded-full p-1">
                    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10"></path>
                    <path d="m9 12 2 2 4-4"></path>
                </svg>
            </div>
            <div class="mt-4 ">
                <h1 class="text-lg font-bold">{{ currentChatUser.nickName || '你的好友' }}</h1>
                <h5 class=" font-bold">{{ currentChatUser.userName || '你的好友' }}</h5>
                <p class="text-sm opacity-70">{{ currentChatUser.email || '暂无邮箱' }}</p>
            </div>
            <p class="text-sm opacity-70 ellipsis-2-lines overflow-hidden">{{currentChatUser.remark || ''}}</p>
            <div class="divide-y divide-gray-500 mt-4">
                <div class="py-2">
                    <h2 class="text-xs uppercase opacity-70">
                        {{ currentChatUser.userType || '暂无用户类型' }}
                    </h2>
                    <p class="text-xs">上次登录</p>
                    <p class="text-sm">{{ formatDate(currentChatUser.loginDate,"MM-DD") || '' }}</p></div>
                <div class="py-2"><h2 class="text-xs uppercase opacity-70">统计</h2>
                    <p class="text-sm disabled">普通の排行</p></div>
            </div>
            <div class="flex items-center justify-between mt-4 p-2 bg-[#8a2be2] rounded">
                <span class="text-sm">添加备注</span>
                <q-input :autofocus="focus.notes" @blur="updateNoted" class="flex-grow ml-1.5 text-sm text-white" filled
                         debounce="500" label="添加备注"
                         dense v-model="currentChatUser.notes"></q-input>
            </div>
            <div class="flex items-center justify-between mt-4 p-2 bg-[#8a2be2] rounded">
                <span class="text-sm">共同好友</span>
                <q-tooltip>暂未实现</q-tooltip>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="text-white">
                    <path d="m9 18 6-6-6-6"></path>
                </svg>
            </div>
            <div class="flex items-center justify-between mt-4 p-2 bg-[#8a2be2] rounded" >
                <span class="text-sm">共同应用</span>
                <q-tooltip>暂未实现</q-tooltip>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="text-white">
                    <path d="m9 18 6-6-6-6"></path>
                </svg>
            </div>
            <div class="flex items-center justify-between mt-4 p-2 bg-[#8a2be2] rounded">
                <span class="text-sm">可能认识</span>
                <q-tooltip>暂未实现</q-tooltip>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="text-white">
                    <path d="m9 18 6-6-6-6"></path>
                </svg>
            </div>
        </div>
    </div>
    <div v-else>
        <q-toolbar>
            <span class="text-white text-2xl">当前活动</span>
        </q-toolbar>
        <div class="h-full flex items-center justify-center">
            <div class="mt-10 text-h6 text-gray-300">
                现在很安静.....
            </div>
            <div class="mt-5 text-1xl text-gray-300 q-pa-md">
                当好友开始活动时（比如玩游戏或进行语音聊天的时
                候)，他们的状态都会显示在这里！
            </div>
        </div>
    </div>

</template>
<script lang="ts" setup name="ShopCard">


import {editFriendNoted} from "@/api/im/friend";
import {bus} from "@/boot/eventbus";
import ImAvatar from "@/components/global/im-avatar.vue";
import {formatDate} from "@/utils/ruoyi";

const emits = defineEmits(['updated:friendList'])

const props = defineProps({
    currentChatUser: {
        type: Object,
        default: {},
        required: true
    },
    classes: {
        type: String,
        default: 'bg-[#6e00b3]',
        required: false
    },
    focus: {
        type: String,
        default: "",
        required: false
    }
})

// 更新备注
async function updateNoted() {
    const {code} = await editFriendNoted({friendId: props.currentChatUser.userId, notes: props.currentChatUser.notes})
    bus.emit("UPDATE_FRIEND_LIST")

}


</script>


<style scoped>

</style>
