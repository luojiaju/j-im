import {boot} from 'quasar/wrappers'

import VueCropper from 'vue-cropper';
import 'vue-cropper/dist/index.css'
import axios from "axios";
import driveUtils from "@/utils/driveUtils";
import {getDefaultImage, getDefaultShowImage} from "@/utils/ruoyi";


let RecordAudio = document.createElement("audio")
document.getElementById('global-app')?.appendChild(RecordAudio)

export default boot(({app}) => {
    app.use(VueCropper)
    app.config.globalProperties.$axios = axios;
    app.config.globalProperties.$recordAudio = document.createElement("audio") as HTMLAudioElement; // 语音录制
    app.config.globalProperties.$audioApp = document.getElementById("audioApp") as HTMLAudioElement; // 本地语音
    app.config.globalProperties.$audioRemoteApp = document.getElementById("audioRemoteApp") as HTMLAudioElement; // 远程语音

    app.config.globalProperties.$videoApp = document.getElementById("videoApp") as HTMLVideoElement; // 本地视频
    app.config.globalProperties.$videoRemoteApp = document.getElementById("videoRemoteApp") as HTMLVideoElement; // 远程视频
    app.config.globalProperties.$getDefaultImage = getDefaultImage
    app.config.globalProperties.$getDefaultShowImage = getDefaultShowImage

    // 自定义指令
    app.directive('de', {
        mounted(el,binding) {
            // binding.value= {func:function,delay:1500}
            switch (binding.arg){
                case "click":
                    let timer:any;
                    // 防抖 binding.value = {func:function,delay:2000}
                   el.addEventListener("click",()=>{
                       clearTimeout(timer);
                       timer = setTimeout(()=>{
                           binding.value.func()
                       },binding.value.delay||1500)
                   })
                    console.log("click")
                break;
                default:
                    console.log("默认")
            }
        }
    })

    app.provide("drive", driveUtils)
})
