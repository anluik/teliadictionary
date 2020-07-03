import { IExample } from './../domain/Examples/IExample';
import { IWordCreate } from './../domain/Words/IWordCreate';
import { IWord } from './../domain/Words/IWord';
import Vue from 'vue'
import Vuex from 'vuex'
import { IAlertData } from '@/types/IAlertData'
import { IFetchResponse } from '@/types/IFetchResponse'
import { AlertType } from '@/types/AlertType'
import { WordsService } from '@/services/WordsService'
import { TranslationsService } from '@/services/TranslationsService';
import { ITranslation } from '@/domain/Translations/ITranslation';
import { ExamplesService } from '@/services/ExampleService';
import { IExampleCreate } from '@/domain/Examples/IExampleCreate';

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        alert: null as IAlertData | null,
        navigationMenuActive: 1,
        languageEst: true,
        searchTerm: "",
        isMobile: () => {
            const toMatch = [
                /Android/i,
                /webOS/i,
                /iPhone/i,
                /iPad/i,
                /iPod/i,
                /BlackBerry/i,
                /Windows Phone/i
            ];

            return toMatch.some((toMatchItem) => {
                return navigator.userAgent.match(toMatchItem);
            });
        },

        fuzzyWords: [] as IWord[],
        estWords: [] as IWord[],
        engWords: [] as IWord[],
        word: {} as IWord | null,

        translations: [] as ITranslation[],
        translation: {} as ITranslation | null,

        examples: [] as IExample[],
        example: {} as IExample | null
    },
    mutations: {
        setAlert(state, alert: IAlertData | null) {
            state.alert = alert;
            setTimeout(function() {
                state.alert = null;
            }, 2500)
        },
        setLanguage(state) {
            state.languageEst = !state.languageEst;
        },

        setEstWords(state, estWords: IWord[]) {
            state.estWords = estWords;
        },
        setEngWords(state, engWords: IWord[]) {
            state.engWords = engWords;
        },
        setFuzzyWords(state, fuzzyWords: IWord[]) {
            state.fuzzyWords = fuzzyWords;
        },

        setTranslations(state, translations: ITranslation[]) {
            state.translations = translations;
        },

        setExamples(state, examples: IExample[]) {
            state.examples = examples;
        }
    },
    actions: {
        updateAlert(context, response: IFetchResponse<string>) {
            if (response !== null) {
                context.commit("setAlert", {
                    message: response.errorMessage,
                    type: AlertType.Info,
                    dismissable: false
                });
            }
        },

        // ================================= words =================================
        async getWords(context): Promise<void> {
            const response1 = await WordsService.getWords("est");
            const response2 = await WordsService.getWords("eng");
            if (response1.statusCode === 200) {
                context.commit("setEstWords", response1.data);
            } else {
                context.dispatch('updateAlert', response1);
            }
            if (response2.statusCode === 200) {
                context.commit("setEngWords", response2.data);
            } else {
                context.dispatch('updateAlert', response2);
            }
        },
        async getFuzzyWords(context, params: {term: string; lang: string }): Promise<void> {
            const response = await WordsService.getFuzzyWords(params.lang, params.term);
            if (response.statusCode === 200) {
                context.commit("setFuzzyWords", response.data);
            } else {
                context.dispatch('updateAlert', response);
            }
        },
        async createWord(context, params: { lang: string; data: IWordCreate[] }): Promise<void> {
            const response = await WordsService.createWord(params.lang, params.data);
            if (!(response.statusCode === 201)) {
                context.dispatch('updateAlert', response);
            }
        },
        async deleteWord(context, params: { term: string; lang: string }): Promise<void> {
            const response = await WordsService.deleteWord(params.term, params.lang);
            if (response.statusCode === 204) {
                await context.dispatch('getWords');
            } else {
                context.dispatch('updateAlert', response);
            }
        },

        // ================================= translations =================================
        async getTranslations(context, params: { lang: string; term: string | null }): Promise<void> {
            const response = await TranslationsService.getTranslations(params.lang, params.term);
            if (response.statusCode === 200) {
                context.commit("setTranslations", response.data);
            } else {
                context.dispatch('updateAlert', response);
            }
        },
        async createTranslation(context, params: { lang: string; data: IWordCreate[] }): Promise<void> {
            const response = await TranslationsService.createTranslation(params.lang, params.data);
            if (!(response.statusCode === 201)) {
                context.dispatch('updateAlert', response);
            }
        },

        // ================================= examples =================================
        async getExamples(context, params: { lang: string; term: string }): Promise<void> {
            const response = await ExamplesService.getExamples(params.lang, params.term);
            if (response.statusCode === 200) {
                context.commit("setExamples", response.data);
            } else {
                context.dispatch('updateAlert', response);
            }
        },
        async createExample(context, params: { term: string; lang: string; data: IExampleCreate }): Promise<void> {
            const response = await ExamplesService.createExample(params.data, params.lang, params.term);
            if (!(response.statusCode === 201)) {
                context.dispatch('updateAlert', response);
            }
        }
    },
    modules: {
    }
})
