package cz.uhk.chaloma1.pronouncecorrector.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id_user;

    private String login;

    private String passsword;


    public User(String login, String passsword) {
        this.login = login;
        this.passsword = passsword;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }
}
