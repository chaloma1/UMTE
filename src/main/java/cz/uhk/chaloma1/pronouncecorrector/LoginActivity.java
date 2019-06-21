package cz.uhk.chaloma1.pronouncecorrector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.dao.UserDao;
import cz.uhk.chaloma1.pronouncecorrector.database.AppRoomDatabase;
import cz.uhk.chaloma1.pronouncecorrector.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText editTextLogin, editTextPassword;
    CheckBox checkBoxLogin;
    AppRoomDatabase database;
    UserDao userDao;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        String login = sharedPreferences.getString("login", "empty");
        String password = sharedPreferences.getString("password", "empty");
        String checkBox = sharedPreferences.getString("checkBoxLogin", "false");

        if (!login.equals("empty") && !password.equals("empty")){
            editTextLogin.setText(login);
            editTextPassword.setText(password);
        }

        checkBoxLogin = findViewById(R.id.checkBoxLogin);

        if (checkBox.equals("true")){
            checkBoxLogin.setChecked(true);

        }else{
            checkBoxLogin.setChecked(false);
        }




        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        database = AppRoomDatabase.getDatabase(this);
        userDao = database.getUserDao();



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextLogin.getText() != null){
                    if (editTextLogin.getText().toString().trim().isEmpty() == false){
                        if (editTextPassword.getText() != null){
                            if (editTextPassword.getText().toString().trim().isEmpty() == false){
                                try{
                                    List<User> user = userDao.findUserByLogin(editTextLogin.getText().toString());
                                    System.out.println("user size" + user.size());
                                if (user.size() == 0) {
                                    userDao.insertUser(new User(editTextLogin.getText().toString(), editTextPassword.getText().toString()));
                                    Toast.makeText(LoginActivity.this, "Registrace uspesna", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(LoginActivity.this, "Login je jiz pouzity", Toast.LENGTH_SHORT).show();
                                }
                                }catch (Exception e){
                                    Toast.makeText(LoginActivity.this, "Neslo vlozit uzivatele", Toast.LENGTH_SHORT).show();
                                    System.out.println(e.getMessage());
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Heslo nesmi byt prazdne", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Heslo nesmi byt null", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "Login nesmi byt prazdny", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Login nesmi byt null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    User u = userDao.findUserByLogin(editTextLogin.getText().toString()).get(0);
                    System.out.println("uzivatle nalezen");
                    if (u.getPasssword().equals(editTextPassword.getText().toString())){
                        System.out.println("heslo sedi");



                        if (checkBoxLogin.isChecked()){
                            sharedPreferencesEditor.putString("login", editTextLogin.getText().toString());
                            sharedPreferencesEditor.putString("password", editTextPassword.getText().toString());
                            sharedPreferencesEditor.putString("checkBoxLogin", "true");
                            sharedPreferencesEditor.commit();
                        }else {
                            sharedPreferencesEditor.remove("login");
                            sharedPreferencesEditor.remove("password");
                            sharedPreferencesEditor.remove("checkBoxLogin");
                            sharedPreferencesEditor.commit();
                        }

                        sharedPreferencesEditor.putString("loginTemp", editTextLogin.getText().toString());
                        sharedPreferencesEditor.commit();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Heslo neni spravne", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                        Toast.makeText(LoginActivity.this, "Login neni spravny", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
