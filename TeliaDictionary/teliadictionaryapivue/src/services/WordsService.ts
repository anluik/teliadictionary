import { IWordCreate } from '../domain/Words/IWordCreate';
import { IWord } from '../domain/Words/IWord';
import { IFetchResponse } from '@/types/IFetchResponse';
import { BaseService } from './BaseService';

export abstract class WordsService extends BaseService<IWord> {
    private static readonly url = "words"

    public static getWords(lang: string): Promise<IFetchResponse<IWord[]>> {
        return super.getAll(this.url + `/${lang}`);
    }

    public static deleteWord(term: string, lang: string): Promise<IFetchResponse<string>> {
        return super.delete(this.url + `/${lang}/${term}/`);
    }

    public static getWord(term: string, lang: string): Promise<IFetchResponse<IWord>> {
        return super.getOne(this.url + `/${lang}/` + term);
    }

    public static updateWord(term: string, lang: string, data: IWord): Promise<IFetchResponse<string>> {
        return super.update(this.url + `/${lang}/${term}`, data);
    }

    public static createWord(lang: string, data: IWordCreate[]): Promise<IFetchResponse<string>> {
        return super.create(this.url + `/${lang}/`, data);
    }

    public static getFuzzyWords(lang: string, term: string): Promise<IFetchResponse<IWord[]>> {
        return super.getAllFuzzy(this.url + `/${lang}/fuzzy/` + term);
    }
}
