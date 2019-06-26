package cz.uhk.chaloma1.pronouncecorrector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import cz.uhk.chaloma1.pronouncecorrector.Service.InitDB;
import cz.uhk.chaloma1.pronouncecorrector.Service.InputFilterMinMax;

public class SettingsActivity extends AppCompatActivity {

    Spinner spinnerPractiseLength, spinnerListeningSpeed;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        spinnerPractiseLength = findViewById(R.id.spinnerPractiseLength);
        ArrayAdapter<String> practiseLengthAdapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.practiseLength));
        practiseLengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerListeningSpeed = findViewById(R.id.spinnerListeningSpeed);
        ArrayAdapter<String> listeningSpeedAdapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.listeningSpeed));
        listeningSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPractiseLength.setAdapter(practiseLengthAdapter);
        spinnerListeningSpeed.setAdapter(listeningSpeedAdapter);


        Button buttonWordInit = findViewById(R.id.buttonWordInit);


        buttonWordInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWords();
                Toast.makeText(SettingsActivity.this, "Slova a hodnoceni resetovana.", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonSaveSettings = findViewById(R.id.buttonSaveSettings);

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesEditor.putString("practiseLength", spinnerPractiseLength.getSelectedItem().toString());
                sharedPreferencesEditor.commit();

                sharedPreferencesEditor.putString("listeningSpeed", spinnerListeningSpeed.getSelectedItem().toString());
                sharedPreferencesEditor.commit();

                Toast.makeText(SettingsActivity.this, "Nastaveni ulozeno", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                finish();
            }
        });

        int spinnerPositionPractiseLenght = practiseLengthAdapter.getPosition(sharedPreferences.getString("practiseLength", "10"));
        spinnerPractiseLength.setSelection(spinnerPositionPractiseLenght);

        int spinnerPositionListeningSpeed = listeningSpeedAdapter.getPosition(sharedPreferences.getString("listeningSpeed", "2500"));
        spinnerListeningSpeed.setSelection(spinnerPositionListeningSpeed);

    }

    public void initWords(){
        InitDB initDB = new InitDB(SettingsActivity.this);
        initDB.initWords();

    }
}
