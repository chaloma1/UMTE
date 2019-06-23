package cz.uhk.chaloma1.pronouncecorrector;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EvaluationActivity extends AppCompatActivity {

    TextView textViewAverage, textViewCorrect, textViewWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        textViewAverage = findViewById(R.id.textViewAverage);
        textViewCorrect = findViewById(R.id.textViewCorrect);
        textViewWrong = findViewById(R.id.textViewWrong);


        float average = getIntent().getFloatExtra("average", -100);
        textViewAverage.setText(String.format("%.2f",average) + "%");
        if(average > 50){
            textViewAverage.setTextColor(Color.GREEN);
        }else {
            textViewAverage.setTextColor(Color.RED);
        }

        textViewCorrect.setText("Správná slova: " + getIntent().getStringExtra("correct"));
        textViewWrong.setText("Špatná slova: " + getIntent().getStringExtra("wrong"));

        Button buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EvaluationActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
