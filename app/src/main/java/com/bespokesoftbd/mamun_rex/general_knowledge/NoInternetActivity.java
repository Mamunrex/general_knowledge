package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bespokesoftbd.mamun_rex.general_knowledge.model.NoInternet;

public class NoInternetActivity extends AppCompatActivity {

    private Snackbar snackBar, snackBar1;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private TextView sbTextView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        progressBar = findViewById(R.id.progressBar);
        constraintLayout = findViewById(R.id.constraintLayout);

        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,5000);

        snackBar = Snackbar.make(constraintLayout,R.string.noNetwork,Snackbar.LENGTH_INDEFINITE)
                .setAction("Refresh", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (NoInternet.checkConnection(getApplicationContext())){
                            noInternetConnection();
                            progressBar.setVisibility(View.VISIBLE);
                        }else {
                            snackBar1.show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        snackBar1 = Snackbar.make(constraintLayout,R.string.noNetwork,Snackbar.LENGTH_INDEFINITE)
                .setAction("Refresh", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (NoInternet.checkConnection(getApplicationContext())){
                            noInternetConnection();
                            progressBar.setVisibility(View.VISIBLE);
                        }else {
                            snackBar.show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        View snackBarView = snackBar.getView();
        sbTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        sbTextView.setTextColor(Color.WHITE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!NoInternet.checkConnection(getApplicationContext())){
            snackBar.show();
        }


    }

    private final Runnable m_Runnable = new Runnable() {
        private boolean killMe=false;
        public void run() {
            try {
                if (NoInternet.checkConnection(getApplicationContext())){
                    noInternetConnection();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){ }
            NoInternetActivity.this.mHandler.postDelayed(m_Runnable, 5000);
        }


    };

    private void noInternetConnection(){
        Intent intent = new Intent(NoInternetActivity.this, MainActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(m_Runnable);
    }
}
