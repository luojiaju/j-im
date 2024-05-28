<!--
  @FileDescription:  ,
  @Date: 2024/4/26 下午4:19 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div class="global-dark">
        <q-separator/>
        <q-toolbar-title>头像</q-toolbar-title>
        <im-avatar  src="http://127.0.0.1:9100/jilijili-project/2024/04/25/633e087c651e46f892fe4dc5f9e29175.png" is-manage></im-avatar>
        <q-separator/>
        <q-toolbar-title>评论卡片</q-toolbar-title>
        <div class="bg-white p-4 rounded-lg shadow-md">
            <div class="flex justify-between items-center">
                <h1 class="text-xl font-bold text-gray-800">ThlioteE</h1>
                <p class="text-sm text-gray-500">今天07:54</p>
            </div>
            <p class="mt-2 text-gray-700">This one is correct. If the layers don't fit into your GPU's VRAM, it will
                fall back to CPU. I would suggest to set a lower number of layers in the settings.</p>
            <p class="text-right text-xs text-gray-400">已编辑</p>
            <div class="flex justify-between items-center mt-4">
                <p class="text-sm text-gray-500">1</p>
                <button class="text-gray-400 hover:text-gray-600">
                </button>
            </div>
        </div>
    </div>

    <!--        群主-->

</template>
<script lang="ts" setup>

import ImAvatar from "@/components/global/im-avatar.vue";
</script>

<style scoped>
</style>
