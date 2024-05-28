<!--
  @FileDescription:  通讯组件,
  @Date: 2024/3/21 20:58 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <div class="fixed-top bg-black w-full h-1/2 z-10" id="communicateId" v-show="show1">
        <header class=" bg-red-400  flex justify-between items-center p-4 text-white">
            <div class="flex items-center space-x-4"><span
                class="relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full"><span
                class="flex h-full w-full items-center justify-center rounded-full bg-muted">AM</span></span><span
                class="font-medium">Amani</span></div>
            <!--            <div class="flex items-center space-x-4">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                 class="text-gray-400">
                                <circle cx="11" cy="11" r="8"></circle>
                                <path d="m21 21-4.3-4.3"></path>
                            </svg>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                 class="text-gray-400">
                                <circle cx="8" cy="21" r="1"></circle>
                                <circle cx="19" cy="21" r="1"></circle>
                                <path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"></path>
                            </svg>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                 class="text-gray-400">
                                <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"></path>
                                <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"></path>
                            </svg>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                 class="text-gray-400">
                                <circle cx="12" cy="12" r="10"></circle>
                            </svg>
                            <button type="button" role="combobox" aria-controls="radix-:r16:" aria-expanded="false"
                                    aria-autocomplete="none" dir="ltr" data-state="closed" data-placeholder=""
                                    class="flex h-10 w-full items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                                    id="language"><span style="pointer-events: none;">Language</span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                     class="h-4 w-4 opacity-50" aria-hidden="true">
                                    <path d="m6 9 6 6 6-6"></path>
                                </svg>
                            </button>
                        </div>-->
        </header>
        <!--        $q.screen.height-->
        <div style="width: 100%;height: 90%" class=" bg-amber-100">
            <q-scroll-area style="height: 100%; max-width: 100%;">
                <div class="row no-wrap">
                    <div class=" flex justify-center items-center " :class="isFullscreen?'flex-wrap':'flex-nowrap'">
                        <div class="h-full q-ma-md" style="width: 450px;height: 300px">
                            <video id="localVideo" style="width: 100%; height:100%; object-fit: fill" controls></video>
                        </div>
                        <div class="h-full q-ma-md" style="width: 450px;height: 300px">
                            <video id="remoteVideo" style="width: 100%; height:100%; object-fit: fill" controls></video>
                        </div>
                        <!--                        <div  v-for="(item,index) in currSendUserIdList" class="h-full q-ma-md" style="width: 450px;height: 300px"  >-->
                        <!--                            <video :id="remoteVideo+index" style="width: 100%; height:100%; object-fit: fill"-->
                        <!--                                   controls></video>-->
                        <!--                        </div>-->
                    </div>
                </div>
            </q-scroll-area>
        </div>


        <footer class="bg-accent flex justify-center items-center p-4 space-x-8">
            <q-btn class="">
                <q-tooltip>
                    上一页
                </q-tooltip>
            </q-btn>

            <q-space/>


            <!--            亮度调节-->
            <q-btn @click="getDisplayMedia">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="text-white">
                    <circle cx="12" cy="12" r="4"></circle>
                    <path d="M12 2v2"></path>
                    <path d="M12 20v2"></path>
                    <path d="m4.93 4.93 1.41 1.41"></path>
                    <path d="m17.66 17.66 1.41 1.41"></path>
                    <path d="M2 12h2"></path>
                    <path d="M20 12h2"></path>
                    <path d="m6.34 17.66-1.41 1.41"></path>
                    <path d="m19.07 4.93-1.41 1.41"></path>
                </svg>
            </q-btn>
            <div class="flex space-x-4">
                <!--                画图-->
                <q-btn
                    class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 bg-gray-700 text-white">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="text-white">
                        <path d="M22 2 2 22"></path>
                    </svg>
                </q-btn>
                <!--               声音开关 -->
                <q-btn
                    class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 bg-gray-700 text-white">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="text-white">
                        <path
                            d="M3 14h3a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-7a9 9 0 0 1 18 0v7a2 2 0 0 1-2 2h-1a2 2 0 0 1-2-2v-3a2 2 0 0 1 2-2h3"></path>
                    </svg>
                </q-btn>
                <!--                设置-->
                <q-btn
                    class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 bg-gray-700 text-white">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="text-white">
                        <path
                            d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"></path>
                        <circle cx="12" cy="12" r="3"></circle>
                    </svg>
                </q-btn>
            </div>
            <!--            挂断结束 -->
            <q-btn @click="endCall"
                   class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-destructive/90 h-10 px-4 py-2 bg-red-600 text-white">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="text-white">
                    <path
                        d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                </svg>
            </q-btn>
            <div class="flex space-x-4">
                <!--                拍照-->
                <button
                    class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 bg-gray-700 text-white">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="text-white">
                        <path
                            d="M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3l-2.5-3z"></path>
                        <circle cx="12" cy="13" r="3"></circle>
                    </svg>
                </button>

                <!--                添加人数-->
                <q-btn
                    class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 bg-gray-700 text-white">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="text-white">
                        <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path>
                        <circle cx="9" cy="7" r="4"></circle>
                        <line x1="19" x2="19" y1="8" y2="14"></line>
                        <line x1="22" x2="16" y1="11" y2="11"></line>
                    </svg>
                </q-btn>
                <!--                取消-->
                <q-btn
                    class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 bg-gray-700 text-white">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         class="text-white">
                        <polyline points="9 17 4 12 9 7"></polyline>
                        <path d="M20 18v-2a4 4 0 0 0-4-4H4"></path>
                    </svg>
                </q-btn>
            </div>


            <!--全屏-->
            <q-btn @click="fullScreenClickHandel">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="text-white">
                    <path
                        d="M12 7.5a4.5 4.5 0 1 1 4.5 4.5M12 7.5A4.5 4.5 0 1 0 7.5 12M12 7.5V9m-4.5 3a4.5 4.5 0 1 0 4.5 4.5M7.5 12H9m7.5 0a4.5 4.5 0 1 1-4.5 4.5m4.5-4.5H15m-3 4.5V15"></path>
                    <circle cx="12" cy="12" r="3"></circle>
                    <path d="m8 16 1.5-1.5"></path>
                    <path d="M14.5 9.5 16 8"></path>
                    <path d="m8 8 1.5 1.5"></path>
                    <path d="M14.5 14.5 16 16"></path>
                </svg>
            </q-btn>
            <q-space/>
            <q-btn class="">
                <q-tooltip>
                    下一页
                </q-tooltip>
            </q-btn>
        </footer>
    </div>


</template>
<script lang="ts" setup>
import {AppFullscreen, Notify} from "quasar";
import {bus} from "@/boot/eventbus";
import {RTCMsg} from "@/api/result.data";
import {call} from "@/api/im/msgRecord";
import {MsgRecordVO} from "@/api/im/msgRecord/types";

const $q = useQuasar()

const videoOff = ref(false)
const audioOff = ref(false)
const isFullscreen = ref(false)
const show1 = ref(false) // 整体显示
const mainHeight = ref($q.screen.height / 2)
const rtcMsg = ref<RTCMsg>({
    toUserId: undefined,
    type: '',
    content: undefined
})
const localVideo = ref<HTMLVideoElement | null>();
const remoteVideo = ref<HTMLVideoElement | null>();
const remoteVideoArray = ref<Array<HTMLVideoElement | null>>([]);
const currSendUserIdList = ref<[]>([])

const mainStyle = computed(() => {
    return {height: `${mainHeight}px`, width: '100%'};
});
const configuration = {'iceServers': [{'urls': 'stun:stun.example.org'}]};
let peerConnection = undefined

// 全屏
function fullScreenClickHandel(e: any) {
    const communicateId: Element = document.getElementById("communicateId")
    AppFullscreen.toggle(communicateId)
    isFullscreen.value = !isFullscreen.value;
}

// 挂断
function endCall() {
    peerConnection.close()
    show1.value = false
    peerConnection = new RTCPeerConnection(configuration)

    // 显示当前活动右侧框
    bus.emit("ACTIVE_USER_SHOW")
}

/**
 * 获取屏幕共享
 */
function getDisplayMedia() {
    navigator.mediaDevices.getDisplayMedia().then(stream => {
        localVideo.value.srcObject = stream;
        localVideo.value?.play()

    }).catch(err => {
        console.log("获取屏幕共享;", err)
    })
}

/**
 * 得到媒体设备
 */
function getMediaDevices() {
    navigator.mediaDevices.getUserMedia({
        video: true,
        audio: false
    }).then(stream => {
        localVideo.value!.srcObject = stream;
        localVideo.value?.play()
        stream.getTracks().forEach(track => peerConnection.addTrack(track, stream));
        peerConnection.ontrack = function (event) {
            remoteVideo.value.srcObject = event.streams[0];
            remoteVideo.value?.play()
        };
    })
}

onMounted(() => {
    peerConnection = new RTCPeerConnection(configuration);
    localVideo.value = document.getElementById("localVideo") as HTMLVideoElement;
    remoteVideo.value = document.getElementById("remoteVideo") as HTMLVideoElement;
    getMediaDevices();
    bus.on("startCall", async (sendUserId: []) => {
        show1.value = true
        currSendUserIdList.value = sendUserId
        const offer = await peerConnection.createOffer();
        await peerConnection.setLocalDescription(offer);
        rtcMsg.value.type = "offer";
        rtcMsg.value.content = JSON.stringify(offer);
        rtcMsg.value.toUserId = sendUserId;
        const {code, data} = await call(rtcMsg.value);

    })

    // 当接收到远程方的 SDP offer 时
    bus.on("offer", async (msgVo: MsgRecordVO) => {
        show1.value = true
        try {
            currSendUserIdList.value = []
            currSendUserIdList.value.push(msgVo.senderId)
            // @ts-expect-error
            await peerConnection.setRemoteDescription(new RTCSessionDescription(msgVo.content));
            console.log("设置远程描述=>", msgVo.content)
            // 创建 SDP answer
            const answer = await peerConnection.createAnswer();
            await peerConnection.setLocalDescription(answer);
            // 发送 SDP answer 给远程方
            rtcMsg.value.type = "answer";
            rtcMsg.value.content = JSON.stringify(answer);
            rtcMsg.value.toUserId = [msgVo.senderId];
            await call(rtcMsg.value);
            console.log("发送answer=>", rtcMsg.value.content)
        } catch (error) {
            console.error("Error processing offer:", error);
        }
    });

    // 当接收到远程方的 SDP answer 时
    bus.on("answer", async (msgVo: MsgRecordVO) => {
        try {
            currSendUserIdList.value = []
            currSendUserIdList.value.push(msgVo.senderId)
            // @ts-expect-error 将远程方的 SDP answer 设置为远程描述
            await peerConnection.setRemoteDescription(new RTCSessionDescription(msgVo.content));
            console.log("设置远程描述=>", msgVo.content)
        } catch (error) {
            console.error("Error processing answer:", error);
        }

    });

    // 接收 ICE 候选者并发送给远程方：
    peerConnection.onicecandidate = async function (event) {
        console.log("候选地址=>", event)
        if (event.candidate && event.candidate.protocol == "udp") {
            // // 发送 ICE 候选者给远程方
            // 三秒后在处理
            setTimeout(async () => {
                rtcMsg.value.type = "candidate";
                rtcMsg.value.content = JSON.stringify(event.candidate);
                rtcMsg.value.toUserId = currSendUserIdList.value;
                await call(rtcMsg.value);
            }, 1400)

        }
    };

    // 当接收到远程方的 ICE 候选者时
    bus.on("candidate", async (msgVo: MsgRecordVO) => {
        try {
            currSendUserIdList.value = []
            currSendUserIdList.value.push(msgVo.senderId)
            console.log("当接收到远程方的 ICE 候选者时=>", msgVo.content)
            //@ts-ignore 将远程方的 ICE 候选者添加到本地的 RTCPeerConnection 中
            await peerConnection.addIceCandidate(new RTCIceCandidate(msgVo.content));
        } catch (error) {
            console.error("Error processing ICE candidate:", error);
        }
    });


})
onUnmounted(() => {
    bus.off("candidate")
    bus.off("answer")
    bus.off("startCall")
    bus.off("offer")

})

</script>


<style scoped>

</style>
