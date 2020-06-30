package ee.itcollege.anluik.Controllers;

import ee.itcollege.anluik.Services.WordService;
import ee.itcollege.anluik.Domain.Translation;
import ee.itcollege.anluik.Domain.Word;
import ee.itcollege.anluik.Services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
public class TranslationController {

    private final TranslationService translationService;
    private final WordService wordService;

    @Autowired
    public TranslationController(TranslationService translationService, WordService wordService) {
        this.translationService = translationService;
        this.wordService = wordService;

    }

    /**
     * Make a query for all translation.
     * Query returns results for one language at once.
     *
     * @param lang - language the translations are queried for (est or eng).
     * @param term - optional parameter to get all translations for provided term.
     * @return list of translations.
     */
    @CrossOrigin
    @RequestMapping(value = "/translations/{lang}", method = RequestMethod.GET)
    public List<Translation> getTranslations(@PathVariable String lang, @RequestParam(value = "term", required = false) String term) {
        return this.translationService.getAll(lang, term);
    }
    /**
     * Make a query for a specific translation.
     *
     * @param id - id of the translation.
     * @return translation, if found.
     */
    @CrossOrigin
    @RequestMapping("/translations/all/{id}")
    public Translation getTranslation(@PathVariable UUID id) {
        return findTranslation(id);
    }

    /**
     * Add a new translation(s).
     *
     * First word in the list is always the word to be translated.
     * Remaining words are all possible translations.
     *
     * @param words - list of words of which id-s are used to create translations.
     * @return response 201.
     */
    @CrossOrigin
    @RequestMapping(value = "/translations/{lang}", method = RequestMethod.POST)
    public ResponseEntity createTranslation(@PathVariable String lang, @RequestBody List<Word> words) {
        // Word being translated is always the first word in the words list.
        Word subject = this.wordService.getOneByName(lang, words.get(0).getTerm());
        if (subject == null) throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot create translation before the words have been created.");
        // Pair first word with every remaining word in words list to create a translation.
        words.subList(1, words.size()).forEach(word -> {
            word = this.wordService.getOneByName(lang.equals("est") ? "eng" : "est", word.getTerm());
            if (word == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create translation before the words have been created.");
            }
            if (lang.equals("est")) {
                // if word is in estonian, always place it as firstWord in translation.
                this.translationService.create(subject, word);
            } else {
                // if word is in english, always place it as secondWord in translation.
                this.translationService.create(word, subject);
            }
        });

        return new ResponseEntity<>("Translation(s) created", HttpStatus.CREATED);
    }

    /**
     * Update a translation's properties.
     *
     * @param id - id of the translation to be updated.
     * @param translation - request body.
     * @return response 204.
     */
    @CrossOrigin
    @RequestMapping(value = "/translations/{id}", method = RequestMethod.PUT)
    public ResponseEntity putTranslation(@PathVariable UUID id, @RequestBody Translation translation) {
        Translation oldTranslation = findTranslation(id);

        // Finding out if replacement word(s) already exist
        Word firstWord = this.wordService.getOneById(translation.getFirstWord().getId());
        Word secondWord = this.wordService.getOneById(translation.getSecondWord().getId());

        // if replacement word does not exist, create new word. First word is always Estonian, second English.
        oldTranslation.setFirstWord(firstWord == null ? new Word(translation.getFirstWord().getTerm(), "est") : firstWord);
        oldTranslation.setSecondWord(secondWord == null ? new Word(translation.getSecondWord().getTerm(), "eng") : secondWord);

        this.translationService.update(oldTranslation);

        return new ResponseEntity<>("Translation updated", HttpStatus.NO_CONTENT);
    }

    /**
     * Delete a translation from database.
     *
     * @param id - id of the translation to be deleted.
     * @return response 204.
     */
    @CrossOrigin
    @RequestMapping(value = "/translations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTranslation(@PathVariable UUID id) {
        Translation translation = findTranslation(id);

        this.translationService.delete(translation);
        return new ResponseEntity<>("Translation deleted", HttpStatus.NO_CONTENT);
    }

    /**
     * Look for a translation in database.
     *
     * @param id - id of translation being looked for.
     * @return - translation, if found.
     * @throws ResponseStatusException - 404 Not Found, if translation not in database.
     */
    private Translation findTranslation(UUID id) throws ResponseStatusException {
        Translation translation = this.translationService.getOne(id);
        if (translation == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Translation not found!");

        return translation;
    }
}
