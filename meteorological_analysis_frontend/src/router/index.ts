import {createRouter, createWebHistory} from 'vue-router'
import HomePage from "@/page/HomePage.vue";
import UploadDataPage from "@/page/UploadDataPage.vue";
import AboutPage from "@/page/AboutPage.vue";

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
            component: UploadDataPage,
        },
        {
            path: '/about',
            name: 'about',
            component: AboutPage,
        },
    ],
})

export default router
