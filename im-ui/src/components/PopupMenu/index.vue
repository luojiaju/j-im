<!--
  @FileDescription:  弹出菜单,
  @Date: 2024/3/17 10:48 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <q-popup-proxy :context-menu="contextMenu" class="global-dark">
        <slot name="cardPale" :data="prop.data"></slot>
        <q-list class="" dense style="min-width: 100px">
            <q-item clickable v-close-popup>
                <q-item-section @click="reply">@回复</q-item-section>
            </q-item>
            <q-item clickable v-close-popup v-show="false">
                <q-item-section>删除消息</q-item-section>
            </q-item>
            <q-separator/>
            <q-item clickable v-show="false">
                <q-item-section>添加反应</q-item-section>
                <q-item-section side>
                    <q-icon name="keyboard_arrow_right"/>
                </q-item-section>
                <q-menu anchor="top end" self="top start">
                    <q-list>
                        <!-- TODO 列出反应表情包 -->
                    </q-list>
                </q-menu>

            </q-item>
            <q-separator/>
            <q-item clickable v-close-popup>
                <q-item-section @click="copyBef">复制文本|消息连接</q-item-section>
            </q-item>
        </q-list>
    </q-popup-proxy>

</template>
<script lang="ts" setup>
import {copyToClipboard} from "@/utils/verify";
import {MsgRecordVO} from "@/api/im/msgRecord/types";
import {onMounted} from "vue";

const emits = defineEmits(['reply'])
const prop = defineProps({
    data: {
        type: Object as () => MsgRecordVO,
        required: false
    },
    contextMenu: {
        type: Boolean,
        required: false
    }
});

// 回复
function reply() {
    const param = {
        id: prop.data?.id, // 消息id
        refMsgId: prop.data?.id, // 回复消息id
        nickName: prop.data?.author.nickName, // 回复的用户昵称
    }
    //   contents: prop.data?.content // 回复的消息内容
    if (prop.data?.contents) {
        let content = prop.data?.contents
        if (content.video && content.video.length > 0) {
            content = "查看附件"
        }
        if (content.files && content.files.length > 0) {
            content = "查看附件"
        }
        if (content.audio && content.audio.length > 0) {
            content = "查看附件"
        }
        if (content.text && content.text.length > 0) {
            content = content.text
        }
        param.coment = content
    }
    emits('reply', param)

}


// 复制文本
function copyBef() {
    console.log(prop.data)
    if (prop.data?.contents.text) {
        copyToClipboard(prop.data?.contents.text)
    } else if (prop.data?.contents.audio) {
        copyToClipboard(prop.data?.contents.audio)
    } else if (prop.data?.contents.files) {
        let files = prop.data?.contents.files
        files = files.map((item: { url: any; }) => item.url + "\n")
        copyToClipboard(files)
    } else {
        copyToClipboard('')
    }
}


</script>


<style scoped>

</style>
