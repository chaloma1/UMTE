package cz.uhk.chaloma1.pronouncecorrector.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.model.User;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM user WHERE login LIKE :login")
    List<User> findUserByLogin(String login);
}
