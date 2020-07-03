<template>
    <div>
        <header>
            <nav class="navbar navbar-expand-sm navbar-toggleable-sm navbar-light bg-blue border-bottom box-shadow mb-3 position-fixed"
                style="width: 100%; z-index: 1">
                <div class="container">
                    <router-link to="/" class="navbar-brand text-dark">MyDictionary</router-link>
                    <button v-if="isMobile()"
                        class="navbar-toggler"
                        type="button"
                        data-toggle="collapse"
                        data-target=".navbar-collapse"
                        aria-controls="navbarSupportedContent"
                        aria-expanded="false"
                        aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                     <div v-if="isMobile()" class="navbar-collapse collapse d-sm-inline-flex flex-sm-row-reverse">
                        <ul class="navbar-nav flex-grow-1">
                            <li class="nav-item">
                                <router-link class="nav-link active" to="/">Home</router-link>
                            </li>
                            <li class="nav-item">
                                <router-link class="nav-link" to="/words">Dictionary</router-link>
                            </li>
                            <li class="nav-item">
                                <router-link class="nav-link" to="/about">About</router-link>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
        <div class="col-sm" :style="getStyle()">
            <main role="main" class="pb-3">
                <keep-alive include="Words">  <!-- keeping page alive while routing elsewhere -->
                    <router-view/>
                </keep-alive>
            </main>
        </div>
        <div v-if="!isMobile()">
            <div class="position-fixed" style="left: 75%; width: 30%; top: 100px">
                <ul class="nav flex-column nav-pills" role="tablist" aria-orientation="vertical">
                    <li class="nav-item pill-1">
                        <router-link class="nav-link active" data-toggle="pill" to="/" role="tab" aria-selected="true">Home</router-link>
                    </li>
                    <li class="nav-item pill-2">
                        <router-link class="nav-link" data-toggle="pill" to="/words" role="tab" aria-selected="false">Dictionary</router-link>
                    </li>
                    <li class="nav-item pill-3">
                        <router-link class="nav-link" data-toggle="pill" to="/about" role="tab" aria-selected="false">About</router-link>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from './store';

@Component
export default class App extends Vue {
    isMobile() {
        return store.state.isMobile();
    }

    getStyle() {
        if (this.isMobile()) {
            return 'height: 100%; left: 10%; width: 80%; top: 60px'
        } else {
            return 'height: 100%; left: 10%; width: 60%; top: 60px'
        }
    }
}
</script>

<style>
html {
    overflow-y: scroll;
}

.verticalLine {
  border-left: thick solid #ff0000;
}

/* not active */
.nav-pills .pill-1 .nav-link:not(.active) {
    background: white; /* Pill fill color */
    color: black; /* Pill text color */
}

.nav-pills .pill-2 .nav-link:not(.active) {
    background: white;
    color: black;
}

.nav-pills .pill-3 .nav-link:not(.active) {
    background: white;
    color: black;
}

/* active (faded) */
.nav-pills .pill-1 .nav-link:active {
    background-color: black !important;
}

.nav-pills .pill-2 .nav-link:active {
    background: black !important;
}

.nav-pills .pill-3 .nav-link:active {
    background: black !important;
}

/* pills margin */
.nav-pills li {
    margin-bottom: 5px;
}

.navbar {
    background: white;
}
</style>
