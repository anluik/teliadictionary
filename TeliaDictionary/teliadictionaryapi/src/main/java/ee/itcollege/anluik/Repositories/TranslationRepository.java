package ee.itcollege.anluik.Repositories;

import ee.itcollege.anluik.Domain.Translation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TranslationRepository extends CrudRepository<Translation, UUID> {
    Translation findByFirstWord_TermAndSecondWord_Term(String firstWord, String secondWord);

}
