package cz.uhk.chaloma1.pronouncecorrector.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Evaluation {

    @PrimaryKey(autoGenerate = true)
    private int id_evaluation;

    private String datum;

    private int correctWords;

    private int wrongWords;

    private String ownerLogin;


    public Evaluation(String datum, int correctWords, int wrongWords, String ownerLogin) {
        this.datum = datum;
        this.correctWords = correctWords;
        this.wrongWords = wrongWords;
        this.ownerLogin = ownerLogin;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public int getId_evaluation() {
        return id_evaluation;
    }

    public void setId_evaluation(int id_evaluation) {
        this.id_evaluation = id_evaluation;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getCorrectWords() {
        return correctWords;
    }

    public void setCorrectWords(int correctWords) {
        this.correctWords = correctWords;
    }

    public int getWrongWords() {
        return wrongWords;
    }

    public void setWrongWords(int wrongWords) {
        this.wrongWords = wrongWords;
    }
}
