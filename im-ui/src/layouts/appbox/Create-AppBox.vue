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
                <q-form @submit="addSubmit">
                    <q-card-section>
                        <div class="text-h6">创建应用</div>
                    </q-card-section>
                    <q-card-section class="q-pt-none">

                        <q-stepper class="global-dark" v-model="step" ref="stepper"
                                   animated>
                            <q-step :name="1" title="应用名称" icon="settings"
                                    :done="done1" class="text-teal">

                                <div>
                                    <q-icon name="*" color="red"></q-icon>
                                    分类:
                                </div>
                                <q-select dense bg-color="grey" label-color="red" v-model="appBoxForm.classesId"
                                          :options="classesOptions"
                                          map-options emit-value
                                          :rules="[(val)=>isNotEmpty(val) || val.length<1]">
                                </q-select>
                                <div>
                                    <q-icon name="*" color="red"></q-icon>
                                    应用名称:
                                </div>
                                <q-input bg-color="grey" v-model="appBoxForm.appName" filled
                                         label-color="red"
                                         :rules="[(val)=>isNotEmpty(val) || val.length<1]"
                                ></q-input>

                                <div>
                                    应用简介:
                                </div>
                                <q-input bg-color="grey" v-model="appBoxForm.remark" filled
                                         :rules="[(val)=>isNotEmpty(val) || val.length<1]"
                                ></q-input>

                                <q-stepper-navigation
                                    v-if="appBoxForm.appName && appBoxForm.remark && appBoxForm.classesId">
                                    <q-btn @click="() =>
                                     { done1 = true; step = 2 ;setup2()}" color="accent" label="下一步"/>
                                </q-stepper-navigation>
                            </q-step>

                            <q-step :name="2" title="配置人员" icon="create_new_folder" :done="done2" class="text-teal">
                                <div class="q-gutter-md">
                                    <q-select clearable bg-color="grey" label="配置ai" v-model="aiSelected"
                                              :options="aiOptions"
                                              map-options emit-value>
                                    </q-select>

                                    <q-select clearable bg-color="grey" label="添加人员" v-model="appBoxForm.userIds"
                                              :options="friendOptions"
                                              multiple map-options emit-value>
                                    </q-select>
                                    <q-option-group
                                        v-model="channelType"
                                        :options="[{ label: '默认频道', value: 0 },
                                        // { label: '自定义频道', value: 1 }
                                        ]"
                                        color="primary"
                                        inline
                                    />
                                </div>

                                <div v-show="channelType">
                                    <q-card dark>
                                        <q-card-section class="q-gutter-md mt-2">
                                            <q-btn :disable="channelList.length >=10" color="primary" label="添加"
                                                   @click="channelList.push({});"></q-btn>
                                            <q-btn color="warning" label="清空" @click="channelList=[]"></q-btn>
                                        </q-card-section>
                                        <q-card-section>
                                            <div class="flex flex-wrap justify-start items-center q-gutter-md">
                                                <div v-for="(item,index) in channelList">
                                                    <q-input bg-color="grey" v-model="channelList[index].appName" filled
                                                             label="频道名称" clearable
                                                             :rules="[
                                                             (val)=>isNotEmpty(val),
                                                         (val)=>!!val.length<2|| '频道名称不能小于2个字符' ,
                                                         channelList.length < 3 || '频道不能少于3个',
                                                          channelList>10 || '频道不能超过10个',
                                                         ]">
                                                        <template v-slot:append>
                                                            <q-btn flat color="break" label="删除"
                                                                   @click="channelList.splice(index,1)"></q-btn>
                                                        </template>
                                                    </q-input>
                                                </div>
                                            </div>
                                        </q-card-section>

                                    </q-card>

                                </div>

                                <q-stepper-navigation>
                                    <q-btn v-show="isChannelType" @click="() => { done2 = true; step = 3 }"
                                           color="accent" label="下一步">
                                    </q-btn>

                                    <q-btn @click="step = 1;setup2()" color="primary" label="上一步"
                                           class="q-ml-sm"/>
                                </q-stepper-navigation>
                            </q-step>

                            <q-step
                                :name="3"
                                title="封面和icon"
                                icon="add_comment"
                                :done="done3">
                                <div class="q-gutter-md">
                                    <div>
                                        <q-icon name="*" color="red"></q-icon>
                                        选择封面:
                                    </div>
                                    <ImageUpload :imgWidth="250" :img-height="250" :label="'选择封面'"
                                                 v-model="appBoxForm.cover">
                                        <template #preview>
                                            <img v-if="appBoxForm.cover" class="aspect-auto rounded-sm  "
                                                 :src="appBoxForm.cover" width="250" height="250">
                                            <img v-else class="aspect-auto rounded-sm  "
                                                 src="@/assets/def_0.png" width="250" height="250">
                                        </template>
                                    </ImageUpload>
                                    <br/>
                                    <div>
                                        <q-icon name="*" color="red"></q-icon>
                                        选择图标
                                        <q-icon name="info" @click="copyToClipboard('https://www.iconfont.cn/')">
                                            <q-tooltip>
                                                仅支持SVG宽高48样式图标,请到https://www.iconfont.cn/复制48格式的svg,点击复制该网站
                                            </q-tooltip>
                                        </q-icon>
                                        :
                                    </div>
                                    <div style="max-width: 250px;height: 205px;">
                                        <q-input dark label="应用图标" v-model="appBoxForm.iconUrl" filled
                                                 clearable>

                                        </q-input>
                                        <div v-html="appBoxForm.iconUrl">

                                        </div>
                                    </div>
                                </div>
                                <q-stepper-navigation>
                                    <q-btn flat @click="step = 2" color="primary" label="上一步" class="q-ml-sm"/>
                                </q-stepper-navigation>
                            </q-step>

                        </q-stepper>

                    </q-card-section>

                    <q-card-actions align="right" class=" text-teal global-dark">
                        <q-btn label="取消" color="primary" v-close-popup/>
                        <q-btn color="secondary" type="submit" label="创建"/>
                    </q-card-actions>
                </q-form>
            </q-card-section>
        </q-card>
    </q-dialog>
</template>

<script setup lang="ts">


import {isNotEmpty} from "@/utils/verify";
import {AppboxApplicationFrom} from "@/api/im/appBox/type";
import {listClasses} from "@/api/im/classes";
import {listFriend} from "@/api/im/friend";
import {getAiUserList} from "@/api/user";
import ImageUpload from "@/components/upload/ImageUpload.vue";
import {appBoxUsers} from "@/api/im/appBox";
import {Notify} from "quasar";

const dialogref = ref(false)
const maximizedToggle = ref(true)


const step = ref(1)
const done1 = ref(false)
const done2 = ref(false)
const done3 = ref(false)
const classesOptions = ref([])
const friendOptions = ref([])
const aiOptions = ref([])
const aiSelected = ref()
const channelList = ref<AppboxApplicationFrom[]>([])
const channelType = ref(0)
const $q = useQuasar()
let fileReader = new FileReader();
let appBoxForm = reactive<AppboxApplicationFrom>({
    popularity: 0,
    pv: 0,
    uv: 0,
    channelStatus: 1,
    parentId: 0,
    cover: '',
    userIds: [],
})

// 判断是否默认频道
const isChannelType = computed(() => {
    if (channelType.value === 0) {
        return true
    } else {
        let bool = channelType.value === 1 && channelList.value.length >= 3
        return bool;
    }
})


// 判断是否可创建
const isCreate = computed(() => {
    if (appBoxForm.classesId && appBoxForm.appName && appBoxForm.iconUrl && appBoxForm.cover) {
        return true
    } else {
        return false
    }

})

// [1] 获取分类列表
async function getClassesList() {
    let param = {
        pageNum: 1,
        pageSize: 100,
        status: 1,
        classesName: undefined,
    }
    let {code, data, msg} = await listClasses(param)
    if (code === 200) {
        classesOptions.value = data.map(item => {
            return {
                label: item.classesName,
                value: item.id
            }
        })
    }
}

// [2] 获取用户好友列表
async function getUserList() {
    // 获取好友列表
    let {code, rows} = await listFriend({pageNum: 1, pageSize: 100, relationStatus: 2})
    if (code === 200) {
        friendOptions.value = rows.map(item => {
            return {
                label: item.nickName,
                value: item.userId
            }
        })
    }

}

// [3] 获取ai列表
async function getAiList() {
    // 获取ai用户列表
    let {code, rows} = await getAiUserList()
    if (code === 200) {
        aiOptions.value = rows.map(item => {
            return {
                label: item.nickName,
                value: item.userId
            }
        })
    }
}

/**
 * 复制内容
 * @param data
 */
function copyToClipboard(data: string) {
    // 复制到剪切板
    navigator.clipboard.writeText(data);
}

/**
 * 重置表单
 */
function reset() {
    aiSelected.value = undefined
    channelList.value = []
    appBoxForm = reactive<AppboxApplicationFrom>({
        popularity: 0,
        pv: 0,
        uv: 0,
        channelStatus: 1,
        parentId: 0,
        cover: '',
        userIds: [],
    })
}

/**
 * 下一步要求
 */
function setup2() {

}

/**
 * 提交表单
 */
async function addSubmit() {
    // 合并交集 appBoxForm.userids 和 aiSelected
    let userIds = []
    if(appBoxForm.userIds){appBoxForm.userIds.forEach(item => userIds.push(item))}
    if (aiSelected.value) userIds.push(aiSelected.value)
    appBoxForm.userIds = userIds
    let {code, data, msg} = await appBoxUsers(appBoxForm)
    if (code === 200) {
        Notify.create({
            message: "添加应用成功",
            color: 'green',
            position: 'top',
            timeout: 2000,
        })
        reset()
        hide()
        window.location.reload()
    } else {
        Notify.create({
            message: "添加应用失败",
            color: 'red',
            position: 'top',
            timeout: 2000,
        })
    }

}

async function show() {
    await getData()
    dialogref.value = true
}

function hide() {
    dialogref.value = false
}

/**
 * 初始化
 */
async function getData() {
    await getClassesList()
    await getUserList()
    await getAiList()
}

// 暴露给父组件使用
defineExpose({
    show,
    hide,
})
</script>
<style scoped lang="scss">

</style>
