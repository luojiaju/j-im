<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <p>
          <br />
        </p>
        <h2>社交媒体即时通讯后台管理系统</h2>
        <p><b>当前版本:</b> <span>v5.1.2</span></p>
        <p>
          <!--          <el-tag type="danger">&yen;免费开源</el-tag>-->
        </p>
        <!--        <p>-->
        <!--          <el-button type="primary" icon="Cloudy" plain @click="goTarget('https://gitee.com/dromara/RuoYi-Vue-Plus')">访问码云</el-button>-->
        <!--          <el-button type="primary" icon="Cloudy" plain @click="goTarget('https://github.com/dromara/RuoYi-Vue-Plus')">访问GitHub</el-button>-->
        <!--        </p>-->
      </el-col>
    </el-row>
    <el-divider />
  </div>
</template>

<script setup name="Index" lang="ts">
import { initWebSocket } from '@/utils/websocket';

onMounted(() => {
  let protocol = window.location.protocol === 'https:' ? 'wss://' : 'ws://'
  initWebSocket(protocol + window.location.host + import.meta.env.VITE_APP_BASE_API + "/resource/websocket");
});

const goTarget = (url:string) => {
  window.open(url, '__blank')
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
