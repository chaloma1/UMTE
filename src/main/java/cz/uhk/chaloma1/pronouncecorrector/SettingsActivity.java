package cz.uhk.chaloma1.pronouncecorrector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cz.uhk.chaloma1.pronouncecorrector.Service.InitDB;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button buttonWordInit = findViewById(R.id.buttonWordInit);

        buttonWordInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWords();
            }
        });
    }

    public void initWords(){
        InitDB initDB = new InitDB(SettingsActivity.this);
        initDB.initWords();

    }
}
