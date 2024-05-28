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

                <q-form @submit="addGroupChatSubmit">
                    <q-card-section>
                        <div class="text-h6">新建群聊</div>
                    </q-card-section>
<!--                    居中-->
                    <div class="row justify-center">
                        <div class="w-1/2 ">
                            <q-card-section class="q-pt-none">

                                <q-stepper class="global-dark" v-model="step" ref="stepper"
                                           animated>
                                    <q-step :name="1" title="取个名字" icon="settings"
                                            :done="done1" class="text-teal">
                                        <div class="flex justify-center">
                                            <ImageUpload v-model="groupChatFrom.avatar" label="设置群聊头像" img-height="150" img-width="150"></ImageUpload>
                                        </div>
                                        <q-input bg-color="white" v-model="groupChatFrom.groupName" filled label="群聊名称"
                                                 :rules="[(val)=>isNotEmpty(val) || val.length<1]"
                                        ></q-input>
                                        <q-stepper-navigation v-if="groupChatFrom.groupName">
                                            <q-btn @click="() => { done1 = true; step = 2 ;setup2()}" color="primary"
                                                   label="下一步"/>
                                        </q-stepper-navigation>
                                    </q-step>

                                    <q-step :name="2"
                                            title="选择您的好朋友"
                                            icon="create_new_folder"
                                            :done="done2">
                                        <q-select filled bg-color="white" v-model="groupChatFrom.userIds" :options="options"
                                                  multiple use-input map-options emit-value label="邀请您的好友">
                                        </q-select>
                                        <q-radio v-model="groupChatFrom.role" val="1" label="管理员" />
                                        <q-radio v-model="groupChatFrom.role" val="2" label="成员" />
                                        <q-stepper-navigation>
                                            <q-btn v-if="groupChatFrom.userIds.length>0"
                                                   @click="() => { done2 = true; step = 3 }" color="primary"
                                                   label="下一步"/>
                                            <q-btn flat @click="step = 1;setup2()" color="primary" label="上一步"
                                                   class="q-ml-sm"/>
                                        </q-stepper-navigation>
                                    </q-step>

                                    <q-step
                                        :name="3"
                                        title="公告"
                                        icon="add_comment"
                                        :done="done3"
                                    >
                                        <q-editor v-model="groupChatFrom.remark" dark dense >

                                        </q-editor>

                                        <q-stepper-navigation>
                                            <q-btn color="primary" @click="done3 = true" label="确定"/>
                                            <q-btn flat @click="step = 2" color="primary" label="上一步" class="q-ml-sm"/>
                                        </q-stepper-navigation>
                                    </q-step>
                                </q-stepper>

                            </q-card-section>
                        </div>
                    </div>
                    <q-card-actions align="right" class=" text-teal global-dark">
                        <q-btn  flat label="取消" v-close-popup/>
                        <q-btn type="submit" flat label="创建" v-if="done3"/>
                    </q-card-actions>
                </q-form>

            </q-card-section>

        </q-card>

    </q-dialog>
</template>

<script setup lang="ts">


import {ChatGroupForm} from "@/api/im/chatGroup/type";
import {addChatGroup} from "@/api/im/chatGroup";
import {isNotEmpty} from "@/utils/verify";
import {useGroupStore} from "@/stores/modules/group";
import {listFriend} from "@/api/im/friend";
import ImageUpload from "@/components/upload/ImageUpload.vue";

const dialogref = ref(false)
const maximizedToggle = ref(true)


const {setCurrGroupInfo, fatchGroupChatList} = useGroupStore()

const step = ref(1)
const done1 = ref(false)
const done2 = ref(false)
const done3 = ref(false)
const friendList = ref([]);
const options = ref([])
const $q = useQuasar()

let groupChatFrom:{} = reactive<ChatGroupForm>({
    groupName: '',
    userIds: [],
    remark: '',
    role: '2',
    remark:'群主未设置',
    avatar:'',
})

function reset() {
    done1.value = false
    done2.value = false
    done3.value = false
    step.value = 1
    groupChatFrom = {}
}


function setup2() {
    listFriend({relationStatus: 2, pageNum: 1, pageSize: 100}).then((res: any) => {
        if (res.code === 200) {
            friendList.value = res.rows

            options.value = res.rows.map((item: any) => {
                return {
                    label: item.nickName,
                    value: item.userId
                }
            })
        }
    })
}

async function addGroupChatSubmit() {
    let res = await addChatGroup(groupChatFrom)
    if (res.code === 200) {
        reset()
        groupChatFrom = {}
        setCurrGroupInfo(res.data)
        await fatchGroupChatList()
        dialogref.value = false
        $q.notify({
            position: 'top',
            message: '创建成功',
            color: 'positive'
        })
    } else {
        $q.notify({
            position: 'top',
            message: res.msg,
            color: 'negative'
        })
    }
}

function show() {
    dialogref.value = true
}

function hide() {
    dialogref.value = false
}


// 暴露给父组件使用
defineExpose({
    show,
    hide,

})
</script>
<style scoped lang="scss">

</style>
