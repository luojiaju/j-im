<!--
  @FileDescription:  ,
  @Date: 2024/4/19 下午12:58 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div class="pl-9 " v-for="(record,index) in records" :key="record.id">
        <div class="hover:bg-black border-b-fuchsia-800 border-l-fuchsia-800"
             :class="(index == records.length-1)?'border-b-2 border-l-2':'border-l-2'">
            <div class="flex items-center space-x-2 pl-10">
                <span class=" flex h-10 w-10 shrink-0 overflow-hidden rounded-full">
                 <span
                     class=" flex h-10 w-10 shrink-0 overflow-hidden rounded-full ring-2 ring-offset-2 ring-offset-[#6e00b3] ring-white">
                     <img class="aspect-square h-full w-full" :src="record.author?.avatar">
                 </span>
                    <span
                        class="flex h-full w-full items-center justify-center rounded-full bg-muted">RS</span>
                </span>
                <div><p class="font-bold">{{ record.author?.nickName }}</p>
                    <p class="text-xs text-[#b0b3b8]">{{ record.createTime }}</p>
                </div>
            </div>
            <!--                内容处理 -->
            <div class="pl-9  discord-font q-ma-md">
                <!-- 文件 -->
                <div v-if=" record.contents?.files" class="q-gutter-md  row items-start">
                    <q-img v-for="(item,index) in record.contents?.files" :key="index"
                           spinner-color="white"
                           :ratio="4/3" fit="contain"
                           class="rounded-borders hover:scale-110 transition-transform  w-2/12 max-w-6/12 max-h-6/12"
                           :src="item.url"></q-img>
                </div>

                <!-- 音频 -->
                <div v-if="record.contents?.audio" class="q-pa-md">
                    <audio controls :src="record.contents?.audio"></audio>
                </div>

                <!--普通文本-->
                <div class="q-pa-md">
                    {{ record.contents?.text }}
                </div>
            </div>
            <q-separator/>
        </div>
    </div>
</template>
<script lang="ts" setup>
import {MsgRecordVO} from "@/api/im/msgRecord/types";

const props = defineProps({
    records: {
        type: Array<MsgRecordVO>,
        default: () => {
            return []
        }
    }
})
</script>


<style scoped>

</style>
