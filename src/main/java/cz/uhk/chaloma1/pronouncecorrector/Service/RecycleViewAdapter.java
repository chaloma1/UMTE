package cz.uhk.chaloma1.pronouncecorrector.Service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.uhk.chaloma1.pronouncecorrector.R;
import cz.uhk.chaloma1.pronouncecorrector.model.Evaluation;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private List<Evaluation> evaluationList;
    private Context context;

    public RecycleViewAdapter(List<Evaluation> evaluationList, Context context) {
        this.evaluationList = evaluationList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_evaluation, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Evaluation evaluation = evaluationList.get(position);

        holder.textViewEvalAverage.setText(String.format("%.2f",evaluation.getAverage()) + "%");
        holder.textViewEvalDate.setText(evaluation.getDatum());
        holder.textViewEvalCorrect.setText("Correct words: " + " " + evaluation.getCorrectWords());
        holder.textViewEvalWrong.setText("Wrong words: " + " " + evaluation.getWrongWords());


    }

    @Override
    public int getItemCount() {
        return evaluationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewEvalAverage, textViewEvalDate, textViewEvalCorrect, textViewEvalWrong;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewEvalAverage = itemView.findViewById(R.id.textViewEvalAverage);
            textViewEvalDate = itemView.findViewById(R.id.textViewEvalDate);
            textViewEvalCorrect = itemView.findViewById(R.id.textViewEvalCorrect);
            textViewEvalWrong = itemView.findViewById(R.id.textViewEvalWrong);
        }
    }
}
