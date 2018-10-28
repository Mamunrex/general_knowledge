package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AllGKActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gk);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All GK List");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void bangladeshOnClick(View view) {
        Intent bdIntent = new Intent(AllGKActivity.this, ShowGKActivity.class);
        bdIntent.putExtra("name", "বাংলাদেশ");
        startActivity(bdIntent);
    }

    public void internationalOnClick(View view) {

        Intent bdIntent = new Intent(AllGKActivity.this, ShowGKActivity.class);
        bdIntent.putExtra("name", "আন্তর্জাতিক");
        startActivity(bdIntent);

    }

    public void sportsOnClick(View view) {

        Intent bdIntent = new Intent(AllGKActivity.this, ShowGKActivity.class);
        bdIntent.putExtra("name", "খেলাধুলা");
        startActivity(bdIntent);

    }

    public void scienceOnClick(View view) {

        Intent bdIntent = new Intent(AllGKActivity.this, ShowGKActivity.class);
        bdIntent.putExtra("name", "বিজ্ঞান ও প্রযুক্তি");
        startActivity(bdIntent);

    }
}
