package ee.itcollege.anluik.Services;

import ee.itcollege.anluik.Domain.Example;
import ee.itcollege.anluik.Repositories.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@Service
public class ExampleService {
    
    private final ExampleRepository exampleRepository;

    @Autowired
    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    /**
     * Get all examples from database.
     *
     * @return list of examples
     */
    public List<Example> getAll(String term, String lang) {
        return new ArrayList<>(this.exampleRepository.findByWordTermAndLanguage(term, lang));
    }


    /**
     * Get one specific example from database by id.
     *
     * @param id - id of the example being queried.
     * @return example, if found, otherwise null.
     */
    public Example getOne(UUID id) {
        return this.exampleRepository.findById(id).orElse(null);
    }

    /**
     * Add a new example to database.
     *
     * @param example - example to be created.
     */
    public void create(Example example) {
        this.exampleRepository.save(example);
    }

    /**
     * Update a example in database.
     *
     * @param example - data for update.
     */
    public void update(Example example) {
        this.exampleRepository.save(example);
    }

    /**
     * Delete a example in the database.
     *
     * @param example - example to be deleted.
     */
    public void delete(Example example) {
        this.exampleRepository.delete(example);
    }
}
