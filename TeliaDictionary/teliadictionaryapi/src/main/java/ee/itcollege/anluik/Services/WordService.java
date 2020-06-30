package ee.itcollege.anluik.Services;

import ee.itcollege.anluik.Domain.Word;
import ee.itcollege.anluik.Repositories.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WordService {

    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    /**
     * Get all words from database.
     *
     * @return list of words
     */
    public List<Word> getAll(String lang) {
        List<Word> words = new ArrayList<>();
        this.wordRepository.findAll().forEach(words::add);

        if (lang != null) {
            words = words.stream().filter(word -> word.getLanguage().equals(lang)).collect(Collectors.toList());
        }

        return words;
    }

    /**
     * Get one specific word from database by id.
     *
     * @param id - id of the word being queried.
     * @return word, if found, otherwise null.
     */
    public Word getOneById(UUID id) {
        return this.wordRepository.findById(id).orElse(null);
    }

    /**
     * Get one specific word from database by id.
     *
     * @param lang - language of the word being queried.
     * @param term - word being queried.
     * @return word, if found, otherwise null.
     */
    public Word getOneByName(String lang, String term) {
        return this.wordRepository.findByLanguageAndTerm(lang, term);
    }

    /**
     * Add a new word to database.
     *
     * @param lang - language of all the words being added.
     * @param words - list of words to be created.
     */
    public void create(List<Word> words, String lang) {
        words.forEach(newWord -> {
            try {
                if (this.getOneByName(lang, newWord.getTerm()) != null) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Caught error in WordService create - term '%s' already exists!", newWord.getTerm()));
                }
                this.wordRepository.save(new Word(newWord.getTerm(), lang));
            } catch (ResponseStatusException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    /**
     * Update a word in database.
     *
     * @param word - data for update.
     */
    public void update(Word word) {
        this.wordRepository.save(word);
    }

    /**
     * Delete a word in the database.
     *
     * @param word - word to be deleted.
     */
    public void delete(Word word) {
        this.wordRepository.delete(word);
    }
}
