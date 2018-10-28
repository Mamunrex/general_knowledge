package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bespokesoftbd.mamun_rex.general_knowledge.model.NoInternet;
import com.bespokesoftbd.mamun_rex.general_knowledge.model.QuestionAnswer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Snackbar snackBar;
    private ConstraintLayout constraintLayout, bangladesh1, sports1, international2, sceince2;
    private TextView sbTextView, bdCount, internationalCountTV, sportsCountTV, scienceCountTV;
    private FirebaseAuth firebaseAuth;
    private Animation mFromBottom, mFromTop, mFromLeft, mFromRight;
    private ImageView gkLogoImageView;
    private DatabaseReference bdDatabaseRefer, interDatabaseRefer, sportsDatabaseRefer, scienceDatabaseRefer;
    private AlertDialog.Builder alertDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        firebaseAuth = FirebaseAuth.getInstance();
        alertDialogBuilder = new AlertDialog.Builder(this);

        // SnackBar
        constraintLayout = findViewById(R.id.constraintLayout);
        snackBar = Snackbar.make(constraintLayout, "No Internet Connection",Snackbar.LENGTH_SHORT);

        View snackBarView = snackBar.getView();
        sbTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        sbTextView.setTextColor(Color.YELLOW);

        gkLogoImageView = findViewById(R.id.gkLogoImageView);
        bangladesh1 = findViewById(R.id.bangladesh1);
        sports1 = findViewById(R.id.sports1);
        international2 = findViewById(R.id.international2);
        sceince2 = findViewById(R.id.sceince2);
        bdCount = findViewById(R.id.bdCount);

        internationalCountTV = findViewById(R.id.internationalCountTV);
        sportsCountTV = findViewById(R.id.sportsCountTV);
        scienceCountTV = findViewById(R.id.scienceCountTV);


        mFromBottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        mFromTop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        mFromLeft = AnimationUtils.loadAnimation(this,R.anim.fromleft);
        mFromRight = AnimationUtils.loadAnimation(this,R.anim.fromright);

        gkLogoImageView.setAnimation(mFromTop);
        bangladesh1.setAnimation(mFromLeft);
        sports1.setAnimation(mFromRight);
        international2.setAnimation(mFromRight);
        sceince2.setAnimation(mFromLeft);

        bdDatabaseRefer = FirebaseDatabase.getInstance().getReference().child("GK").child("বাংলাদেশ");
        bdDatabaseRefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int bangladeshCount = (int) dataSnapshot.getChildrenCount();
                String countBD = Integer.toString(bangladeshCount);
                bdCount.setText(countBD);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        interDatabaseRefer = FirebaseDatabase.getInstance().getReference().child("GK").child("আন্তর্জাতিক");
        interDatabaseRefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int internationalCount = (int) dataSnapshot.getChildrenCount();
                String countInternational = Integer.toString(internationalCount);
                internationalCountTV.setText(countInternational);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sportsDatabaseRefer = FirebaseDatabase.getInstance().getReference().child("GK").child("খেলাধুলা");
        sportsDatabaseRefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sportsCount = (int) dataSnapshot.getChildrenCount();
                String countSports = Integer.toString(sportsCount);
                sportsCountTV.setText(countSports);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        scienceDatabaseRefer = FirebaseDatabase.getInstance().getReference().child("GK").child("বিজ্ঞান ও প্রযুক্তি");
        scienceDatabaseRefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int scienceCount = (int) dataSnapshot.getChildrenCount();
                String countScience = Integer.toString(scienceCount);
                scienceCountTV.setText(countScience);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = firebaseAuth.getCurrentUser();
        if (NoInternet.checkConnection(getApplicationContext())){
            if (cUser == null){
                Intent splashScreen = new Intent(MainActivity.this, SplashActivity.class);
                splashScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(splashScreen);
                finish();
            }
        }else {
            Intent noInternet = new Intent(MainActivity.this, NoInternetActivity.class);
            noInternet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(noInternet);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_share:
                shareApp();
                return true;
            case R.id.nav_about:
                return true;
            case R.id.nav_admin:
                Intent adminLogin = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(adminLogin);
                return true;
            case R.id.nav_exit:
                finishApp();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void bangladeshOnClick(View view) {
        if (NoInternet.checkConnection(getApplicationContext())){
            Intent bdIntent = new Intent(MainActivity.this, UserSeeCategoryActivity.class);
            bdIntent.putExtra("name", "বাংলাদেশ");
            startActivity(bdIntent);
        }else {
            snackBar.show();
        }
    }
    public void internationalOnClick(View view) {
        if (NoInternet.checkConnection(getApplicationContext())){
            Intent bdIntent = new Intent(MainActivity.this, UserSeeCategoryActivity.class);
            bdIntent.putExtra("name", "আন্তর্জাতিক");
            startActivity(bdIntent);
        }else {
            snackBar.show();
        }

    }
    public void sportsOnClick(View view) {
        if (NoInternet.checkConnection(getApplicationContext())){
            Intent bdIntent = new Intent(MainActivity.this, UserSeeCategoryActivity.class);
            bdIntent.putExtra("name", "খেলাধুলা");
            startActivity(bdIntent);
        }else {
            snackBar.show();
        }

    }
    public void scienceOnClick(View view) {
        if (NoInternet.checkConnection(getApplicationContext())){
            Intent bdIntent = new Intent(MainActivity.this, UserSeeCategoryActivity.class);
            bdIntent.putExtra("name", "বিজ্ঞান ও প্রযুক্তি");
            startActivity(bdIntent);
        }else {
            snackBar.show();
        }

    }

    private void finishApp() {

        alertDialogBuilder.setTitle("Confirm");
        alertDialogBuilder.setIcon(R.drawable.alert);
        alertDialogBuilder.setMessage("           Do your really want exit app..?");

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).
          setPositiveButton("Yes", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                finish();
             }
         });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    private void shareApp() {

        final String appPackageName = BuildConfig.APPLICATION_ID;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareSubText = getString(R.string.app_name);
        String shareBodyText = "https://play.google.com/store/apps/details?id=" +appPackageName;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(shareIntent, "Share With"));

    }

}
