<!--
  @FileDescription:  ,
  @Date: 2024/4/11 15:49 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div>
        <q-pull-to-refresh @refresh="refresh" color="orange-2" bg-color="black" icon="autorenew">

            <q-scroll-area style="height: 50vmin">
                <div v-show="noticeVOList&&noticeVOList.length>0" class="ellipsis-3-lines ">
                    <div v-for="(item,index) in noticeVOList" :key="item.id" class="flex justify-start items-start">
                        <q-list class="w-full">
                            <q-item dense dark>
                                <q-item-section no-wrap>
                                    <div class="flex no-wrap justify-start items-center q-gutter-md ">
                                        <q-icon transition="flip-right " size="md"
                                                @click="toggleItem(item)"
                                                :name="isItemShown(item) ?'arrow_drop_down':'arrow_right'">
                                            <q-tooltip>折叠</q-tooltip>
                                        </q-icon>
                                        <img class="aspect-square w-10 " src="@/assets/def_0.png">
                                        <h2>{{ item.title || '' }}</h2>
                                    </div>
                                </q-item-section>

                            </q-item>
                            <q-item>
                                <q-item-label :lines="!isItemShown(item)?4:100">
                                    <div>
                                        <div v-if="!isItemShown(item)" class="ellipsis-3-lines " v-html="item.content">
                                        </div>
                                        <div v-else role="alert"
                                             class="mx-auto max-w-lg rounded-lg border-[#bd1e59] text-white border-stone   p-4 shadow-lg sm:p-6 lg:p-8">
                                            <div class="flex items-center gap-4">
                                            <span class="shrink-0 rounded-full bg-emerald-400 p-2 text-white">
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                                                 fill="currentColor" class="h-4 w-4">
                                                <path fill-rule="evenodd"
                                                      d="M18 3a1 1 0 00-1.447-.894L8.763 6H5a3 3 0 000 6h.28l1.771 5.316A1 1 0 008 18h1a1 1 0 001-1v-4.382l6.553 3.276A1 1 0 0018 15V3z"
                                                      clip-rule="evenodd"></path></svg>
                                            </span>
                                                <p class="font-medium sm:text-lg text-emerald-600">
                                                    {{ item.title || '' }}</p>
                                            </div>
                                            <p class="mt-4 text-gray-100 " v-html="item.content">
                                            </p>
                                            <div class="mt-6 sm:flex sm:gap-4">
                                                <q-btn @click="read(item.id)"

                                                       class="inline-block w-full rounded-lg bg-emerald-500 px-5 py-3 text-center text-sm font-semibold text-white sm:w-auto">
                                                    我已了然
                                                </q-btn>
                                            </div>
                                        </div>
                                    </div>
                                </q-item-label>
                            </q-item>
                        </q-list>

                    </div>
                </div>
                <div v-if="noticeVOList&&noticeVOList.length<=0">

                </div>
            </q-scroll-area>
            <q-inner-loading :showing="loading" dark>
                <div class="flex flex-row gap-2">
                    <div class="w-4 h-4 rounded-full bg-blue-700 animate-bounce [animation-delay:.7s]"></div>
                    <div class="w-4 h-4 rounded-full bg-blue-700 animate-bounce [animation-delay:.3s]"></div>
                    <div class="w-4 h-4 rounded-full bg-blue-700 animate-bounce [animation-delay:.7s]"></div>
                </div>
            </q-inner-loading>
        </q-pull-to-refresh>
    </div>
</template>
<script lang="ts" setup>

// 得到精选通知
import {listNotice, updateNoticeRead} from "@/api/im/notice";
import {NoticeQuery, NoticeVO} from "@/api/im/notice/types";
import {bus} from "@/boot/eventbus";



// 状态
const loading = ref(false)

// 显示更多
const moreShow = ref<NoticeVO[]>([])
// 精选消息
const noticeVOList = ref<NoticeVO[]>([])
// 查询条件
const queryForm = ref<NoticeQuery>({
    noticeType: 1,
    pageNum: 1,
    pageSize: 100
})

// 精选消息未读数量
const choiceMsgCount = computed(()=>{
    return noticeVOList.value.length
})



// 显示更多
function toggleItem(item: NoticeVO) {
    if (moreShow.value.includes(item)) {
        moreShow.value.splice(moreShow.value.indexOf(item), 1);
    } else {
        moreShow.value.push(item);
    }
}

// 判断是否显示更多
function isItemShown(item: NoticeVO) {
    return moreShow.value.includes(item);
}


// 已读
async function read(id: string) {
    let {code} = await updateNoticeRead({noticeId:id,type:1})
    if (code == 200) {
        // 移除
        noticeVOList.value.splice(noticeVOList.value.findIndex(item => item.id == id), 1)
        bus.emit("UNREAD_COUNT", -1)
    }
}

// 读取所有
async function readAll() {
    console.log("读取所有")
    const promises = noticeVOList.value.map(async item => {
        const {code} = await read(item.id);
        return code !== 200;
    });

    const results = await Promise.all(promises);

    noticeVOList.value = noticeVOList.value.filter((_, index) => results[index]);
}

// 下拉刷新
async function refresh(done: Function) {
    await getChoiceInfo()
    done()
}


// 获取精选通知
async function getChoiceInfo() {
    loading.value = true
    // 阻塞1.5秒]
    await new Promise(resolve => setTimeout(resolve, 500));

    let {code, rows} = await listNotice(queryForm.value)
    if (code == 200) {
        noticeVOList.value = rows
    }

    loading.value = false
}


onMounted(async () => {
    await getChoiceInfo();
    bus.on("CHOICE_MSG", (data: NoticeVO) => {
        console.log("接收到精选通知=>", data)
        if (data) {
            noticeVOList.value.push(data)
        }
        // 更新未读数量
        bus.emit("UNREAD_COUNT", 1)
    })
})

onUnmounted(() => {
    bus.off("CHOICE_MSG")
})

defineExpose({
    readAll,
    choiceMsgCount
})

</script>

<style scoped>

</style>
