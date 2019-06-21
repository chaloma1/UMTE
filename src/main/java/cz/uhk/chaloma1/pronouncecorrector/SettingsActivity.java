package cz.uhk.chaloma1.pronouncecorrector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cz.uhk.chaloma1.pronouncecorrector.Service.InitDB;
import cz.uhk.chaloma1.pronouncecorrector.Service.InputFilterMinMax;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextPraciseLength;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        editTextPraciseLength = findViewById(R.id.editTextPractiseLength);
        //editTextPraciseLength.setFilters(new InputFilter[]{new InputFilterMinMax("1", "20")});

        Button buttonWordInit = findViewById(R.id.buttonWordInit);

        buttonWordInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWords();
            }
        });

        Button buttonSaveSettings = findViewById(R.id.buttonSaveSettings);

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesEditor.putString("practiseLength", editTextPraciseLength.getText().toString());
                sharedPreferencesEditor.commit();
                Toast.makeText(SettingsActivity.this, "Nastaveni ulozeno", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                finish();
            }
        });

        editTextPraciseLength.setText(sharedPreferences.getString("practiseLength", "10"));
    }

    public void initWords(){
        InitDB initDB = new InitDB(SettingsActivity.this);
        initDB.initWords();

    }
}
