import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/Home.vue";

import WordsIndex from "../views/Words/index.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
    { path: "/", name: "Home", component: Home },
    {
        path: "/about",
        component: () =>
            import(/* webpackChunkName: "about" */ "../views/About.vue")
    },

    { path: "/words", name: "Words", component: WordsIndex }
];

const router = new VueRouter({
    routes
});

export default router;
