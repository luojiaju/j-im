<template>
    <div class="global-dark w-1/3">
        <div class="tilt-in-fwd-tr rounded-lg border bg-card text-card-foreground shadow-sm w-full  "
             data-v0-t="card">
            <div class="p-6">
                <q-img
                    :src="currAppBox?.coverUrl"
                    spinner-color="white"
                    :ratio="16/9" fit="cover" class="rounded-borders"
                    height="65px"></q-img>
                <div class="p-4">
                    <div class="flex q-gutter-md ">
                        <div v-html="currAppBox.iconUrl"></div>
                        <div class="">
                            <h3 class="text-white text-lg mb-2">{{ currAppBox.appName || '1' }}</h3>
                            <p class="text-gray-400 text-sm mb-4 ellipsis-3-lines">
                                {{ currAppBox.remark || '暂无描述' }}</p>
                        </div>
                    </div>
                    <div class="flex justify-between text-gray-300 text-sm">
                        <span><q-badge color="green" rounded></q-badge> {{ currAppBox.pv || '1' }} </span>
                        <span><q-icon name="local_fire_department"
                                      color="red"></q-icon>{{ currAppBox.popularity || '1' }}</span>
                    </div>
                </div>
                <button :disabled="isJoin" @click="handelJoinAppBox"
                        class="hover:bg-green-700 w-full  bg-green-600 rounded-2xl text-white px-4 py-2">
                    {{ isJoin ? '已加入' : '加入' }}
                </button>
            </div>
        </div>
    </div>
</template>
<script lang="ts" setup>
import {AppboxApplicationVo} from "@/api/im/appBox/type";
import {useAppBoxs} from "@/stores/modules/appBox";
import {addAppBox} from "@/api/im/appBox";

const props = defineProps({
    json: {
        type: String,
        default: ""
    }
})
const instance = getCurrentInstance()

//store
const appBoxsStore = useAppBoxs()

// ref
const currAppBox = ref<AppboxApplicationVo>({})

// computed
// 是否已加入
const isJoin = computed(() => {
    // 确保props.appBox.id是存在的
    if (currAppBox.value) {
        // 使用find方法查找当前appBox是否在appBoxsStore的列表中
        return appBoxsStore.currAppBoxList.find(app => app.id === currAppBox.value.id) !== undefined;
    }
    return false;
})

// func
// 加入应用
function handelJoinAppBox() {
    instance?.proxy?.$log_confirm("加入应用", "确定加入此应用吗?", () => {
        let appId = currAppBox.value.id
        // 添加应用
        addAppBox(appId).then((res: any) => {
            if (res.code === 200) {
                // 重新载入页面
                appBoxsStore.currAppBoxList.push(currAppBox.value)
                // window.location.reload()
            }
        })

    })
}


onMounted(() => {
    currAppBox.value = JSON.parse(props.json)
})

</script>


<style scoped>

</style>
