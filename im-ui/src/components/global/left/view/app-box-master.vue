<!--
  @FileDescription: 顶级app应用设置 ,
  @Date: 2024/4/25 下午10:37 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
<div>
    <q-form @submit="submit" v-model="appBoxForm">

    </q-form>
    <q-list>
        <q-item>
            <q-item-section>
                <q-item-label>
                    应用名称
                </q-item-label>
                <q-item-label caption>

                </q-item-label>
            </q-item-section>
        </q-item>
    </q-list>
</div>
</template>
<script lang="ts" setup>
const appBoxForm = ref()

</script>


<style scoped>

</style>
