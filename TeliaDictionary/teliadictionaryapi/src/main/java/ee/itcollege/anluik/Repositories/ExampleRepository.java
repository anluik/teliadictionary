package ee.itcollege.anluik.Repositories;

import ee.itcollege.anluik.Domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExampleRepository extends CrudRepository<Example, UUID> {
    List<Example> findByWordTerm(String term);
}
