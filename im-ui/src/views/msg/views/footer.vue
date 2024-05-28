<!--
  @FileDescription:  ,
  @Date: 2024/3/26 8:51 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div class="text-white global-dark q-pa-md">
        <q-footer class="text-white global-dark q-pa-md">
            <q-form v-model="msgFrom" @submit="handleSubmit">
                <div class="full-width  q-pa-md" v-if="fileListBar">
                    <upload class="full-width" v-model="msgFrom.content.files"/>
                </div>
                <q-toolbar class="row">
                    <recordAudio v-model="msgFrom.content.audio" @handleSubmit="handleSubmit"/>
                    <q-btn round flat icon="perm_media" class="q-mr-sm" @click="fileListBar=!fileListBar">
                    </q-btn>
                    <q-input ref="inputRef" clearable standout filled bg-color="white" autofocus autogrow
                             placeholder="请输入内容,ctrl+enter发送"
                             maxlength="1200" class="col-grow text-white" type="textarea"
                             @keyup.ctrl.enter="handleSubmit" v-model.trim="msgFrom.content.text"/>
                    <q-btn round flat icon="send" type="submit"/>
                    <V3Emoji :recent="true"
                             @clickEmoji="handleClickEmoji"
                             @close="handleEmClose"
                             :fulldata="true"
                             style="width: 50px"
                             :size="'mid'"
                             class="row">
                        <q-btn round flat icon="insert_emoticon" class="q-mr-sm"/>
                    </V3Emoji>

                </q-toolbar>
            </q-form>
        </q-footer>

    </div>

</template>
<script lang="ts" setup>
import V3Emoji from "vue3-emoji";
const props = defineProps({
    msgFrom: {

    } ,
    handleSubmit: {
    },
    handleClickEmoji: {
    },
    handleEmClose: {
    },
    fileListBar: {
    }
})
</script>


<style scoped lang="scss">

</style>
