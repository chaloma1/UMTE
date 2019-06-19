package cz.uhk.chaloma1.pronouncecorrector.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int id_word;

    private String nazev;

    private int rating;


    public Word(String nazev) {
        this.nazev = nazev;

        this.rating = 1;
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
