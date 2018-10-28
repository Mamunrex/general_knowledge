package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.adapter.CategoryRecyclerAdapter;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AllCategoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView categoryRecyclerView;
    private List<Category> categoryList;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
        Query dataQuery = mDatabaseReference.orderByChild("dateTime");


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Category");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(AllCategoryActivity.this);
        progressDialog.setMessage("Please wait...");

        categoryList = new ArrayList<>();
        categoryRecyclerView = findViewById(R.id.RecyclerView);

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(AllCategoryActivity.this));
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);

        progressDialog.show();
        dataQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.exists()){

                            Category category = snapshot.getValue(Category.class);
                            categoryList.add(category);
                            categoryRecyclerAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }
                    }
                }else {
                    Toast.makeText(AllCategoryActivity.this, "Data Not Available", Toast.LENGTH_SHORT).show();
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