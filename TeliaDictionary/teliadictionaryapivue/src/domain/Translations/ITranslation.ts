import { IWord } from '../Words/IWord';

export interface ITranslation {

    id: string;
    firstWord: IWord;
    secondWord: IWord;
}
