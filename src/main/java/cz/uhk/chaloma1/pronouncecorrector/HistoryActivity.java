package cz.uhk.chaloma1.pronouncecorrector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.Service.RecycleViewAdapter;
import cz.uhk.chaloma1.pronouncecorrector.dao.EvaluationDao;
import cz.uhk.chaloma1.pronouncecorrector.database.AppRoomDatabase;
import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;

public class HistoryActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleAdapter;

    private AppRoomDatabase database;

    private EvaluationDao evaluationDao;

    List<Evaluation> evaluations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String login = sharedPreferences.getString("loginTemp", "chyba");

        database = AppRoomDatabase.getDatabase(this);

        evaluationDao = database.getEvaluationDao();

        evaluations = new ArrayList<Evaluation>();

        try{
            evaluations = evaluationDao.getUsersEvaluation(login);



        }catch (Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(this, "Bohuzel nelze nacist minule hodnoceni.", Toast.LENGTH_SHORT);
        }

        recycleAdapter = new RecycleViewAdapter(evaluations, this);

        recyclerView.setAdapter(recycleAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_home){
            startActivity(new Intent(HistoryActivity.this, HomeActivity.class));
            finish();

        }


        return super.onOptionsItemSelected(item);
    }
}
