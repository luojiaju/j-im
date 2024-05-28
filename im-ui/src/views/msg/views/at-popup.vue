<!--
  @FileDescription:  ,
  @Date: 2024/4/27 下午8:40 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div >
        <q-card dark>
            <q-card-section>
                <div class="flex-grow-0 cursor-pointer px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-800" @click="selectUser({ userId: '0', nickName: '全体成员'})">
                    <div class="flex items-center gap-3">
                            <span class="relative flex shrink-0 overflow-hidden rounded-full h-8 w-8">
                            <span class="flex h-full w-full items-center justify-center rounded-full bg-muted">All</span>
                            </span>
                        <div>
                            <div class="font-medium">全体成员</div>
                            <div class="text-gray-500 dark:text-gray-400">创建，管理员可选</div>
                        </div>
                    </div>
                </div>
                <div class="cursor-pointer px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-800" @click="selectUser({ userId: '1', nickName: '管理员'})">
                    <div class="flex items-center gap-3">
                            <span class="relative flex shrink-0 overflow-hidden rounded-full h-8 w-8">
                            <span class="flex h-full w-full items-center justify-center rounded-full bg-muted">MG</span>
                            </span>
                        <div>
                            <div class="font-medium">管理员</div>
                            <div class="text-gray-500 dark:text-gray-400">创建者可选</div>
                        </div>
                    </div>
                </div>
            </q-card-section>
            <q-separator color="blue" size="5px"></q-separator>

<!--            -->

            <q-card-section>
                <div
                    class="max-h-60 w-full overflow-auto rounded-md    py-1 text-sm shadow-lg dark:border-gray-700  ">
                    <div v-for="(item,index) in appBoxUserList" :key="item.userId">
                        <div v-if="item.deptId == '1753687739292135425'"   @click="selectUser({ userId: '3', nickName: 'AI'})"
                             class="cursor-pointer px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-800">
                            <div class="flex items-center gap-3">
                                <im-avatar :src="item.avatar" :is-online="item.online"></im-avatar>
                                <div>
                                    <div class="font-medium">{{ item.nickName }}</div>
                                    <div class="text-gray-500 dark:text-gray-400">{{ item.userName }}</div>
                                </div>
                            </div>
                            <q-separator color="white" size="1px"></q-separator>
                        </div>
                        <div v-else  @click="selectUser(item)"
                             class="cursor-pointer px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-800">
                            <div class="flex items-center gap-3">
                                <im-avatar :src="item.avatar" :is-online="item.online"></im-avatar>
                                <div>
                                    <div class="font-medium">{{ item.nickName }}</div>
                                    <div class="text-gray-500 dark:text-gray-400">{{ item.userName }}</div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </q-card-section>
            <q-separator color="blue" size="5px"></q-separator>
        </q-card>
    </div>

</template>
<script lang="ts" setup>
import {useAppBoxs} from "@/stores/modules/appBox";
import ImAvatar from "@/components/global/im-avatar.vue";

const emit = defineEmits(["closePopup"])

const appBox = useAppBoxs()

const appBoxUserList = ref([])

// 获取应用成员列表
const getAppMemberList = () => {
    console.log(appBox.currAppBoxUserList)
}

// 搜索用户
const searchUser = () => {

    console.log(appBox.currAppBoxUserList)
}

// 选择的用户
const selectUser = (user: any) => {
    emit("closePopup", user)
}

// 获取当前应用ai列表
onMounted(() => {
    appBoxUserList.value = appBox.currAppBoxUserList
    console.log(appBoxUserList.value)
})


</script>


<style scoped>

</style>
