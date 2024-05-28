<template>
    <div class="q-gutter-y-md global-dark" style="width: 600px">
        <q-card dark>
            <q-toolbar>
                <q-btn icon="mark_unread_chat_alt" flat label="收信箱"/>
                <q-space/>
                <!--                <q-btn :icon="switchIcon" flat label="全部标记已读" @click="clearMessage(tab)"/>-->
            </q-toolbar>
            <q-tabs
                v-model="tab"
                dense
                class=" global-dark"
                active-color="primary"
                indicator-color="purple"
                align="justify"
            >
                <q-tab name="为您精选" label="通知">
                    <q-badge v-show="choiceMsgCount" color="red" :label="choiceMsgCount" floating rounded
                             align="top"></q-badge>
                </q-tab>
                <!--                <q-tab name="未读消息" label="其他消息"/>-->
                <q-tab name="提及" label="提及"/>
            </q-tabs>
            <q-tab-panels v-model="tab" animated class=" global-dark">
                <q-tab-panel name="为您精选">
                    <Choice ref="choiceRef"></Choice>
                </q-tab-panel>

                <!--                <q-tab-panel name="未读消息">-->
                <!--                    <Message></Message>-->
                <!--                </q-tab-panel>-->

                <q-tab-panel name="提及">
                    <Message></Message>
                </q-tab-panel>
            </q-tab-panels>
        </q-card>

    </div>
</template>

<script setup>
import Mention from "./view/Mention.vue"
import Message from "./view/Message.vue"

const tab = ref('为您精选')
const choiceRef = ref(null)

const switchIcon = computed(() => {
    if (tab.value === '未读消息') {
        return 'mark_chat_read'
    } else if (tab.value === '提及') {
        return 'alternate_email'
    } else {
        return 'conveyor_belt'
    }
})

const choiceMsgCount = computed(() => {
    if (choiceRef.value) {
        return choiceRef.value.choiceMsgCount
    } else return 0
})

// 标记已读所有
function clearMessage(tab) {
    if (tab === '未读消息') {
        // updateNoticeUnRead(0)
    } else if (tab === '提及') {
        // updateNoticeUnRead(1)
    } else {
        if (choiceRef.value) {
            choiceRef.value.readAll()
        }
    }
}

</script>
