package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGkActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseReference databaseReference, mAddDatabaseRefer;
    private List<String> categoryName = new ArrayList<String>();

    private TextView tvShowCategoryName;
    private EditText etAddQuestion, etAddAnswer;
    private Spinner spinnerCategoryName;
    private Button mAddData;
    private ConstraintLayout constraintLayout;
    private ProgressDialog mProgressDialog;
    private  String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gk);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Question and Answer");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        tvShowCategoryName = findViewById(R.id.tvShowCategoryName);
        etAddQuestion = findViewById(R.id.etAddQuestion);
        etAddAnswer = findViewById(R.id.etAddAnswer);
        spinnerCategoryName = findViewById(R.id.spinnerCategoryName);
        mAddData = findViewById(R.id.addData);
        constraintLayout = findViewById(R.id.constraintLayout);

        category = getIntent().getStringExtra("category");

        //====== Progress Dialog =========
        mProgressDialog = new ProgressDialog(AddGkActivity.this);
        mProgressDialog.setTitle("Please wait ...");
        mProgressDialog.setMessage("Adding General Knowledge");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query databaseReference = ref.child("Category").orderByChild("category").equalTo(category);

        //======= Data retrived =======
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    categoryName.add(postSnapshot.child("categoryName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //======== Spinner ============
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_singlechoice, categoryName);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerCategoryName.setAdapter(adapter);

        //==== Spinner show constraint layout ======
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_singlechoice, categoryName);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinnerCategoryName.setAdapter(adapter);

                spinnerCategoryName.setVisibility(View.VISIBLE);
                String s = (String) spinnerCategoryName.getSelectedItem();
                spinnerCategoryName.performClick();

                spinnerCategoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        tvShowCategoryName.setText(""+ spinnerCategoryName.getSelectedItem());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        //=========== Button on click save data ==============
        mAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGKData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu_gk,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save){
            addGKData();
        }else if (id == R.id.exit){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backAdminPanelActivity = new Intent(AddGkActivity.this, AdminPanelActivity.class);
        startActivity(backAdminPanelActivity);
        backAdminPanelActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    private void addGKData(){
        String categoryName = tvShowCategoryName.getText().toString().trim();
        String question = etAddQuestion.getText().toString().trim();
        String answer = etAddAnswer.getText().toString().trim();
        if (TextUtils.isEmpty(categoryName)){
            Toast.makeText(AddGkActivity.this, "Please select category name.", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(question)){
            Toast.makeText(AddGkActivity.this, "Please Input your Question.", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(answer)){
            Toast.makeText(AddGkActivity.this, "Please Input your Answer.", Toast.LENGTH_SHORT).show();
        }else {

            mProgressDialog.show();
            mAddDatabaseRefer = FirebaseDatabase.getInstance().getReference().child("GK").child(category);
            String myKey = mAddDatabaseRefer.push().getKey();
            String dateTime = DateFormat.getDateTimeInstance().format(new Date());

            Map<String, Object> gkMap = new HashMap<>();
            gkMap.put("category", category);
            gkMap.put("categoryName", categoryName);
            gkMap.put("question", question);
            gkMap.put("answer", answer);
            gkMap.put("gkid",myKey);
            gkMap.put("dateTime",dateTime);
            gkMap.put("timeStamp", ServerValue.TIMESTAMP);

            mAddDatabaseRefer.child(myKey).setValue(gkMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AddGkActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        tvShowCategoryName.setText("");
                        etAddQuestion.setText("");
                        etAddAnswer.setText("");
                        mProgressDialog.dismiss();
                    }else {
                        Toast.makeText(AddGkActivity.this, "Data add Fail", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }

    }
}
