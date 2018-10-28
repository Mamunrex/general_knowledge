package com.bespokesoftbd.mamun_rex.general_knowledge.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.QuesionAnswerActivity;
import com.bespokesoftbd.mamun_rex.general_knowledge.R;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.ShowCategory;

import java.util.List;

public class UserShowCategoryAdapter extends RecyclerView.Adapter<UserShowCategoryAdapter.ViewHolder> {

    private List<ShowCategory> showCategories;
    private Context context;

    public UserShowCategoryAdapter(List<ShowCategory> showCategorie){
        this.showCategories = showCategorie;
    }

    @NonNull
    @Override
    public UserShowCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_category_user,parent,false);
        context = parent.getContext();
        return new UserShowCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserShowCategoryAdapter.ViewHolder holder, final int position) {

        final String category = showCategories.get(position).getCategory();
        final String categoryName = showCategories.get(position).getCategoryName();
        holder.category(categoryName);

        holder.cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goIntent = new Intent(context,QuesionAnswerActivity.class);
                goIntent.putExtra("category", category);
                goIntent.putExtra("categoryName", categoryName);
                context.startActivity(goIntent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return showCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View cView;
        private TextView showCategoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            cView = itemView;
        }

        public void category(String c){
            showCategoryName = cView.findViewById(R.id.showCategoryName);
            showCategoryName.setText(c);
        }


    }
}
