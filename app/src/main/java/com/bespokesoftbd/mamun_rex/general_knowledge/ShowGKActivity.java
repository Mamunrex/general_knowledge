package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.adapter.GkRecyclerAdapter;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.Gk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowGKActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String category;
    private RecyclerView categoryRecyclerView;
    private List<Gk> gkList;
    private GkRecyclerAdapter categoryRecyclerAdapter;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gk);

        category = getIntent().getStringExtra("name");
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(category);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        progressDialog = new ProgressDialog(ShowGKActivity.this);
        progressDialog.setMessage("Please wait...");

        gkList = new ArrayList<>();
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

        categoryRecyclerAdapter = new GkRecyclerAdapter(gkList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(ShowGKActivity.this));
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("GK").child(category);
        progressDialog.show();
        mDatabaseReference.orderByChild("dateTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.exists()){
                            Gk gk = snapshot.getValue(Gk.class);
                            gkList.add(gk);
                            categoryRecyclerAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(ShowGKActivity.this, "Data Not Available", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
