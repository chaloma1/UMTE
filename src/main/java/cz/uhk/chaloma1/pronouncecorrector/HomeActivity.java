package cz.uhk.chaloma1.pronouncecorrector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.dao.EvaluationDao;
import cz.uhk.chaloma1.pronouncecorrector.database.AppRoomDatabase;
import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LineGraphSeries<DataPoint> correctSeries, wrongSeries;

    private SharedPreferences sharedPreferences;

    private AppRoomDatabase database;

    private EvaluationDao evaluationDao;

    private List<Evaluation> evaluationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String login = sharedPreferences.getString("loginTemp", "chyba");

        database = AppRoomDatabase.getDatabase(this);
        evaluationDao = database.getEvaluationDao();

        evaluationList = new ArrayList<Evaluation>();

        GraphView graph = findViewById(R.id.graphView);

        try{
            evaluationList = evaluationDao.getUsersEvaluation(login);

        }catch (Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(this, "Nahrani hodnoceni pro graf se nepovedlo.", Toast.LENGTH_SHORT);

        }

        if (evaluationList.size() != 0){
            double x, y, z;

            correctSeries = new LineGraphSeries<>();
            wrongSeries = new LineGraphSeries<>();



            for (int i = 0; i < evaluationList.size(); i++){
                x = i;
                y = evaluationList.get(i).getCorrectNumber();
                z = evaluationList.get(i).getWrongNumber();
                correctSeries.appendData(new DataPoint(x,y), true, evaluationList.size());
                wrongSeries.appendData(new DataPoint(x,z), true, evaluationList.size());

            }

            correctSeries.setColor(Color.GREEN);
            wrongSeries.setColor(Color.RED);

            correctSeries.setDrawBackground(true);
            wrongSeries.setDrawBackground(true);

            correctSeries.setBackgroundColor(Color.argb(30, 0,255,0));
            wrongSeries.setBackgroundColor(Color.argb(30, 255,0,0));


            graph.addSeries(correctSeries);
            graph.addSeries(wrongSeries);

            correctSeries.setTitle("Spravna slova");
            wrongSeries.setTitle("Spatna slova");
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


        }





        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Button buttonPracise = findViewById(R.id.buttonPractise);

        buttonPracise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AndroidTTS.class));
            }
        });

        Button buttonListening = findViewById(R.id.buttonListening);
        buttonListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ListeningActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(HomeActivity.this, HistoryActivity.class));

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
