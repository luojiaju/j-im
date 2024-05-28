<template>
    <q-dialog
        v-model="dialogref"
        persistent
        :maximized="maximizedToggle"
        transition-show="slide-up"
        transition-hide="slide-down"
    >
        <q-card class="global-dark text-white">
            <q-bar>
                <q-space/>

                <q-btn dense flat icon="minimize" @click="maximizedToggle = false" :disable="!maximizedToggle">
                    <q-tooltip v-if="maximizedToggle" class="bg-white text-primary">Minimize</q-tooltip>
                </q-btn>
                <q-btn dense flat icon="crop_square" @click="maximizedToggle = true" :disable="maximizedToggle">
                    <q-tooltip v-if="!maximizedToggle" class="bg-white text-primary">Maximize</q-tooltip>
                </q-btn>
                <q-btn dense flat icon="close" v-close-popup>
                    <q-tooltip class="bg-white text-primary">Close</q-tooltip>
                </q-btn>
            </q-bar>

            <q-card-section>

                <div class="q-pa-md">
                    <div>
                        <q-toolbar-title>搜索用户,在这里可以搜索的本站的所有用户</q-toolbar-title>
                    </div>
                    <div>
                        <q-input v-model="friendQueryFrom.userName"
                                 @keydown.enter="handelSearch"
                                 bg-color="white" filled clearable label="你可以通过用户名添加好友">
                            <template v-slot:append>
                                <q-btn color="white" text-color="black" @click="handelSearch" label="搜索"/>
                            </template>
                        </q-input>

                        <q-separator/>
                    </div>

                    <q-table grid :rows="userList" :columns="columns" row-key="userId" hide-header :loading="true"
                             v-model:pagination="pagination"
                    >
                        <template #item="props">
                            <q-card class="my-card q-ma-xs global-dark border border-fuchsia-800">
                                <q-img v-if="props.row.avatar" :src="props.row.avatar">
                                </q-img>
                                <q-img v-else src="@/assets/def_0.png">
                                </q-img>
                                <q-card-section class="bg-gray-800">
                                    <q-btn fab color="green" icon="add" class="absolute"
                                           @click="handelAddFriend(props.row.userId)"
                                           style="top: 0; right: 12px; transform: translateY(-50%);">
                                        <q-tooltip>发送好友申请</q-tooltip>
                                    </q-btn>
                                    <div class="row no-wrap items-center">
                                        <div class="col text-h6  ellipsis text-white">
                                            {{ props.row.userName }}
                                            <q-icon v-if="props.row.sex === '0'" name="man" color="blue"></q-icon>
                                            <q-icon v-else-if="props.row.sex === '1'" name="woman"
                                                    color="pink"></q-icon>
                                            <q-icon v-else name="?" color="grey"></q-icon>
                                        </div>
                                    </div>
                                </q-card-section>
                                <q-card-section class="q-pt-none">
                                    <div class="text-caption text-white ">
                                        {{ props.row.userType }}
                                        <q-icon v-if="props.row.online === null" name="online_prediction"
                                                color="red">
                                            <q-tooltip>离线</q-tooltip>
                                        </q-icon>
                                        <q-icon v-else name="online_prediction" color="green">
                                            <q-tooltip>在线</q-tooltip>
                                        </q-icon>
                                    </div>
                                </q-card-section>
                                <q-separator/>
                            </q-card>
                        </template>
                        <template #pagination>
                            <q-pagination class="bg-white"
                                          @update:modelValue="handelPagin"
                                          v-model="pagination.page"
                                          color="purple"
                                          :max="(pagination.rowsNumber / pagination.rowsPerPage)+1"
                                          :max-pages="10"
                                          boundary-numbers
                            />
                        </template>
                    </q-table>
                </div>
            </q-card-section>
        </q-card>
    </q-dialog>
</template>

<script setup lang="ts">

import {FriendForm, FriendQuery} from "@/api/im/friend/types";
import {addFriend, searchFriend} from "@/api/im/friend";
import {useFriendStore} from "@/stores/modules/friend";
import useUserStore from "@/stores/modules/user";

const $q = useQuasar()
const friendQueryFrom = ref<FriendQuery>({pageNum: 0, pageSize: 10})
const {friendList, setFriendList} = useFriendStore()
const userStore = useUserStore()

const columns = [
    {name: 'userName', required: true, field: "userName", align: 'left'},
    {name: 'userId', align: 'center', field: 'userId', sortable: true},
    {name: 'sex', field: 'sex', sortable: true},
    {name: 'userType', label: 'userType', field: 'userType'}
]

const dialogref = ref(false)
const maximizedToggle = ref(true)

const userList = ref<any>()
const pagination = ref({
    rowsPerPage: 14,
    sortBy: 'createTime',
    descending: true,
    page: 1,
    rowsNumber: 10,

})


function show() {
    dialogref.value = true
}

function hide() {
    dialogref.value = false
}

function handelPagin(val){
    console.log(val,pagination.value)
    handelSearch()

}

// 搜索好友
async function handelSearch() {
    console.log(pagination.value)
    friendQueryFrom.value.nickName = friendQueryFrom.value.userName
    friendQueryFrom.value.pageNum = pagination.value.page - 1
    friendQueryFrom.value.pageSize = 16
    let {code, rows, total} = await searchFriend(friendQueryFrom.value)
    if (code === 200) {
        userList.value = rows
        pagination.value.rowsNumber = total
    }
}

// 发送好友申请
async function handelAddFriend(friendId: string | number) {
    if (userStore.currUser.userId === friendId) {
        $q.notify({
            position: "top",
            message: '不能添加自己为好友!!!',
            color: 'negative'
        })
        return
    }
    let param: FriendForm = {
        attachMsg: "",
        noted: "",
        nickName: userStore.currUser.nickName,
        userId: userStore.currUser.userId,
        friendId: friendId,
        relationStatus: 1
    }
    let res = await addFriend(param)
    if (res.code === 200) {
        $q.notify({
            position: "top",
            message: '已发送,好友申请!',
            color: 'positive'
        })
    } else {
        $q.notify({
            position: "top",
            message: res.msg,
            color: 'warning'
        })
    }
}

// 暴露给父组件使用
defineExpose({
    show,
    hide,
    handelSearch,
    handelAddFriend,
    dialogref,
    maximizedToggle
})
</script>
<style scoped lang="scss">

.my-card {
    width: 100%;
    max-width: 250px

}

</style>
