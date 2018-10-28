package com.bespokesoftbd.mamun_rex.general_knowledge.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.AllCategoryActivity;
import com.bespokesoftbd.mamun_rex.general_knowledge.R;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>{

    public List<Category> categoriesList;
    public Context mContext;
    private DatabaseReference mAddDatabaseRefer;
    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog progressDialog;
    private Dialog cDialog;

    public CategoryRecyclerAdapter(List<Category> categoryeListInfo){
        this.categoriesList = categoryeListInfo;
    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_delete_category,parent,false);
        mContext = parent.getContext();

        alertDialogBuilder = new AlertDialog.Builder(mContext);
        progressDialog = new ProgressDialog(mContext);
        cDialog = new Dialog(mContext);

        return new CategoryRecyclerAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.ViewHolder holder, final int position) {

        String category = categoriesList.get(position).getCategory();
        holder.setCategory(category);
        String categoryName = categoriesList.get(position).getCategoryName();
        holder.categoryName(categoryName);
        String dateTime = categoriesList.get(position).getDateTime();
        holder.setDateTime(dateTime);
        final String key = categoriesList.get(position).getKey();



        //==================== Edit Button =================

        holder.imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView tvCategory;
                final EditText etCategoryName;
                Button btnClose, btnUpdate;

                cDialog.setContentView(R.layout.edit_category_popup);
                cDialog.setCancelable(false);
                tvCategory = cDialog.findViewById(R.id.tvCategory);
                etCategoryName = cDialog.findViewById(R.id.etCategoryName);
                btnClose = cDialog.findViewById(R.id.btnClose);
                btnUpdate = cDialog.findViewById(R.id.btnUpdate);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Category").child(key);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            String category = dataSnapshot.child("category").getValue().toString();
                            String categoryName = dataSnapshot.child("categoryName").getValue().toString();
                            tvCategory.setText(category);
                            etCategoryName.setText(categoryName);
                            cDialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cDialog.dismiss();
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String category = tvCategory.getText().toString().trim();
                        String categoryName = etCategoryName.getText().toString().trim();
                        if (!TextUtils.isEmpty(categoryName)){

                            progressDialog.setTitle("Update");
                            progressDialog.setMessage("Please wait Category is Updating...");
                            //progressDialog.setCanceledOnTouchOutside(false);

                            DatabaseReference updateDatabaseRefer = FirebaseDatabase.getInstance().getReference().child("Category").child(key);
                            String dateTime = DateFormat.getDateTimeInstance().format(new Date());

                            HashMap<String, String> updateMap = new HashMap<>();
                            updateMap.put("category", category);
                            updateMap.put("categoryName", categoryName);
                            updateMap.put("key",key);
                            updateMap.put("dateTime", dateTime);

                            updateDatabaseRefer.setValue(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent newIntent = new Intent(mContext, AllCategoryActivity.class);
                                        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mContext.startActivity(newIntent);
                                        ((Activity)mContext).finish();
                                        Toast.makeText(mContext, "Category Update Successful", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }else {
                                        Toast.makeText(mContext, "Category Update Fail", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(mContext, "Input Category Name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //==================== Delete Button =================

        holder.imgDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogBuilder.setTitle("Confirm...");
                alertDialogBuilder.setMessage("Are You Sure ?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setTitle("Delete");
                        progressDialog.setMessage("Please wait Category is deleting...");
                        progressDialog.setCanceledOnTouchOutside(false);

                        mAddDatabaseRefer = FirebaseDatabase.getInstance().getReference();
                        if (key == null){
                            Toast.makeText(mContext, "Key Not Available", Toast.LENGTH_SHORT).show();
                        }else {
                            mAddDatabaseRefer.child("Category").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Intent reload = new Intent(mContext, AllCategoryActivity.class);
                                        reload.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mContext.startActivity(reload);
                                        ((Activity)mContext).finish();
                                        Toast.makeText(mContext, "Successfully Delete", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }

                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView tvCategory, tvDateTime, tvCategoryName;
        private ImageView imgBtnEdit,imgDeleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            imgBtnEdit = mView.findViewById(R.id.imgBtnEdit);
            imgDeleteBtn = mView.findViewById(R.id.imgDeleteBtn);
        }

        public void setCategory(String category){
            tvCategory = mView.findViewById(R.id.tvCategory);
            tvCategory.setText(category);

        }
        public void setDateTime(String dateTime){
            tvDateTime = mView.findViewById(R.id.tvDateTime);
            tvDateTime.setText(dateTime);
        }
        public void categoryName(String cataName){
            tvCategoryName = mView.findViewById(R.id.tvCategoryName);
            tvCategoryName.setText(cataName);
        }

    }
}
