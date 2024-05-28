<template>
    <q-list class="absolute-bottom bg-brown">
        <q-item class="q-pa-xs">
            <q-item-section side>
                <im-avatar :src="userStore.currUser?.avatar" :is-online="true"></im-avatar>
            </q-item-section>
            <q-item-section>
                <q-item-label>
                    {{ userStore.currUser?.nickName }}
                </q-item-label>
                <q-item-label caption>
                    {{ userStore.currUser?.userName }}
                </q-item-label>
            </q-item-section>
            <q-item-section side v-show="false">
                <q-bar class="rounded-borders">
                    <q-btn icon="keyboard_voice" dense flat color="white" v-if="audioStatus" @click="closeAudio">
                        <q-tooltip>关闭语音</q-tooltip>
                    </q-btn>
                    <q-btn icon="mic_off" dense flat color="red" v-else @click="openAudio">
                        <q-tooltip>开启语音</q-tooltip>
                    </q-btn>

                    <q-btn icon="headset" dense flat color="white" v-if="musicStatus" @click="closeMusic">
                        <q-tooltip>关闭声音</q-tooltip>
                    </q-btn>
                    <q-btn icon="headset_off" color="red" dense flat v-else @click="openMusic">
                        <q-tooltip>开启声音</q-tooltip>
                    </q-btn>
                    <q-btn flat dense icon="settings">
                        <q-tooltip>个人资料</q-tooltip>
                    </q-btn>

                </q-bar>

            </q-item-section>
        </q-item>

    </q-list>

</template>
<script lang="ts" setup>
import useUserStore from "@/stores/modules/user";
import ImAvatar from "@/components/global/im-avatar.vue";


const userStore = useUserStore()
const audioStatus = ref(false)
const musicStatus = ref(false)

const openAudio = () => {
    audioStatus.value = true
    userStore.audioStatus = true
}
const closeAudio = () => {
    audioStatus.value = false
    userStore.audioStatus =false
}

const openMusic = () => {
    musicStatus.value = true
    userStore.musicStatus = true

}
const closeMusic = () => {
    musicStatus.value = false
    userStore.musicStatus =false
}

</script>


<style scoped>

</style>
