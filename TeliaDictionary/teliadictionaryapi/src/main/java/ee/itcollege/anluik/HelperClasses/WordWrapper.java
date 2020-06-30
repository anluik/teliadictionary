package ee.itcollege.anluik.HelperClasses;

import ee.itcollege.anluik.Domain.Word;

import java.util.List;

public class WordWrapper {
    private List<Word> words;

    /**
     * @return list of words.
     */
    public List<Word> getWords() {
        return words;
    }

    /**
     * @param words - words for wrap.
     */
    public void setWords(List<Word> words) {
        this.words = words;
    }
}
