package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.model.NoInternet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Snackbar snackBar, snackBar1;
    private ConstraintLayout constraintLayout;
    private TextView sbTextView, appNameSP;
    private ImageView splashScreenLogo;
    private Animation mFromBottom, mFromTop, mFromLeft, mFromRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        appNameSP = findViewById(R.id.appNameSP);
        splashScreenLogo = findViewById(R.id.splashScreenLogo);

        // SnackBar
        constraintLayout = findViewById(R.id.constraintLayout);
        snackBar = Snackbar.make(constraintLayout,"No Internet Connection",Snackbar.LENGTH_INDEFINITE)
                .setAction("Refresh", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (NoInternet.checkConnection(getApplicationContext())){
                            startWork();
                            progressBar.setVisibility(View.VISIBLE);
                        }else {
                            snackBar1.show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        snackBar1 = Snackbar.make(constraintLayout,"No Internet Connection",Snackbar.LENGTH_INDEFINITE)
                .setAction("Refresh", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (NoInternet.checkConnection(getApplicationContext())){
                            startWork();
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

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        mFromBottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        mFromTop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        mFromLeft = AnimationUtils.loadAnimation(this,R.anim.fromleft);
        mFromRight = AnimationUtils.loadAnimation(this,R.anim.fromright);

        splashScreenLogo.setAnimation(mFromTop);
        appNameSP.setAnimation(mFromLeft);


    }

    @Override
    protected void onStart() {
        super.onStart();
        connectionCheck();
    }

    private void connectionCheck(){
        progressBar.setVisibility(View.VISIBLE);

        if (NoInternet.checkConnection(getApplicationContext())){
            startWork();
            progressBar.setVisibility(View.VISIBLE);

        }else {
            snackBar.show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void startWork(){

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser == null){
            mAuth.signInAnonymously().addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        startApp();
                        progressBar.setVisibility(View.INVISIBLE);
                    }else {
                        Toast.makeText(SplashActivity.this, "Anonymous login Fail", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doWork();
                    startApp();
                    finish();
                }
            }).start();
        }
    }

    private void doWork(){
        for (int progress = 0; progress<100; progress+=3){
            try {
                Thread.sleep(500);
                progressBar.setProgress(progress);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void startApp(){
        Intent mainScreenIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainScreenIntent);
        finish();
    }

}
