import { IExampleEdit } from './../domain/Examples/IExampleEdit';
import { IExampleCreate } from './../domain/Examples/IExampleCreate';
import { IFetchResponse } from '@/types/IFetchResponse';
import { BaseService } from './BaseService';
import { IExample } from '@/domain/Examples/IExample';

export abstract class ExamplesService extends BaseService<IExample> {
    private static readonly url = "words/"

    public static getExamples(lang: string, term: string): Promise<IFetchResponse<IExample[]>> {
        return super.getAll(this.url + lang + "/" + term + "/examples");
    }

    public static deleteExample(id: string, lang: string, term: string): Promise<IFetchResponse<string>> {
        return super.delete(this.url + lang + "/" + term + "/examples/" + id);
    }

    public static getExample(id: string, lang: string, term: string): Promise<IFetchResponse<IExample>> {
        return super.getOne(this.url + lang + "/" + term + "/examples/" + id);
    }

    public static updateExample(id: string, data: IExampleEdit, lang: string, term: string): Promise<IFetchResponse<string>> {
        return super.update(this.url + lang + "/" + term + "/examples/" + id, data);
    }

    public static createExample(data: IExampleCreate, lang: string, term: string): Promise<IFetchResponse<string>> {
        return super.create(this.url + lang + "/" + term + "/examples", data);
    }
}
