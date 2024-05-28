<!--组件地址: https://www.ihtmlcss.com/demo/dist/#/croptool-->
<template>
    <div class="q-gutter-md  " >
        <ImgCutter v-on:cutDown="cutDown" :maxSize="1" :cutWidth="imgWidth" :cutHeight="imgHeight">
            <template #open>
                <div class="">
                    <q-img :width="imgWidth+'px'" :height="imgHeight+'px'" :src="showUrl">
                        <q-tooltip>{{label}}</q-tooltip>
                        <slot name="preview"></slot>
                        <img v-if="modelValue && modelValue.length <= 0" class="absolute-top"
                             :width="imgWidth+'px'"
                             :height="imgHeight+'px'"
                             :src="getDefaultImage" alt="">
                        <img v-else class="absolute-top" :width="imgWidth+'px'" :height="imgHeight+'px'"
                             :src="modelValue" alt="">

                    </q-img>
                </div>
            </template>
        </ImgCutter>
    </div>
</template>
<script lang="ts" setup>
import ImgCutter from 'vue-img-cutter';
import {uploadImage} from "@/api/upload";
import {Notify, QSpinnerFacebook} from "quasar";
import {getDefaultImage} from "@/utils/ruoyi";

const props = defineProps({
    modelValue: {
        type: String,
        default: ''
    },
    label:{
        type: String,
        default: '上传图片'
    },
    imgWidth:{
        type: Number,
        default: 250
    },
    imgHeight:{
        type: Number,
        default: 250
    },


})

const $q = useQuasar()
const emits = defineEmits(['update:modelValue','update:imgData'])
const showUrl = ref()
const isDefault =ref(true)

/**
 * 裁剪完成回调
 * @param fileName
 * @param file
 * @param blob
 * @param dataURL
 * @param index
 */
async function cutDown({fileName, file, blob,dataURL,index}) {
    $q.loading.show({
        message: "上传中",
        spinner: QSpinnerFacebook,
        spinnerColor: 'yellow',
        spinnerSize: 100,
        backgroundColor: 'purple',
        messageColor: 'black'
    })

    try {
        let {code, msg, data} = await uploadImage({fileName,file,blob})
        // 阻塞 3秒
        await new Promise(resolve => setTimeout(resolve, 3000));
        if (code == 200) {
            let {fileName, ossId, url} = data
            showUrl.value = dataURL
            showUrl.value = url
            isDefault.value=false
            emits('update:modelValue', ossId)
            emits('update:imgData', {fileName, file, blob, dataURL, index})
        } else {
            Notify.create({
                message: msg,
                color: 'red',
                position: 'top',
                timeout: 3000,
                textColor: 'white',
            })
        }
    }catch (e){
        console.log(e)
    }finally {
        $q.loading.hide()
    }


}

onMounted(()=>{
    console.log(props.modelValue);
})

</script>

<style scoped lang="scss">
.hidden {
    display: none;
}

.visible {
    display: inline-block;
}

.hover-visible {
    @apply hidden;
}

.hover-visible:hover {
    @apply visible;
}

.hover-visible {
    display: none;
}

.hover-visible:hover {
    display: inline-block;
}


</style>
