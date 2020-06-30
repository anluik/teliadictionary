<template>
    <div>
        <header>
            <nav class="navbar navbar-expand-sm navbar-toggleable-sm navbar-light bg-blue border-bottom box-shadow mb-3 position-fixed"
                style="width: 100%">
                <div class="container">
                    <router-link to="/" class="navbar-brand text-dark">MyDictionary</router-link>
                    <button
                        class="navbar-toggler"
                        type="button"
                        data-toggle="collapse"
                        data-target=".navbar-collapse"
                        aria-controls="navbarSupportedContent"
                        aria-expanded="false"
                        aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                </div>
            </nav>
        </header>
        <div class="col-sm" style="height: 100%; left: 10%; width: 60%; top: 60px">
            <main role="main" class="pb-3">
                <keep-alive include="Words">  <!-- keeping page alive while routing elsewhere -->
                    <router-view/>
                </keep-alive>
            </main>
        </div>
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
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import store from '../src/store/index';

@Component
export default class App extends Vue {
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
</style>
