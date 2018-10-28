package com.bespokesoftbd.mamun_rex.general_knowledge.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bespokesoftbd.mamun_rex.general_knowledge.R;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.QuestionAnswer;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerAdapter.ViewHolder> {

    private List<QuestionAnswer> questionAnswer;
    private Context qaContext;
    private List<String> lengthCount = new ArrayList<String>();

    public QuestionAnswerAdapter(List<QuestionAnswer> questionAnswers){
        this.questionAnswer = questionAnswers;
    }

    @NonNull
    @Override
    public QuestionAnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_question_answer,parent,false);
        qaContext = view.getContext();
        return new QuestionAnswerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAnswerAdapter.ViewHolder holder, int position) {

        //List<String> lengthCount = new ArrayList<String>();

        String question = questionAnswer.get(position).getQuestion();
        lengthCount.add(question);
        int size = lengthCount.size();
        holder.setQuestonTV(question);
        holder.setQuestonCount(size);
        String answer = questionAnswer.get(position).getAnswer();
        holder.setAnswerTV(answer);

    }

    @Override
    public int getItemCount() {
        return questionAnswer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View qaView;
        private TextView questionTV, answerTV, questionCount;

        public ViewHolder(View itemView) {
            super(itemView);
            qaView = itemView;
        }

        public void setQuestonCount(int count) {
            questionCount = qaView.findViewById(R.id.questionCount);
            questionCount.setText(count+".");
        }

        public void setQuestonTV(String questonTV) {
            questionTV = qaView.findViewById(R.id.questionTV);
            questionTV.setText(questonTV);
        }

        public void setAnswerTV(String answeTV) {
            answerTV = qaView.findViewById(R.id.answerTV);
            answerTV.setText("উত্তর: "+answeTV);
        }

    }
}
