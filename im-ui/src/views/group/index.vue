<template>
    <q-toolbar>
        <q-input rounded outlined dense class="WAL__field full-width"
                 bg-color="white" v-model="groupChatQueryFrom.groupName"
                 @keydown.enter="fatchGroupChatList"
                 placeholder="搜索群名称,开始新对话">
            <template v-slot:append>
                <q-btn flat dense icon="search"
                       @click="fatchGroupChatList"/>
            </template>
        </q-input>
    </q-toolbar>
    <q-list>
        <!--         @click="fatchGroupChatUserList(group)" -->
        <q-item clickable v-ripple v-for="(group,index) in groupStore.groupList" :key="group.id"
                v-de:click="{func:()=>{fatchGroupChatUserList(group)}, delay:1500}"
                @click.right="(e)=>{e.preventDefault()}">
            <PopupMenu>
                <template #listItem>
                    <q-list class="global-dark">
                        <q-item class="">
                            <q-item-section>
                                <q-btn flat dense class="" label="退出此群" @click="quitGroup(group)"></q-btn>
                            </q-item-section>
                        </q-item>
                        <q-item class="" v-if="currenUser.userId === group.groupLeaderId">
                            <q-item-section>

                                <q-btn flat dense label="解散" @click="dissolutionGroup(group)">
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
                            </q-item-section>
                        </q-item>
                    </q-list>
                </template>
            </PopupMenu>
            <q-item-section avatar>
                <im-avatar :src="group.avatar" :unread="group.unread" is-group></im-avatar>
            </q-item-section>
            <q-item-section>
                <q-item-label lines="1">
                    <div class="ellipsis">{{ group.groupName }}</div>
                </q-item-label>
                <span class="text-gray-400 ellipsis" style="font-size: 10px">
                    {{ group.recentMessage || "" }}
                </span>
            </q-item-section>
            <q-space/>
            <q-separator color="red"/>
        </q-item>
    </q-list>
</template>
<script lang="ts" setup>

import {useGroupStore} from "@/stores/modules/group";
import {getGroupChatList, getUserList} from "@/api/im/chatGroup";
import {useFriendStore} from "@/stores/modules/friend";
import {ChatGroupData} from "@/api/im/chatGroup/type";
import {useAppBoxs} from "@/stores/modules/appBox";
import {listMsgRecord} from "@/api/im/msgRecord";
import {bus} from "@/boot/eventbus";
import PopupMenu from "@/views/group/view/PopupMenu.vue";
import useUserStore from "@/stores/modules/user";
import ImAvatar from "@/components/global/im-avatar.vue";

const instance = getCurrentInstance()
const currenUser = ref({})

const {
    groupChatQueryFrom,
    fatchGroupChatList, quitGroupChat, dissolutionGroupChat
} = useGroupStore()
const groupStore = useGroupStore()


// 退出群聊
function quitGroup(group: ChatGroupData) {
    instance?.proxy?.$log_confirm("退出群聊?", "", async () => {
        await quitGroupChat(group.id, null)
    })
}

// 解散群聊
function dissolutionGroup(group: ChatGroupData) {
    instance?.proxy?.$log_confirm("解散群聊?", "", async () => {
        await dissolutionGroupChat(group.id, null)
    })
}


// 获取群聊用户列表
const fatchGroupChatUserList = async (group: ChatGroupData) => {

    // 更新头部信息
    bus.emit("TOP_HEAD_DATA", group)

    // 设置当前聊天类型
    useAppBoxs().chatType = 2
    // 清除当前聊天好友信息
    useFriendStore().currFriendUser = null
    // 设置当前聊天类型
    groupStore.currGroupInfo = group
    // 得到当前群聊信息
    groupStore.groupChatQueryFrom.id = group.id
    // 得到当前群聊用户列表
    let res = await getUserList(groupStore.groupChatQueryFrom);
    if (res.code === 200) {
        groupStore.currGroupUserList = res.rows
        // 发射消息
        bus.emit('ACTIVE_USER_LIST', res.rows)
    }

    res = await listMsgRecord({toGroupId: group.id, toType: 2});
    if (res.code === 200) {
        bus.emit('UPDATE_FRIEND_MSG', res.rows)
    }

}

onMounted(() => {
    // await fatchGroupChatList();
    getGroupChatList(groupChatQueryFrom).then((res: any) => {
        if (res.code === 200) {
            groupStore.groupList = res.rows;
        }
    })
    currenUser.value = useUserStore().currUser

});

defineExpose({fatchGroupChatList});

</script>


<style scoped>

</style>
