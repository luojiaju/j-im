<!--
  @FileDescription:  ,
  @Date: 2024/4/25 下午9:11 ,
  @version: 0.0.1,
  @Author: 罗家炬,
 -->
<template>
    <q-dialog v-model="seamless" seamless transition-duration="100"
              transition-show="slide-right"
              transition-hide="slide-left">
        <q-card class="global-dark text-white " style="margin-left: 6vmin;width: 200vmin;height: 88vmin">
            <q-bar>
                <q-space/>
                <q-btn dense flat icon="close" v-close-popup>
                    <q-tooltip class="bg-white text-primary">Close</q-tooltip>
                </q-btn>
            </q-bar>
            <slot name="default">
                <q-card-section>
                    <div class="text-h6">Alert</div>
                </q-card-section>

                <q-card-section class="q-pt-none">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Rerum repellendus sit voluptate voluptas
                    eveniet porro. Rerum blanditiis perferendis totam, ea at omnis vel numquam exercitationem aut, natus
                    minima, porro labore.
                </q-card-section>
            </slot>
        </q-card>
    </q-dialog>

</template>
<script lang="ts" setup>
const seamless = ref(false)

function show() {
    seamless.value = true
}

function hide() {
    seamless.value = false
}

defineExpose({
    show,
    hide
})
</script>


<style scoped>
@media (min-width: 600px) {
    .q-dialog__inner--minimized > div {
        max-width: 149vmin;
    }
}
</style>
