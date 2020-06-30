<template>
    <div>
        <div v-if="isAlert" :class="'alert alert-' + alertData.type + ' alert-dismissible fade show'" role="alert">
            {{alertData.message}}
            <button v-if="alertData.dismissable" type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { IAlertData } from './../types/IAlertData';
import { Component, Prop, Vue } from "vue-property-decorator";
import store from "../store";
import router from '../router';

@Component
export default class Alert extends Vue {
    public alertData: IAlertData | null = null;

    get isAlert() {
        if (store.state.alert !== null) {
            this.alertData = store.state.alert;
        }
        return store.state.alert !== null;
    }
}
</script>
