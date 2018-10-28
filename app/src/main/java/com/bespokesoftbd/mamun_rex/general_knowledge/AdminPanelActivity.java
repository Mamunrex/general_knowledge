package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminPanelActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Dialog mDialogAddCategory, mDialogAddGKCategory;
    private ProgressDialog progressDialog;
    private long countPost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        mDialogAddCategory = new Dialog(AdminPanelActivity.this);
        mDialogAddGKCategory = new Dialog(AdminPanelActivity.this);

        progressDialog = new ProgressDialog(AdminPanelActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Adding Gk....");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void addCategoryBtn(View view) {
        showAddCategoryPopup();
    }

    public void addGK(View view) {
        showAddGK();
    }

    public void editCategory(View view) {
        Intent editCategoryIntent = new Intent(AdminPanelActivity.this, AllCategoryActivity.class);
        startActivity(editCategoryIntent);
    }

    public void editGK(View view) {
        Intent editGkIntent = new Intent(AdminPanelActivity.this, AllGKActivity.class);
        startActivity(editGkIntent);
    }

    public void goToHome(View view) {
        Intent intentGoToHome = new Intent(AdminPanelActivity.this, MainActivity.class);
        startActivity(intentGoToHome);
        intentGoToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    private void showAddCategoryPopup(){

        final Spinner addCategorySpinner;
        final EditText categoryNameET;
        Button cancelCatogoryBtn, addCategoryBtn;
        final TextView showCategory;
        ConstraintLayout mConstraintLayout;

        mDialogAddCategory.setContentView(R.layout.category_custome_popup);
        mDialogAddCategory.setCancelable(false);
        addCategorySpinner = mDialogAddCategory.findViewById(R.id.addCSpinner);
        categoryNameET = mDialogAddCategory.findViewById(R.id.categoryNameET);
        cancelCatogoryBtn = mDialogAddCategory.findViewById(R.id.cancelCatogoryBtn);
        addCategoryBtn = mDialogAddCategory.findViewById(R.id.addCategoryBtn);
        showCategory = mDialogAddCategory.findViewById(R.id.showCategory);
        mConstraintLayout = mDialogAddCategory.findViewById(R.id.addCSpinner2);


        cancelCatogoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                mDialogAddCategory.dismiss();
            }
        });


        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameET.getText().toString().trim();
                String addCategory = showCategory.getText().toString().trim();

                if (TextUtils.isEmpty(addCategory)){
                    Toast.makeText(AdminPanelActivity.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                }else if (addCategory.equals("Select Category")){
                    Toast.makeText(AdminPanelActivity.this, "Please Select Other Category Option", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(categoryName)){
                    Toast.makeText(AdminPanelActivity.this, "Please Select Category Name", Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.show();
                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
                    String myKey = mDatabaseReference.push().getKey();
                    String dateTime = DateFormat.getDateTimeInstance().format(new Date());

                    Map<String, Object> categoryMap = new HashMap<>();
                    categoryMap.put("category",addCategory);
                    categoryMap.put("categoryName", categoryName);
                    categoryMap.put("key", myKey);
                    categoryMap.put("dateTime", dateTime);
                    categoryMap.put("countPost", countPost);
                    categoryMap.put("timeStamp", ServerValue.TIMESTAMP);

                    mDatabaseReference.child(myKey).setValue(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AdminPanelActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                mDialogAddCategory.dismiss();
                            }else {
                                Toast.makeText(AdminPanelActivity.this, "Data Adding Fail", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                mDialogAddCategory.dismiss();
                            }
                        }
                    });

                }
            }
        });


        mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.category,android.R.layout.select_dialog_singlechoice);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                addCategorySpinner.setAdapter(adapter);

                addCategorySpinner.setVisibility(View.VISIBLE);
                String s = (String) addCategorySpinner.getSelectedItem();
                addCategorySpinner.performClick();

                addCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        showCategory.setText(""+ addCategorySpinner.getSelectedItem());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
        addCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showCategory.setText(""+ addCategorySpinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.category,android.R.layout.select_dialog_singlechoice);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        addCategorySpinner.setAdapter(adapter);
        mDialogAddCategory.show();

    }

    private void showAddGK() {

        final Spinner addGKSpinner;
        Button addGKCloseBtn, addGKNextBtn;
        ConstraintLayout mConstraintLayoutGK;
        final TextView showGKResust;

        mDialogAddGKCategory.setContentView(R.layout.add_gk_custom_popup);
        mDialogAddGKCategory.setCancelable(false);
        addGKSpinner = mDialogAddGKCategory.findViewById(R.id.addGKSpinner);
        addGKCloseBtn = mDialogAddGKCategory.findViewById(R.id.addGKCloseBtn);
        addGKNextBtn = mDialogAddGKCategory.findViewById(R.id.addGKNextBtn);
        mConstraintLayoutGK = mDialogAddGKCategory.findViewById(R.id.constraintLayout);
        showGKResust = mDialogAddGKCategory.findViewById(R.id.showGKResust);

        addGKCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogAddGKCategory.dismiss();
            }
        });

        addGKNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gkCategory = showGKResust.getText().toString().trim();

                if (TextUtils.isEmpty(gkCategory)){
                    Toast.makeText(AdminPanelActivity.this, "Please Select Gk", Toast.LENGTH_SHORT).show();
                }else if (gkCategory.equals("Select Category")){
                    Toast.makeText(AdminPanelActivity.this, "Please Select Other Gk Option", Toast.LENGTH_SHORT).show();
                }else {
                    Intent gkIntent = new Intent(AdminPanelActivity.this, AddGkActivity.class);
                    gkIntent.putExtra("category", gkCategory);
                    startActivity(gkIntent);
                    gkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }

            }
        });

        mConstraintLayoutGK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.category,android.R.layout.select_dialog_singlechoice);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                addGKSpinner.setAdapter(adapter);

                addGKSpinner.setVisibility(View.VISIBLE);
                String s = (String) addGKSpinner.getSelectedItem();
                addGKSpinner.performClick();

                addGKSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        showGKResust.setText(""+ addGKSpinner.getSelectedItem());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.category,android.R.layout.select_dialog_singlechoice);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        addGKSpinner.setAdapter(adapter);
        mDialogAddGKCategory.show();

    }



}
