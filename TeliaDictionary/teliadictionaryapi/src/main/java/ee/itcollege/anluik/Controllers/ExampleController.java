package ee.itcollege.anluik.Controllers;

import ee.itcollege.anluik.Domain.Example;
import ee.itcollege.anluik.Domain.Word;
import ee.itcollege.anluik.Services.ExampleService;
import ee.itcollege.anluik.Services.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class ExampleController {

    private final ExampleService exampleService;
    private final WordService wordService;

    @Autowired
    public ExampleController(ExampleService exampleService, WordService wordService) {
        this.exampleService = exampleService;
        this.wordService = wordService;
    }

    /**
     * Make a query for all examples.
     *
     * @param lang - language of term the examples are being looked for.
     * @param term - term the examples are being looked for.
     * @return list of examples.
     */
    @CrossOrigin
    @RequestMapping("/words/{lang}/{term}/examples")
    public List<Example> getExamples(@PathVariable String lang, @PathVariable String term) {
        findWord(term, lang);
        return this.exampleService.getAll(term);
    }

    /**
     * Make a query for a specific example.
     *
     * @param lang - language of term the example is being looked for
     * @param id - id of the example.
     * @return - example, if found.
     */
    @CrossOrigin
    @RequestMapping("/words/{lang}/{term}/examples/{id}")
    public Example getExample(@PathVariable String lang, @PathVariable String term, @PathVariable UUID id) {
        findWord(term, lang);
        return this.findExample(id);
    }

    /**
     * Add a new example to database.
     *
     * @param lang - language of word the example is being create for.
     * @param example - example with example text and its translation.
     * @return response 201.
     */
    @CrossOrigin
    @RequestMapping(value = "/words/{lang}/{term}/examples", method = RequestMethod.POST)
    public ResponseEntity createExample(@PathVariable String lang, @PathVariable String term, @Valid @RequestBody Example example) {
        Word word = findWord(term, lang);
        Example newExample = new Example(example.getExampleText());
        newExample.setWord(word);
        this.exampleService.create(newExample);

        return new ResponseEntity<>("Example(s) created", HttpStatus.CREATED);
    }

    /**
     * Update a example's properties.
     *
     * @param lang - language of the example (and the word) that is being updated.
     * @param id - id of example to be updated.
     * @param example - request body.
     * @return response 204.
     */
    @CrossOrigin
    @RequestMapping(value = "/words/{lang}/{term}/examples/{id}", method = RequestMethod.PUT)
    public ResponseEntity putExample(@PathVariable String lang, @PathVariable String term, @PathVariable UUID id, @Valid @RequestBody Example example) {
        findWord(term, lang);
        Example oldExample = findExample(id);
        oldExample.setExampleText(example.getExampleText());
        this.exampleService.update(oldExample);

        return new ResponseEntity<>("Example updated", HttpStatus.NO_CONTENT);
    }

    /**
     * Delete a example from database.
     *
     * @param lang - language of the word and example.
     * @param id - id of the example to be deleted.
     * @return response 204.
     */
    @CrossOrigin
    @RequestMapping(value = "/words/{lang}/{term}/examples/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteExample(@PathVariable String lang, @PathVariable String term, @PathVariable UUID id) {
        findWord(term, lang); // check for word

        Example example = findExample(id);

        this.exampleService.delete(example);

        return new ResponseEntity<>("Example deleted", HttpStatus.NO_CONTENT);
    }

    /**
     * Look for a example in database.
     *
     * @param id - id of example being looked for.
     * @return - example, if found.
     * @throws ResponseStatusException - 404 Not Found, if example not in database.
     */
    private Example findExample(UUID id) throws ResponseStatusException {
        Example example = this.exampleService.getOne(id);
        if (example == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Example not found!");

        return example;
    }

    /**
     * Look for a translation in database.
     *
     * @param lang - language of the term being looked for.
     * @param term - term being looked for.
     * @return - word, if found.
     * @throws ResponseStatusException - 404 Not Found, if word not in database.
     */
    private Word findWord(String term, String lang) throws ResponseStatusException {
        Word word = this.wordService.getOneByName(lang, term);
        if (word == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word not found!");
        return word;
    }
}
