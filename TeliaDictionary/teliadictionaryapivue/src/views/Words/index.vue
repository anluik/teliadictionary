<template>
    <div>
        <br />
        <h3>Dictionary</h3>
        <hr />

        <!-- Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteModalLabel">Delete word '{{ modalDataTerm }}'</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure?<br/>
                        This will also delete all translations linked to this word.<br/>
                        Some words might be left without a translation.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-dark" data-dismiss="modal">Cancel</button>
                        <button @click="onDelete(modalDataTerm)" type="button" class="btn btn-dark" data-dismiss="modal">Delete</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Navigation -->
        <nav id="navbar-example2" class="navbar navbar-light bg-light">
            <ul class="nav nav-pills">
                <div v-for="letter in alphabet" :key="letter">
                    <li class="nav-item"><a class="nav-link" :href="'#' + letter">{{ letter }}</a></li>
                </div>
            </ul>
        </nav>

        <br/>
        <Alert />
        <!-- Add form and language switch button-->
        <div>
            <button class="btn btn-dark" type="button" data-toggle="collapse" data-target="#collapseForm" aria-expanded="false" aria-controls="collapseForm">
                Add to dictionary
            </button>
            <button @click="switchLanguage()" class="btn btn-dark" type="button" style="margin-left: 5px">
                {{ getIsLanguageEst() ? "Estonian -> English" : "English -> Estonian" }}<i class="fa fa-exchange"></i>
            </button>
        </div>
        <br />
        <div class="collapse" id="collapseForm">
            <div class="card card-body">
                <form @submit="addNewDictionaryEntry()">
                    <div class="form-row">
                        <div class="col-md-10 mb-3">
                            <label for="newWord">New word in {{ getIsLanguageEst() ? "Estonian" : "English" }}*</label>
                            <input v-model="newWord.term" type="text" class="form-control" placeholder="New word (required)" maxlength="32" value="" required>
                        </div>
                    </div>
                    <label for="newWord">Translations in {{ getIsLanguageEst() ? "English" : "Estonian" }}</label>
                    <div class="form-row">
                        <div class="col-md-6 mb-3">
                            <div class="text-right">
                                <input v-model="newTranslations.translation1" type="text" class="form-control" maxlength="32" placeholder="Translation 1" value="">
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="text-right">
                                <input v-model="newTranslations.translation2" type="text" class="form-control" maxlength="32" placeholder="Translation 2" value="">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="col-md-6 mb-3">
                            <div class="text-right">
                                <input v-model="newTranslations.translation3" type="text" class="form-control" maxlength="32" placeholder="Translation 3" value="">
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="text-right">
                                <input v-model="newTranslations.translation4" type="text" class="form-control" maxlength="32" placeholder="Translation 4" value="">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="col-md-6 mb-3">
                            <div class="text-right">
                                <input v-model="newTranslations.translation5" type="text" class="form-control" maxlength="32" placeholder="Translation 5" value="">
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="text-right">
                                <input v-model="newTranslations.translation6" type="text" class="form-control" maxlength="32" placeholder="Translation 6" value="">
                            </div>
                        </div>
                    </div>
                    <label for="newWord">Examples in {{ getIsLanguageEst() ? "Estonian" : "English" }}</label>
                    <div class="form-row">
                        <div class="col mb-3">
                            <div class="text-right">
                                <input v-model="newExamples.example1" type="text" class="form-control" maxlength="256" placeholder="Example 1" value="">
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="col mb-3">
                            <div class="text-right">
                                <input v-model="newExamples.example2" type="text" class="form-control" maxlength="256" placeholder="Example 2" value="">
                            </div>
                        </div>
                    </div>
                    <button :disabled="newWord.term.length === 0" class="btn btn-dark" type="submit" data-toggle="collapse" data-target="#collapseForm">Done</button>
                </form>
            </div>
        </div>

        <br />

        <!-- Words -->
        <div v-if="getIsLanguageEst() ? estWords.length !== 0 : engWords.length !== 0">
            <div data-spy="scroll" data-target="#navbar-example2" data-offset="0">
                <div v-for="letter in alphabet" :key="letter">
                    <h4 :id="letter">{{ letter }}</h4>
                    <div v-for="word in (getIsLanguageEst() ? estWords : engWords)" :key="word.id">
                        <div v-if="word.term[0] === letter">
                            <div class="dropright">
                                <button class="btn btn-link" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <span class="text-dark">{{ word.term }}</span>
                                </button>
                                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                    <span @click="dropdownSearch(word.term)" class="dropdown-item">Search</span>
                                    <span @click="modalAction(word.term)" class="dropdown-item" data-toggle="modal" data-target="#deleteModal">Delete word</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-else>
            <h4>Dictionary is empty</h4>
        </div>
        <br/><br/>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { AlertType } from "@/types/AlertType";
import Alert from "@/components/Alert.vue";
import router from "../../router";
import store from "@/store";
import { IWord } from '../../domain/Words/IWord';
import { IWordCreate } from '../../domain/Words/IWordCreate';
import { IExampleCreate } from '../../domain/Examples/IExampleCreate';

@Component({
    components: {
        Alert
    }
})
export default class WordsIndex extends Vue {
    private modalDataTerm = "";
    private newWord: IWordCreate = { // new word.
        term: ""
    }

    private newTranslations: {[key: string]: string} = { // translations to new word. If translation word doesn't exist it will also be added.
        translation1: "",
        translation2: "",
        translation3: "",
        translation4: "",
        translation5: "",
        translation6: ""
    }

    private newExamples: {[key: string]: string} = { // new examples for word.
        example1: "",
        example2: ""
    }

    private alphabet: string[] = [];
    private estWords: IWord[] = [];
    private engWords: IWord[] = [];

    switchLanguage() { // switch language mode
        store.commit("setLanguage");
        this.updateAlphabet();
    }

    updateAlphabet() { // update alphabet list for dictionary page letter navigation
        this.alphabet = (this.getIsLanguageEst() ? this.estWords : this.engWords).map(function(word) {
            return word.term.charAt(0)
        });
        this.alphabet = this.alphabet.filter((letter, index) => {
            return this.alphabet.indexOf(letter) === index;
        }).sort();
    }

    dropdownSearch(term: string) { // search term when user clicked word -> search from dropdown
        router.push({ name: "Home", params: { term: term } })
    }

    getIsLanguageEst() { // get boolean if language mode is Estonian
        return store.state.languageEst;
    }

    getLanguage() { // get language mode abbreviation
        return store.state.languageEst ? "est" : "eng"
    }

    modalAction(term: string) { // set data for modal
        this.modalDataTerm = term;
    }

    onDelete(deleteTerm: string) { // delete word
        store.dispatch("deleteWord", { term: deleteTerm, lang: this.getLanguage() }).then(() => {
            this.refreshWords();
        })
    }

    addNewDictionaryEntry() { // add a new word to dictionary with translations and examples if any
        // add new words to dictionary. newWord in one language, translations in other.
        const translationWords: string[] = Object.values(this.newTranslations);
        const words: IWordCreate[] = translationWords
            .filter(translation => translation !== "") // no empty words.
            .map(translation => this.mapStringToIWordCreate(translation)); // map to correct type.

        // creating newWord
        store.dispatch("createWord", { lang: this.getLanguage(), data: [this.newWord] }).then(() => {
            // add examples. Examples can be added when the word has been added first.
            for (const example of Object.values(this.newExamples)) {
                if (example !== "") {
                    store.dispatch("createExample", { term: this.newWord.term, lang: this.getLanguage(), data: this.mapStringToIExampleCreate(example) }).then(() => {
                        for (const key in this.newExamples) {
                            this.newExamples[key] = "";
                        }
                    });
                }
            }
            this.refreshWords();

            if (words.length !== 0) {
                // creating translation words
                store.dispatch("createWord", { lang: this.getIsLanguageEst() ? "eng" : "est", data: words }).then(() => {
                    // create translations between the newWord and all translations (if not empty string)
                    store.dispatch("createTranslation", { lang: this.getLanguage(), data: [this.newWord, ...words] }).then(() => {
                        for (const key in this.newTranslations) {
                            this.newTranslations[key] = "";
                        }
                        this.newWord.term = "";
                    });
                });
            } else {
                this.newWord.term = "";
            }

            store.commit("setAlert", {
                message: "✅",
                type: AlertType.Success,
                dismissable: true
            });
        });
    }

    mapStringToIWordCreate(word: string): IWordCreate { // map word string -> IWordCreate
        // mapping translation words to IWordCreate
        return {
            term: word
        }
    }

    mapStringToIExampleCreate(text: string): IExampleCreate { // map text string -> IExampleCreate
        // mapping translation words to IWordCreate
        return {
            exampleText: text
        }
    }

    refreshWords() { // refresh words lists
        store.dispatch("getWords").then(() => {
            this.estWords = store.state.estWords;
            this.engWords = store.state.engWords;
            this.updateAlphabet();
        });
    }

    mounted() { // lifecycle method
        this.refreshWords();
    }
}
</script>

<style>
.nav-link {
    color: black;
}

.fa-exchange {
    margin-left: 10px;
}

.btn.btn-link:focus {
    outline: none;
    box-shadow: none;
}

.btn-link {
    color: black !important;
}
</style>
