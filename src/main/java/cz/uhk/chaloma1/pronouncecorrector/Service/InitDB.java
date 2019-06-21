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
        }catch (Exception e){
            System.out.println("Chyba v nacteni slov");


        }


    }
}
