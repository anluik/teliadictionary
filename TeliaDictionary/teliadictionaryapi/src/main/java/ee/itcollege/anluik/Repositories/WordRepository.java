package ee.itcollege.anluik.Repositories;

import ee.itcollege.anluik.Domain.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WordRepository extends CrudRepository<Word, UUID> {

    Word findByLanguageAndTerm(String lang, String term);
}
