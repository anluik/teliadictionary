import { IWordCreate } from './../domain/Words/IWordCreate';
import { IFetchResponse } from '@/types/IFetchResponse';
import { BaseService } from './BaseService';
import { ITranslation } from '@/domain/Translations/ITranslation';

export abstract class TranslationsService extends BaseService<ITranslation> {
    private static readonly url = "translations"

    public static getTranslations(lang: string, term: string | null = null): Promise<IFetchResponse<ITranslation[]>> {
        return super.getAll(this.url + `/${lang}`, term);
    }

    public static deleteTranslation(id: string): Promise<IFetchResponse<string>> {
        return super.delete(this.url + "/" + id);
    }

    public static getTranslation(id: string): Promise<IFetchResponse<ITranslation>> {
        return super.getOne(this.url + "/all/" + id);
    }

    public static updateTranslation(id: string, data: ITranslation): Promise<IFetchResponse<string>> {
        return super.update(this.url + "/" + id, data);
    }

    public static createTranslation(lang: string, data: IWordCreate[]): Promise<IFetchResponse<string>> {
        return super.create(this.url + `/${lang}`, data);
    }
}
