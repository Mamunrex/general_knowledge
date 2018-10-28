package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditGkActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView tvShowCategoryName;
    private EditText etAddQuestion, etAddAnswer;
    private Button updateData;
    private ProgressDialog progressDialog;
    private String category;

    private DatabaseReference editDbReference, updateDataRefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gk);

        category = getIntent().getStringExtra("name");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(category);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        final String gkId = getIntent().getStringExtra("key");

        tvShowCategoryName = findViewById(R.id.tvShowCategoryName);
        etAddQuestion = findViewById(R.id.etAddQuestion);
        etAddAnswer = findViewById(R.id.etAddAnswer);
        updateData = findViewById(R.id.updateData);

        progressDialog = new ProgressDialog(EditGkActivity.this);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Please wait .....");

        editDbReference = FirebaseDatabase.getInstance().getReference().child("GK").child(category).child(gkId);
        editDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String cName = dataSnapshot.child("categoryName").getValue().toString();
                    String question = dataSnapshot.child("question").getValue().toString();
                    String answer = dataSnapshot.child("answer").getValue().toString();


                    tvShowCategoryName.setText(cName);
                    etAddQuestion.setText(question);
                    etAddAnswer.setText(answer);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = etAddQuestion.getText().toString().trim();
                String answer = etAddAnswer.getText().toString().trim();

                if (TextUtils.isEmpty(question)){
                    Toast.makeText(EditGkActivity.this, "Please Input your Question", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(answer)){
                    Toast.makeText(EditGkActivity.this, "Please Input your Answer", Toast.LENGTH_SHORT).show();
                }else {

                    progressDialog.show();
                    updateDataRefer = FirebaseDatabase.getInstance().getReference().child("GK").child(category).child(gkId);
                    String dateTime = DateFormat.getDateTimeInstance().format(new Date());

                    Map<String, Object> gkMap = new HashMap<>();
                    gkMap.put("category", category);
                    gkMap.put("categoryName", question);
                    gkMap.put("question", question);
                    gkMap.put("answer", answer);
                    gkMap.put("dateTime", dateTime);
                    gkMap.put("gkid",gkId);
                    gkMap.put("timeStamp", ServerValue.TIMESTAMP);

                    updateDataRefer.setValue(gkMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent allGkIntent = new Intent(EditGkActivity.this, ShowGKActivity.class);
                                allGkIntent.putExtra("name", category);
                                allGkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(allGkIntent);
                                Toast.makeText(EditGkActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }else {
                                Toast.makeText(EditGkActivity.this, "Updating Fail", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });



                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goBackGk = new Intent(EditGkActivity.this, ShowGKActivity.class);
        goBackGk.putExtra("name", category);
        goBackGk.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goBackGk);
        finish();
    }
}
