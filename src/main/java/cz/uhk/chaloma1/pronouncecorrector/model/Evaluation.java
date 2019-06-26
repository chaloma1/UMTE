package cz.uhk.chaloma1.pronouncecorrector.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Evaluation {

    @PrimaryKey(autoGenerate = true)
    private int id_evaluation;

    private String datum;

    private String correctWords;

    private String wrongWords;

    private String ownerLogin;

    private float average;

    private int correctNumber;

    private int wrongNumber;


    public Evaluation(String ownerLogin) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy-MM-dd");


        this.datum = simpleDateFormat.format(new Date());
        this.ownerLogin = ownerLogin;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public void setCorrectWords(String correctWords) {
        this.correctWords = correctWords;
    }

    public void setWrongWords(String wrongWords) {
        this.wrongWords = wrongWords;
    }

    public int getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }

    public int getWrongNumber() {
        return wrongNumber;
    }

    public void setWrongNumber(int wrongNumber) {
        this.wrongNumber = wrongNumber;
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

    public String getCorrectWords() {
        return correctWords;
    }

    public void setCorrectWords(List<String> correctWords) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ", ";
        for(String word : correctWords){
            if (correctWords.indexOf(word) != 0) {
                stringBuilder.append(separator);
            }
            stringBuilder.append(word);
            correctWords.set(correctWords.indexOf(word), "");

        }
        this.correctWords = stringBuilder.toString();
    }

    public String getWrongWords() {
        return wrongWords;
    }

    public void setWrongWords(List<String> wrongWords) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ", ";
        for(String word : wrongWords){
            if (wrongWords.indexOf(word) != 0) {
                stringBuilder.append(separator);
            }
            stringBuilder.append(word);
            wrongWords.set(wrongWords.indexOf(word), "");


        }
        this.wrongWords = stringBuilder.toString();
    }
}
