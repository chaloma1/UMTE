package cz.uhk.chaloma1.pronouncecorrector.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;

@Dao
public interface EvaluationDao {

    @Insert
    void insertEvaluation(Evaluation evaluation);

    @Query("SELECT * FROM Evaluation WHERE ownerLogin = :login ORDER BY DATE(datum) DESC")
    List<Evaluation> getUsersEvaluation(String login);


    //List<Evaluation> getUserGraphEvaluation(String login);

    @Query("DELETE FROM Evaluation")
    void deleteAll();
}
