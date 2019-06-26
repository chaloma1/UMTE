package cz.uhk.chaloma1.pronouncecorrector.Service;

import android.content.Context;

import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.dao.EvaluationDao;
import cz.uhk.chaloma1.pronouncecorrector.dao.WordDao;
import cz.uhk.chaloma1.pronouncecorrector.database.AppRoomDatabase;
import cz.uhk.chaloma1.pronouncecorrector.model.Word;

public class InitDB {

    AppRoomDatabase database;
    WordDao wordDao;
    EvaluationDao evaluationDao;

    public InitDB(Context context){
        database = AppRoomDatabase.getDatabase(context);
        wordDao = database.getWordDao();
        evaluationDao = database.getEvaluationDao();
    }

    public void initWords(){
        try {
            evaluationDao.deleteAll();
            wordDao.deleteAll();
            wordDao.insertWord(new Word("Hello"));
            wordDao.insertWord(new Word("Goodbye"));
            wordDao.insertWord(new Word("Welcome"));
            wordDao.insertWord(new Word("Pronounce"));
            wordDao.insertWord(new Word("Dog"));
            wordDao.insertWord(new Word("Cat"));
            wordDao.insertWord(new Word("Evening"));
            wordDao.insertWord(new Word("Car"));
            wordDao.insertWord(new Word("Bus"));
            wordDao.insertWord(new Word("Hamster"));
            wordDao.insertWord(new Word("Morning"));
            wordDao.insertWord(new Word("Absent"));
            wordDao.insertWord(new Word("Absolutely"));
            wordDao.insertWord(new Word("Accept"));
            wordDao.insertWord(new Word("Accident"));
            wordDao.insertWord(new Word("Adult"));
            wordDao.insertWord(new Word("View"));
            wordDao.insertWord(new Word("Aggressive"));
            wordDao.insertWord(new Word("Amazing"));
            wordDao.insertWord(new Word("Anniversary"));
            wordDao.insertWord(new Word("Appearance"));
            wordDao.insertWord(new Word("Apply"));
            wordDao.insertWord(new Word("Awkward"));
            wordDao.insertWord(new Word("Bargain"));
            wordDao.insertWord(new Word("Certainly"));
            wordDao.insertWord(new Word("Colleague"));
            wordDao.insertWord(new Word("Common"));
            wordDao.insertWord(new Word("Condition"));
            wordDao.insertWord(new Word("Cottage"));
            wordDao.insertWord(new Word("Cough"));
            wordDao.insertWord(new Word("Dangerous"));
            wordDao.insertWord(new Word("Dear"));
            wordDao.insertWord(new Word("Decay"));

        }catch (Exception e){
            System.out.println("Chyba v nacteni slov");


        }


    }
}
