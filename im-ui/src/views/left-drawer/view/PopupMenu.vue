<!--
  @FileDescription:  右键弹出,
  @Date: 2024/4/18 下午7:01 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <q-popup-proxy context-menu  dense  transition-hide="flip-down">
        <slot name="listItem"></slot>
    </q-popup-proxy>
</template>
<script lang="ts" setup>
import {bus} from "@/boot/eventbus";

function hide(){

}

</script>


<style scoped>

</style>
