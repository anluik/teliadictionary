package ee.itcollege.anluik.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "example")
public class Example {

    @Id
    @GeneratedValue
    private UUID id;
    @Size(min = 1, max = 256)
    private String exampleText;
    private String language;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="word_id")
    private Word word;

    public Example() {} // must have for deserialization

    public Example(String exampleText, String language) {
        this.id = UUID.randomUUID();
        this.exampleText = exampleText;
        this.language = language;
    }

    public UUID getId() {
        return this.id;
    }

    public String getExampleText() {
        return this.exampleText;
    }

    public void setExampleText(String exampleText) {
        this.exampleText = exampleText;
    }

    public Word getWord() {
        return this.word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return this.exampleText;
    }
}
