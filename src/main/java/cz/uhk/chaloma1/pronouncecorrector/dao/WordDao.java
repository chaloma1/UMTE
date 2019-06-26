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

    @Query("UPDATE word set rating = 0 WHERE nazev = :nazev")
    void updateWordRating(String nazev);


    @Query("SELECT * FROM word where rating > 0")
    List<Word> getAll();

    @Query("UPDATE word set ranking = ranking + :increment where nazev LIKE :nazev")
    void updateWordRank(String nazev, int increment);

    @Query("SELECT * FROM (SELECT * FROM word WHERE rating > 0 ORDER BY ranking ASC) LIMIT 4")
    List<Word> getLeastRankingWords();
}
