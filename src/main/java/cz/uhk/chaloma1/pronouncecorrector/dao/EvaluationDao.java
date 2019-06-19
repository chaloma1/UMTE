package cz.uhk.chaloma1.pronouncecorrector.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;

@Dao
public interface EvaluationDao {

    @Insert
    void insertEvaluation(Evaluation evaluation);
}
