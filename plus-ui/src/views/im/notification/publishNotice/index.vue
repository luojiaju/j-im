<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
                :leave-active-class="proxy?.animate.searchAnimate.leave"></transition>
    <el-card shadow="never">
      <template #header>
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
          <el-tab-pane label="系统消息" name="appIndexPublish">
            <allPublish></allPublish>
          </el-tab-pane>
<!--          <el-tab-pane label="应用消息" name="allPublish">-->
<!--            <allPublish></allPublish>-->
<!--          </el-tab-pane>-->
<!--          <el-tab-pane label="用户消息" name="onePublish">-->
<!--            <onePublish></onePublish>-->
<!--          </el-tab-pane>-->
        </el-tabs>
      </template>
    </el-card>
  </div>
</template>

<script setup name="Notice" lang="ts">
import type { TabsPaneContext } from "element-plus";

import allPublish from "./allPublish/index.vue";
import onePublish from "./onePublish/index.vue";
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const activeName = ref("appIndexPublish");
const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event);
};
</script>

<style>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
