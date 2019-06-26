package cz.uhk.chaloma1.pronouncecorrector;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.dao.EvaluationDao;
import cz.uhk.chaloma1.pronouncecorrector.dao.WordDao;
import cz.uhk.chaloma1.pronouncecorrector.database.AppRoomDatabase;
import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;
import cz.uhk.chaloma1.pronouncecorrector.model.Word;

public class StatisticActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> correctSeries, wrongSeries;

    private BarGraphSeries<DataPoint> rankWordsSeries;

    private SharedPreferences sharedPreferences;

    private AppRoomDatabase database;

    private EvaluationDao evaluationDao;

    private List<Evaluation> evaluationList;

    private WordDao wordDao;

    private List<Word> leastRankingWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String login = sharedPreferences.getString("loginTemp", "chyba");

        database = AppRoomDatabase.getDatabase(this);
        evaluationDao = database.getEvaluationDao();
        wordDao = database.getWordDao();

        evaluationList = new ArrayList<Evaluation>();
        leastRankingWords = new ArrayList<Word>();

        GraphView graphEval = findViewById(R.id.graphViewEval);

        GraphView graphWordRank = findViewById(R.id.graphViewWordRank);

        Button buttonStatisticHome = findViewById(R.id.buttonStatisticHome);

        buttonStatisticHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try{
            evaluationList = evaluationDao.getUsersEvaluationASC(login);
            System.out.println("Evaluation list size: " + evaluationList.size());

        }catch (Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(this, "Nahrani hodnoceni pro graf se nepovedlo.", Toast.LENGTH_SHORT).show();

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


            graphEval.getViewport().setYAxisBoundsManual(true);
            graphEval.getViewport().setMaxY(0);
            graphEval.getViewport().setMaxY(20);



            graphEval.getViewport().setMinX(evaluationList.size()-5);
            graphEval.getViewport().setMaxX(evaluationList.size());

            graphEval.getViewport().setScalable(true);
            //graphEval.getViewport().setScrollable(true);

            graphEval.addSeries(correctSeries);
            graphEval.addSeries(wrongSeries);

            graphEval.getGridLabelRenderer().setHorizontalAxisTitle("Cislo mereni");
            graphEval.getGridLabelRenderer().setVerticalAxisTitle("Hodnoceni");


           /* graphEval.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){

                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX){
                        if (value < evaluationList.size()) {
                            return evaluationList.get((int) value).getDatum();
                        }else{ return "";}
                    }

                    return super.formatLabel(value, isValueX);
                }
            });
            */



            correctSeries.setTitle("Spravna slova");
            wrongSeries.setTitle("Spatna slova");
            graphEval.getLegendRenderer().setVisible(true);
            graphEval.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);




        }

        try{

            leastRankingWords = wordDao.getLeastRankingWords();
            System.out.println("Least ranking words: " + " " + leastRankingWords.size() );

        }catch (Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(this, "Chyba v nacteni nejtezsich slov", Toast.LENGTH_SHORT).show();
        }

        if (leastRankingWords.size() != 0){
            double x,y;

            rankWordsSeries = new BarGraphSeries<>();

            for (int i = 0; i < leastRankingWords.size(); i++){
                x = i ;
                y = leastRankingWords.get(i).getRanking();
                rankWordsSeries.appendData(new DataPoint(x,y), false, leastRankingWords.size());

            }

            rankWordsSeries.setDrawValuesOnTop(true);



            // setSpacing(10);

            rankWordsSeries.setSpacing(20);

            rankWordsSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    if (data.getY() < 0){
                        rankWordsSeries.setValuesOnTopColor(Color.rgb(255, 0, 0));
                        return Color.rgb(255, 0, 0);

                    }else if (data.getY() > 0){
                        rankWordsSeries.setValuesOnTopColor(Color.rgb(0,100,0));
                        return Color.rgb(0,255,0);
                    }else {
                        rankWordsSeries.setValuesOnTopColor(Color.YELLOW);
                        return Color.YELLOW;

                    }
                }
            });

            graphWordRank.getViewport().setXAxisBoundsManual(true);
            graphWordRank.getViewport().setMaxX(leastRankingWords.size());




           // graphWordRank.getViewport().setMaxX(leastRankingWords.size());

            //graphWordRank.getGridLabelRenderer().setNumHorizontalLabels(4);


            //graphWordRank.getGridLabelRenderer().setLabelsSpace(-10);

            graphWordRank.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);

            //graphWordRank.getGridLabelRenderer().setHorizontalLabelsAngle(90);



            //graphWordRank.getViewport().setScrollable(true);

            graphWordRank.addSeries(rankWordsSeries);

            rankWordsSeries.setTitle("Nejmene spravne vyslovena slova");

            graphWordRank.getLegendRenderer().setVisible(true);
            graphWordRank.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            graphWordRank.getGridLabelRenderer().setHorizontalAxisTitle("Slovo");
            graphWordRank.getGridLabelRenderer().setVerticalAxisTitle("Hodnota uspesnosti");





            graphWordRank.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX){
                        if(value < leastRankingWords.size()){
                            if (value % 1 == 0.0) {
                                return leastRankingWords.get((int) value).getNazev();
                            }
                        }
                    }else {
                        return super.formatLabel(value, isValueX);
                    }
                    return null;
                }
            });



        }


    }
}
