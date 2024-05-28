<!--
  @FileDescription:  ,
  @Date: 2024/4/18 下午8:23 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div>
        <q-dialog v-model="show">
            <ShopCard class="w-full" :current-chat-user="userDesc"></ShopCard>
        </q-dialog>
    </div>

</template>
<script lang="ts" setup>
import {SysUserVo} from "@/api/login/login.data";
import ShopCard from "@/components/card/ShopCard.vue";
import {getUserByUserId} from "@/api/user";

const show = ref(false)
const userDesc = ref<SysUserVo>({})

defineExpose({
    showDialog: async (userInfo: SysUserVo) => {
        if (userInfo) {
            const {code, data} = await getUserByUserId(userInfo.userId)
            if (code == 200) {
                userDesc.value = data.user
            }
        }
        show.value = true
    },
    closeDialog: () => {
        userDesc.value = {}
        show.value = false
    }
})

</script>


<style scoped>

</style>
