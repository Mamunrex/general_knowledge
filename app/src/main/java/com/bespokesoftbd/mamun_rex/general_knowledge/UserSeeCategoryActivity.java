package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.adapter.UserShowCategoryAdapter;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.Gk;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.NoInternet;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.ShowCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserSeeCategoryActivity extends AppCompatActivity {

    private String category;
    private TextView categoryShowTV;
    private RecyclerView recyclerView;
    private List<ShowCategory> showCategory;
    private UserShowCategoryAdapter userShowCategoryAdapter;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog progressDialog;
    private Snackbar snackbar;
    private ConstraintLayout constraintLayout;
    private TextView sbTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_see_category);

        category = getIntent().getStringExtra("name");
        categoryShowTV = findViewById(R.id.categoryShowTV);
        categoryShowTV.setText(category);

        progressDialog = new ProgressDialog(UserSeeCategoryActivity.this);
        progressDialog.setMessage("Please wait...");

        // SnackBar
        constraintLayout = findViewById(R.id.constraintLayout);
        snackbar = Snackbar.make(constraintLayout, "No Internet Connection",Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        sbTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        sbTextView.setTextColor(Color.WHITE);

        showCategory = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        userShowCategoryAdapter = new UserShowCategoryAdapter(showCategory);
        //recyclerView.setLayoutManager(new LinearLayoutManager(UserSeeCategoryActivity.this));
        recyclerView.setLayoutManager(new GridLayoutManager(UserSeeCategoryActivity.this,2));
        recyclerView.setAdapter(userShowCategoryAdapter);


       if (NoInternet.checkConnection(getApplicationContext())){

           DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
           Query databaseReference = ref.child("Category").orderByChild("category").equalTo(category);

           progressDialog.show();
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {

                   if (dataSnapshot.exists()){
                       for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                           if (snapshot.exists()){
                               ShowCategory showCat = snapshot.getValue(ShowCategory.class);
                               showCategory.add(showCat);
                               userShowCategoryAdapter.notifyDataSetChanged();
                               progressDialog.dismiss();
                           }
                       }
                   }else {
                       Toast.makeText(UserSeeCategoryActivity.this, "Data Not Available", Toast.LENGTH_SHORT).show();
                       progressDialog.dismiss();
                   }

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });

       }else {
           snackbar.show();
       }



    }

    public void backImageBtn(View view) {

        Intent backIntent = new Intent(UserSeeCategoryActivity.this, MainActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backIntent);
        finish();

    }
}
