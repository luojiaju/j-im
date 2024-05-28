<template>
    <q-layout view="lhh lpR lFr">
        <q-drawer dark v-model="leftDrawerOpen" :mini="miniState" :breakpoint="700"
                  class="global-dark">
            <appbox></appbox>
        </q-drawer>
        <q-page-container>
            <q-page>
                <router-view/>
            </q-page>
        </q-page-container>
    </q-layout>
</template>

<script lang="ts" setup>
import appbox from "@/layouts/appbox/index.vue";

const router = useRouter;

const leftDrawerOpen = ref(true);
const miniState = ref(true);

</script>
