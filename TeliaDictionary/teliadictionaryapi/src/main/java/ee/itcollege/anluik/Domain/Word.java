package ee.itcollege.anluik.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ee.itcollege.anluik.HelperClasses.ToLowerCaseDeserializer;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="word")
public class Word {

    @Id
    @GeneratedValue
    private UUID id;
    private String language;
    @Size(min = 1, max = 32)
    @JsonDeserialize(using = ToLowerCaseDeserializer.class)
    private String term;
    @JsonManagedReference
    @OneToMany(mappedBy = "word", orphanRemoval = true)
    private List<Example> examples = new ArrayList<>();

    public Word(String term, String language) {
        this.term = term;
        this.language = language;
    }

    public Word() {} // must have for deserialization

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public UUID getId() {
        return this.id;
    }

    public List<Example> getExamples() {
        return this.examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return this.term;
    }
}
