package ee.itcollege.anluik.Services;

import ee.itcollege.anluik.Domain.Translation;
import ee.itcollege.anluik.Domain.Word;
import ee.itcollege.anluik.Repositories.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    private final TranslationRepository translationRepository;

    @Autowired
    public TranslationService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    /**
     * Get all translations from database by language.
     *
     * If not sorted by language, some queries filtered by term, i.e 'suits',
     * can return false (wrong language) translations.
     *
     * @param term - optional parameter for filtering results by term.
     * @return list of words.
     */
    public List<Translation> getAll(String lang, String term) {
        List<Translation> translations = new ArrayList<>();
        this.translationRepository.findAll().forEach(translations::add);

        if (term != null) {
            return translations
                    .stream()
                    .filter(translation -> lang.equals("est") ?
                            translation.getFirstWord().getTerm().equals(term) // first word is always estonian
                            : translation.getSecondWord().getTerm().equals(term)) // second word is always english
                    .collect(Collectors.toList());
        }

        return translations;
    }

    /**
     * Get one specific translation from database by id.
     *
     * @param id - id of translation being queried.
     * @return translation, if found, otherwise null.
     */
    public Translation getOne(UUID id) {
        return this.translationRepository.findById(id).orElse(null);
    }

    /**
     * Add a new translation to database.
     *
     * @param estWord - estonian word.
     * @param engWord - english word.
     */
    public void create(Word estWord, Word engWord) {
        if (this.translationRepository.findByFirstWord_TermAndSecondWord_Term(estWord.getTerm(), engWord.getTerm()) != null) {
            System.out.println(String.format("Translation <%s, %s> aborted as the resource already exists.", estWord, engWord));
        } else {
            Translation translation = new Translation(estWord, engWord);
            this.translationRepository.save(translation);
        }
    }

    /**
     * Find and update a translation entry in database.
     *
     * @param translation - updated translation.
     */
    public void update(Translation translation) {
        this.translationRepository.save(translation);
    }

    /**
     * Find and delete a translation entry in the database.
     *
     * @param translation - translation entry to be deleted.
     */
    public void delete(Translation translation) {
        this.translationRepository.delete(translation);
    }
}
