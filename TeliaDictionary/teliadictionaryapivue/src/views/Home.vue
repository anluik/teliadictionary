<template>
    <div style="word-break: break-word">
        <div class="input-group mb-3" style="top: 20px">
            <div class="input-group-prepend">
                <button @click="searchTerm(searchInput)" class="btn btn-outline-secondary" type="button" style="background: whitesmoke">Search</button>
            </div>
            <input type="text" class="form-control" :placeholder="getLanguage() === 'est' ? 'Estonian -> English' : 'English -> Estonian'"
                aria-label="Recipient's username" aria-describedby="basic-addon2" v-model="searchInput">
            <div class="input-group-append">
                <button @click="switchLanguage()" class="btn btn-outline-secondary" type="button" style="background: whitesmoke">
                    <i class="fa fa-exchange"></i>
                </button>
            </div>
        </div>
        <br />
        <Alert />

        <div>
            <div style="width: 100%; border-bottom: solid black; margin-top: 50px; word-break: break-word">
                <h4>Exact Match</h4>
            </div>
            <br/>
            <div v-if="exactLoader">
                <div class="d-flex align-items-center">
                    <strong><h5>Loading...</h5></strong>
                    <div class="spinner-border ml-auto" role="status" aria-hidden="true"></div>
                </div>
            </div>
            <div v-else-if="translations.length !== 0">
                <h5><strong>{{ firstWord }}</strong></h5>
                <div class="row" style="margin-left: 0">
                    <div v-for="translation in translations" :key="translation.id" style="padding-right: 20px">
                        {{ getLanguage() === "est" ? translation.secondWord.term : translation.firstWord.term }}
                    </div>
                </div>
                <br />
                <p>
                    <button @click="showExamples(firstWord)" class="btn btn-dark" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                        See Examples
                    </button>
                </p>
                    <div class="collapse" id="collapseExample">
                        <div class="card card-body">
                            <div v-if="examples !== null && examples.length !== 0">
                                <div v-for="example in examples" :key="example.id">
                                    {{ example.exampleText }}
                                </div>
                            </div>
                            <div v-else>
                                <i class="fa fa-frown-o"></i>
                                no examples
                            </div>
                        </div>
                    </div>
                </div>
            <div v-else>
                Nothing found currently
            </div>
        </div>

        <div>
            <div style="width: 100%; border-bottom: solid black; margin-top: 50px; word-break: break-word">
                <h4>Similar Matches</h4>
            </div>
            <br/>
            <div v-if="fuzzyLoader">
                <div class="d-flex align-items-center">
                    <strong><h5>Loading...</h5></strong>
                    <div class="spinner-border ml-auto" role="status" aria-hidden="true"></div>
                </div>
            </div>
            <div v-else>
                <div v-if="fuzzyWords.length !== 0">
                    <p style="font-size: 10px">Click word for search</p>
                    <div class="row" style="margin-left: 0">
                        <div v-for="word in takeNFuzzyWords(5)" :key="word.id" style="padding-right: 20px">
                            <h5 class="fuzzy-word" @click="fuzzyWordClicked(word.term)">{{ word.term }}</h5>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { ITranslation } from '../domain/Translations/ITranslation';
import { IExample } from '../domain/Examples/IExample';
import store from '../store';
import { IWord } from '../domain/Words/IWord';
import Alert from '../components/Alert.vue';

@Component({
    components: {
        Alert
    }
})
export default class Home extends Vue {
    private searchInput = "";
    private exactLoader = false;
    private fuzzyLoader = false;
    private firstWord = "";
    private showingExamples = false;
    private translations: ITranslation[] = [];
    private examples: IExample[] | null = null;
    private fuzzyWords: IWord[] = [];

    switchLanguage() { // switch language mode
        store.commit("setLanguage");
        this.searchInput = "";
        this.translations = [];
        this.examples = null;
        this.fuzzyWords = [];
    }

    searchTerm(searchInput: string) { // search for a word in dictionary and its translations and its fuzzy words
        this.exactLoader = true;
        this.fuzzyLoader = true;
        this.examples = null;
        this.firstWord = this.searchInput;
        this.fuzzyWords = [];
        if (searchInput.length !== 0) {
            store.dispatch("getTranslations", { lang: this.getLanguage(), term: searchInput }).then(() => {
                this.translations = store.state.translations;
                this.exactLoader = false;
            });
            store.dispatch("getFuzzyWords", { term: searchInput, lang: this.getLanguage() }).then(() => {
                this.fuzzyWords = store.state.fuzzyWords;
                this.fuzzyLoader = false;
            })
        } else {
            this.translations = [];
            this.exactLoader = false;
            this.fuzzyLoader = false;
        }
    }

    showExamples(term: string) { // get examples for a word and show them
        if (this.examples === null) {
            store.dispatch("getExamples", { lang: this.getLanguage(), term: term }).then(() => {
                this.examples = store.state.examples;
            });
        }
    }

    getLanguage(): string { // get current language mode
        return store.state.languageEst ? "est" : "eng";
    }

    fuzzyWordClicked(word: string) { // translate fuzzy word
        this.searchInput = word;
        this.searchTerm(this.searchInput);
    }

    takeNFuzzyWords(n: number): IWord[] { // display n first fuzzy words
        return this.fuzzyWords.slice(0, n);
    }

    mounted() { // lifecycle method
        if (this.$route.params.term !== undefined) {
            this.searchInput = this.$route.params.term;
            this.searchTerm(this.searchInput);
        }
    }
}
</script>

<style>
.btn-outline-secondary:hover {
    color:black;
    background-color:#6D6B70;
    border-color: #6D6B70
}

.fuzzy-word {
  color: black;
}

.fuzzy-word:hover {
  background: rgb(173, 216, 230, 0.3);
}
</style>
