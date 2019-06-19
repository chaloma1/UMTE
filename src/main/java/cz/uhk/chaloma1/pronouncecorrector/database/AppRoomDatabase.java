package cz.uhk.chaloma1.pronouncecorrector.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import cz.uhk.chaloma1.pronouncecorrector.dao.EvaluationDao;
import cz.uhk.chaloma1.pronouncecorrector.dao.UserDao;
import cz.uhk.chaloma1.pronouncecorrector.dao.WordDao;
import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;
import cz.uhk.chaloma1.pronouncecorrector.model.User;
import cz.uhk.chaloma1.pronouncecorrector.model.Word;

@Database(entities = {User.class, Word.class, Evaluation.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {


    public abstract UserDao getUserDao();

    public abstract WordDao getWordDao();

    public abstract EvaluationDao getEvaluationDao();

    private static volatile AppRoomDatabase appRoomInstance;

    public static AppRoomDatabase getDatabase(final Context context){
        if(appRoomInstance == null){
            synchronized (AppRoomDatabase.class){
                if (appRoomInstance == null){
                    appRoomInstance = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "myDB").allowMainThreadQueries().build();

                }
            }
        }
        return appRoomInstance;
    }

}
