package ee.itcollege.anluik.Domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="translating")
public class Translation {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne //(cascade = CascadeType.PERSIST)
    private Word firstWord;
    @ManyToOne //(cascade = CascadeType.PERSIST)
    private Word secondWord;

    public Translation() {}  // must have for deserialization

    public Translation(Word firstWord, Word secondWord) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
    }

    public UUID getId() {
        return this.id;
    }

    public Word getFirstWord() {
        return this.firstWord;
    }

    public void setFirstWord(Word firstWord) {
        this.firstWord = firstWord;
    }

    public Word getSecondWord() {
        return this.secondWord;
    }

    public void setSecondWord(Word secondWord) {
        this.secondWord = secondWord;
    }

    @Override
    public String toString() {
        return "Translation EST " + firstWord.getTerm() + " = ENG " + secondWord.getTerm();
    }
}
