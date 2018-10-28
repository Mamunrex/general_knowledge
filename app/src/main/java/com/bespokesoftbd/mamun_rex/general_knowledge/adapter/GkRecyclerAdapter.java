package com.bespokesoftbd.mamun_rex.general_knowledge.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.EditGkActivity;
import com.bespokesoftbd.mamun_rex.general_knowledge.R;
import com.bespokesoftbd.mamun_rex.general_knowledge.ShowGKActivity;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.Gk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class GkRecyclerAdapter extends RecyclerView.Adapter<GkRecyclerAdapter.ViewHolder>{

    public List<Gk> gkListInfo;
    public Context context;
    private DatabaseReference mAddDatabaseRefer;
    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog progressDialog;

    public GkRecyclerAdapter(List<Gk> categoriesListInfo){
        this.gkListInfo = categoriesListInfo;
    }

    @NonNull
    @Override
    public GkRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_delete_gk,parent,false);
        context = parent.getContext();

        alertDialogBuilder = new AlertDialog.Builder(context);
        progressDialog = new ProgressDialog(context);

        return new  ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GkRecyclerAdapter.ViewHolder holder, final int position) {

        String dateTime = gkListInfo.get(position).getDateTime();
        holder.setTvDateTime(dateTime);
        String question = gkListInfo.get(position).getQuestion();
        holder.setTvQuestion(question);
        String answer = gkListInfo.get(position).getAnswer();
        holder.setTvAnswer(answer);

        final String gkId = gkListInfo.get(position).getGkid();
        final String category = gkListInfo.get(position).getCategory();


        //==================== Edit Button =================

        holder.imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gkId == null){
                    Toast.makeText(context, "Key Not Available", Toast.LENGTH_SHORT).show();
                }else {
                    Intent editIntent = new Intent(context, EditGkActivity.class);
                    editIntent.putExtra("name", category);
                    editIntent.putExtra("key", gkId);
                    editIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(editIntent);
                    ((Activity)context).finish();

                }
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
                        progressDialog.setMessage("Please wait gk is deleting...");
                        progressDialog.setCanceledOnTouchOutside(false);

                        mAddDatabaseRefer = FirebaseDatabase.getInstance().getReference();
                        if (gkId == null){
                            Toast.makeText(context, "Key Not Available", Toast.LENGTH_SHORT).show();
                        }else {
                            mAddDatabaseRefer.child("GK").child(category).child(gkId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Intent reload = new Intent(context, ShowGKActivity.class);
                                        reload.putExtra("name", category);
                                        reload.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(reload);
                                        ((Activity)context).finish();
                                        Toast.makeText(context, "Successfully Delete", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
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
        return gkListInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView tvDateTime, tvQuestion, tvAnswer;
        private ImageView imgBtnEdit,imgDeleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            imgBtnEdit = mView.findViewById(R.id.imgBtnEdit);
            imgDeleteBtn = mView.findViewById(R.id.imgDeleteBtn);
        }

        public void setTvDateTime(String dateTime){
            tvDateTime = mView.findViewById(R.id.tvDateTime);
            tvDateTime.setText(dateTime);

        }
        public void setTvQuestion(String question){
            tvQuestion = mView.findViewById(R.id.tvQuestion);
            tvQuestion.setText(question);
        }
        public void setTvAnswer(String answer){
            tvAnswer = mView.findViewById(R.id.tvAnswer);
            tvAnswer.setText("Answer : "+answer);
        }

    }
}