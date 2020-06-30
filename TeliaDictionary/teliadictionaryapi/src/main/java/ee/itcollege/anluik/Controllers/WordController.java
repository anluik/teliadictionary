package ee.itcollege.anluik.Controllers;

import ee.itcollege.anluik.Domain.Translation;
import ee.itcollege.anluik.Domain.Word;
import ee.itcollege.anluik.Services.TranslationService;
import ee.itcollege.anluik.Services.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WordController {

    private final WordService wordService;
    private final TranslationService translationService;

    @Autowired
    public WordController(WordService wordService, TranslationService translationService) {
        this.wordService = wordService;
        this.translationService = translationService;
    }

    /**
     * Make a query for all words.
     *
     * @param lang - language of the words.
     * @return list of words.
     */
    @CrossOrigin
    @RequestMapping("/words/{lang}")
    public List<Word> getWords(@PathVariable String lang) {
        return this.wordService.getAll(lang);
    }

    /**
     * Make a query for fuzzy words for term.
     *
     * @param lang - language of the words.
     * @param term - the word fuzzy words are being looked for.
     * @return list of words.
     */
    @CrossOrigin
    @RequestMapping("/words/{lang}/fuzzy/{term}")
    public List<Word> getFuzzyWords(@PathVariable String lang, @PathVariable String term) {
        List<Word> fuzzy = this.getFuzzyTranslations(term, lang);
        for (Word word : fuzzy) {
            // System.out.println("'" + word.getTerm() + "' is a fuzzy word to '" + term + "'");
        }
        return fuzzy;
    }

    /**
     * Make a query for a specific word.
     *
     * @param lang - language of the word.
     * @param term - the word.
     * @return - word, if found.
     */
    @CrossOrigin
    @RequestMapping("/words/{lang}/{term}")
    public Word getWord(@PathVariable String lang, @PathVariable String term) {
        return findWord(lang, term);
    }

    /**
     * Add new word(s) to database.
     *
     * @param lang - language of the words.
     * @param words - word or list of words to be added.
     * @return response 201.
     */
    @CrossOrigin
    @RequestMapping(value = "/words/{lang}", method = RequestMethod.POST)
    public ResponseEntity createWord(@PathVariable String lang, @RequestBody List<Word> words) {
        this.wordService.create(words, lang);
        return new ResponseEntity<>("Word(s) created", HttpStatus.CREATED);
    }

    /**
     * Delete a word from database.
     *
     * @param lang - language of the word.
     * @param term - word to be deleted.
     * @return response 204.
     */
    @CrossOrigin
    @RequestMapping(value = "/words/{lang}/{term}", method = RequestMethod.DELETE)
    public ResponseEntity deleteWord(@PathVariable String lang, @PathVariable String term) {
        Word word = findWord(lang, term);

        // first delete all translations who are parent to the word.
        for (Translation translation: this.translationService.getAll(lang, term)) {
            this.translationService.delete(translation);
        }

        // second delete word.
        this.wordService.delete(word);

        return new ResponseEntity<>("Word deleted", HttpStatus.NO_CONTENT);
    }

    /**
     * Look for a word in database.
     *
     * @param lang - language of the word being looked for.
     * @param term - word being looked for.
     * @return - word, if found.
     * @throws ResponseStatusException - 404 Not Found, if term not in database.
     */
    private Word findWord(String lang, String term) throws ResponseStatusException {
        Word word = this.wordService.getOneByName(lang, term);
        if (word == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word not found!");

        return word;
    }

    /**
     * After user searches for a word in the dictionary, find similar words to user's search
     * that might be the actual words the user might have meant to search.
     *
     * @param term - search term user looked for.
     * @param lang - language of the search term.
     * @return list of fuzzy words.
     */
    private List<Word> getFuzzyTranslations(String term, String lang) {
        return this.wordService
                .getAll(lang)
                .stream()
                .filter(word -> checkLevenshteinDistanceRange(term, word))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to check whether distance between two words is 0 < n <= 2.
     * If true, the word is considered a fuzzy word to term.
     *
     * @param term - search term user looked for.
     * @param word - other word in dictionary being checked if it is similar to the searchWord.
     * @return boolean.
     */
    private boolean checkLevenshteinDistanceRange(String term, Word word) {
        int distance = this.getLevenshteinDistance(word.getTerm(), term);
        return distance > 0 && distance <= 2;
    }

    /**
     * Calculate Levenshtein's distance between two words.
     * The less operations (insertion, deletion, replacement) have to be made
     * to convert searchWord into word, the more similar the two words are.
     *
     * @param searchWord - word the user is searching for.
     * @param word - other word in dictionary being checked if it is similar to the searchWord.
     * @return int - how many operations had to be made.
     */
    private int getLevenshteinDistance(CharSequence searchWord, CharSequence word) {
        if (searchWord == null || word == null) {
            throw new IllegalArgumentException("Strings must not be null!");
        }

        int n = searchWord.length(); // length of s
        int m = word.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory.
            final CharSequence tmp = searchWord;
            searchWord = word;
            word = tmp;
            n = m;
            m = word.length();
        }

        int[] previous = new int[n + 1];
        int[] current = new int[n + 1];
        int[] p; // placeholder

        char t_index;

        int cost;

        for (int i = 0; i <= n; i++) {
            previous[i] = i;
        }

        for (int j = 1; j <= m; j++) {
            t_index = word.charAt(j - 1);
            current[0] = j;

            for (int i = 1; i <= n; i++) {
                cost = searchWord.charAt(i - 1) == t_index ? 0 : 1;
                current[i] = Math.min(Math.min(current[i - 1] + 1, previous[i] + 1), previous[i - 1] + cost);
            }

            p = previous;
            previous = current;
            current = p;
        }

        // System.out.println("Levenshtein's distance between " + searchWord.toString() + " and " + word.toString() + " is " + previous[n]);
        return previous[n];
    }
}