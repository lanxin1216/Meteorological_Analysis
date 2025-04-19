import {createRouter, createWebHistory} from 'vue-router'
import HomePage from "@/page/HomePage.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomePage,
        },
        {
            path: '/uploadData',
            name: 'uploadData',
            component: () => import('@/page/UploadDataPage.vue'),
        },
        {
            path: '/dataView',
            name: 'dataView',
            component: () => import('@/page/MeteorologicalDataPage.vue'),
        },
    ],
})

export default router
