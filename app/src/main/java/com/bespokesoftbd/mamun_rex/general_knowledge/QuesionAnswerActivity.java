package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.adapter.QuestionAnswerAdapter;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.NoInternet;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.QuestionAnswer;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.ShowCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuesionAnswerActivity extends AppCompatActivity {

    private TextView showCategoryNameTV;
    private String category, categoryName;

    private RecyclerView qaRecyclerView;
    private List<QuestionAnswer> questionAnswer;
    private QuestionAnswerAdapter questionAnswerAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quesion_answer);

        showCategoryNameTV = findViewById(R.id.showCategoryNameTV);
        category = getIntent().getStringExtra("category");
        categoryName = getIntent().getStringExtra("categoryName");
        showCategoryNameTV.setText(category);

        progressDialog = new ProgressDialog(QuesionAnswerActivity.this);
        progressDialog.setMessage("Please wait...");

        questionAnswer = new ArrayList<>();
        qaRecyclerView = findViewById(R.id.qaRecyclerView);
        questionAnswerAdapter = new QuestionAnswerAdapter(questionAnswer);
        qaRecyclerView.setLayoutManager(new LinearLayoutManager(QuesionAnswerActivity.this));
        qaRecyclerView.setAdapter(questionAnswerAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("GK");
        Query databaseReference = ref.child(category).orderByChild("categoryName").equalTo(categoryName);

        if (NoInternet.checkConnection(getApplicationContext())){
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if (snapshot.exists()){
                                QuestionAnswer qA = snapshot.getValue(QuestionAnswer.class);
                                questionAnswer.add(qA);
                                questionAnswerAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        }
                    }else {
                        Toast.makeText(QuesionAnswerActivity.this, "Data Not Available", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(QuesionAnswerActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }

    public void imageBackBtn(View view) {
        backPrev();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPrev();

    }

    private void backPrev(){
        Intent backBtn = new Intent(QuesionAnswerActivity.this, UserSeeCategoryActivity.class);
        backBtn.putExtra("name", category);
        startActivity(backBtn);
        backBtn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

}
