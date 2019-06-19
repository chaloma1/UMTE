package cz.uhk.chaloma1.pronouncecorrector.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.model.Word;

@Dao
public interface WordDao {

    @Insert
    void insertWord(Word word);

    @Query("SELECT * FROM word WHERE id_word = :id")
    Word findWordById(int id);

    @Query("DELETE FROM word")
    void deleteAll();


    @Query("SELECT * FROM word")
    List<Word> getAll();
}
