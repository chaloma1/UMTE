package cz.uhk.chaloma1.pronouncecorrector.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int id_word;

    private String nazev;

    // pokud se slovo neda vyslovit
    private int rating;

    // pro nacteni tezkych slov
    private int ranking;


    public Word(String nazev) {
        this.nazev = nazev;

        this.rating = 1;
        this.ranking = 0;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getId_word() {
        return id_word;
    }

    public void setId_word(int id_word) {
        this.id_word = id_word;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
