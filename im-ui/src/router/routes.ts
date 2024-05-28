import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'index',
        redirect: '/index',
        children: [
            {
                path: '/index',
                name: 'layout',
                redirect: '/home/0',
                component: () => import( '@/layouts/index.vue'),
                children: [
                    {
                        path: '/home/:id',
                        name: 'home',
                        component: () => import('@/views/index.vue'),
                        children: [
                        ],
                    },
                ]
            },
            {
                path: '/guild-list', name: "guild-list", component: () => import('@/views/guild-im/index.vue'),
            },
            {
                path: '/test', name: "test", component: () => import('@/views/test.vue'),
            },
        ],

    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/login.vue'),
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('@/views/register.vue'),
    },


    // Always leave this as last one,
    // but you can also remove it
    {
        path: '/:catchAll(.*)*',
        component: () => import('@/views/ErrorNotFound.vue'),
    },
];

export default routes;
