package cz.uhk.chaloma1.pronouncecorrector;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cz.uhk.chaloma1.pronouncecorrector.Service.InitDB;
import cz.uhk.chaloma1.pronouncecorrector.dao.EvaluationDao;
import cz.uhk.chaloma1.pronouncecorrector.dao.WordDao;
import cz.uhk.chaloma1.pronouncecorrector.database.AppRoomDatabase;
import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;
import cz.uhk.chaloma1.pronouncecorrector.model.Word;

public class AndroidTTS extends AppCompatActivity {

    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;


    private TextView textViewGenerated;
    private ImageButton imageButtonHint;
    private Button buttonNext;
    private ProgressBar progressBarTraining;
    private TextView textViewProgress;

    private Evaluation evaluation;
    private List<String> correctWords;
    private List<String> wrongWords;

    private boolean zapocitaneSlovo;

    private WordDao wordDao;
    private EvaluationDao evaluationDao;

    private List<Word> allWords;

    private Random randomInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        textViewGenerated = findViewById(R.id.textViewGenerated);
        //textViewGenerated.setText("Hi there!");

        imageButtonHint = findViewById(R.id.imageButtonHint);
        buttonNext = findViewById(R.id.buttonNext);
        progressBarTraining = findViewById(R.id.progressBarTraining);

        progressBarTraining.setMax(Integer.valueOf(sharedPreferences.getString("practiseLength", "10")));

        textViewProgress = findViewById(R.id.textViewProgress);
        textViewProgress.setText(progressBarTraining.getProgress() + " " + "/" + " " + progressBarTraining.getMax());

        randomInt = new Random();
        zapocitaneSlovo = false;

        requestAudioPermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked");
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

                mySpeechRecognizer.startListening(intent);
                Toast.makeText(AndroidTTS.this, "recording", Toast.LENGTH_SHORT).show();



            }
        });

        imageButtonHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak(textViewGenerated.getText().toString());
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (progressBarTraining.getProgress() < progressBarTraining.getMax()) {

                    try {
                        generateWord();
                        int progress = progressBarTraining.getProgress();
                        progressBarTraining.setProgress(progress + 1);
                        textViewProgress.setText(progressBarTraining.getProgress() + " " + "/" + " " + progressBarTraining.getMax());
                        zapocitaneSlovo = false;
                    } catch (Exception e) {
                        System.out.println("chyba v nacteni");
                    }
                }else {
                    evaluation.setCorrectWords(correctWords);
                    evaluation.setWrongWords(wrongWords);
                    evaluation.setCorrectNumber(correctWords.size());
                    evaluation.setWrongNumber(wrongWords.size());
                    // pridej evaluation
                    evaluationDao.insertEvaluation(evaluation);
                    Toast.makeText(AndroidTTS.this, "Vyhodnoceni dokonceno", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AndroidTTS.this, HomeActivity.class));
                    finish();
                }

            }
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();

        AppRoomDatabase database = AppRoomDatabase.getDatabase(this);

        evaluationDao = database.getEvaluationDao();

        wordDao = database.getWordDao();
        allWords = wordDao.getAll();
        if(allWords.size() == 0){
            InitDB initDB = new InitDB(AndroidTTS.this);
            initDB.initWords();
            allWords = wordDao.getAll();
        }

        evaluation = new Evaluation(sharedPreferences.getString("loginTemp", "Error"));

        correctWords = new ArrayList<>();

        wrongWords = new ArrayList<>();

        List<Evaluation> evaluations = evaluationDao.getUsersEvaluation(sharedPreferences.getString("loginTemp", ""));

        System.out.println("evaluations + " + evaluations.size());
        if (evaluations.size() != 0) {
            for (int i = 0; i <= evaluations.size()-1; i++) {
                System.out.println("evaluations datum + " + evaluations.get(i).getDatum());
                System.out.println("evaluations wrong words + " + evaluations.get(i).getWrongWords());
                System.out.println("evaluations wrong + " + evaluations.get(i).getWrongNumber());
                System.out.println("evaluations owner + " + evaluations.get(i).getOwnerLogin());
            }
        }


        generateWord();
    }

    private void generateWord(){
        int i = randomInt.nextInt(allWords.size());


        textViewGenerated.setText(allWords.get(i).getNazev());
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {
                    System.out.println("AudioBuffer" + bytes.toString());

                }

                @Override
                public void onEndOfSpeech() {


                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {

                    List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    System.out.println("onResults" + results);
                    processResult(results.get(0));
                    Toast.makeText(AndroidTTS.this, results.get(0), Toast.LENGTH_SHORT).show();
                    compareWord(results.get(0));

                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    private void compareWord(String spokenWord){
        spokenWord = spokenWord.toLowerCase();
        String generatedWord = textViewGenerated.getText().toString().toLowerCase();


        if(spokenWord.equals(generatedWord)){
            correctWords.add(generatedWord);
            Toast.makeText(AndroidTTS.this, "Correct!", Toast.LENGTH_SHORT).show();


        }else{
            wrongWords.add(generatedWord);
            Toast.makeText(AndroidTTS.this, "Wrong! You said " + " " + spokenWord, Toast.LENGTH_SHORT).show();

        }

        zapocitaneSlovo = true;
    }

    private void processResult(String command) {
        command = command.toLowerCase();

        System.out.println("Command" + command);

        if (command.indexOf("what") != -1){
            System.out.println("what");
            if (command.indexOf("your name") != -1){
                System.out.println("your name");
                speak("my name is Martin");
            }
            if (command.indexOf("time") != -1){
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("the time is" + time);
            }
        }
    }

    private void initializeTextToSpeech(){
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (myTTS.getEngines().size() == 0){
                    Toast.makeText(AndroidTTS.this, "There is no tts engine on your device", Toast.LENGTH_LONG).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_android_tt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
        mySpeechRecognizer.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
        mySpeechRecognizer.destroy();
    }

    private void requestAudioPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_RECORD_AUDIO:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initializeTextToSpeech();
                }else {
                    Toast.makeText(this, "Permissions denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
