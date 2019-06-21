package cz.uhk.chaloma1.pronouncecorrector;

import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import cz.uhk.chaloma1.pronouncecorrector.Service.InitDB;
import cz.uhk.chaloma1.pronouncecorrector.dao.WordDao;
import cz.uhk.chaloma1.pronouncecorrector.database.AppRoomDatabase;
import cz.uhk.chaloma1.pronouncecorrector.model.Word;

public class ListeningActivity extends AppCompatActivity {

    TextView textViewListening;

    private TextToSpeech myTTS;

    int counter = 0;

    Random randomInt;

    WordDao wordDao;

    List<Word> allWords;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {



            if(counter == 0){
                counter = 1;
                generateWord();


            }else{
                counter++;

                speak(textViewListening.getText().toString());
                if (counter > 2){
                    counter = 0;
                }


            }




            timerHandler.postDelayed(this, 2500);

        }
    };

    private void generateWord(){
        int i = randomInt.nextInt(allWords.size());

        textViewListening.setText(allWords.get(i).getNazev());

    }

    private void initializeTextToSpeech(){
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (myTTS.getEngines().size() == 0){
                    Toast.makeText(ListeningActivity.this, "There is no tts engine on your device", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    myTTS.setLanguage(Locale.US);
                    //speak("Hello! I am ready.");
                }
            }
        });
    }

    private void speak(String message){
        if (Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        textViewListening = findViewById(R.id.textViewListening);

        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter = 0;
                timerHandler.postDelayed(timerRunnable, 0);

            }
        });

        Button buttonStop = findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timerHandler.removeCallbacks(timerRunnable);
                finish();

            }
        });

        randomInt = new Random();

        AppRoomDatabase database = AppRoomDatabase.getDatabase(this);
        wordDao = database.getWordDao();
        allWords = wordDao.getAll();

        if(allWords.size() == 0){
            InitDB initDB = new InitDB(this);
            initDB.initWords();
            allWords = wordDao.getAll();
        }
        initializeTextToSpeech();

    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        myTTS.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
        myTTS.shutdown();
    }
}
