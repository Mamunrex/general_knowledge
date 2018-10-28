package com.bespokesoftbd.mamun_rex.general_knowledge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mAdminBtn;
    private EditText mUsername, mPassword;
    private final String userName="admin", passWord="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Admin Login");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mUsername = findViewById(R.id.userName);
        mPassword = findViewById(R.id.passWord);
        mAdminBtn = findViewById(R.id.adminLoginBtn);

        mAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mUsername.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)){
                    if (!name.equals(userName)){
                        Toast.makeText(AdminLoginActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
                    } else if(!pass.equals(passWord)){
                        Toast.makeText(AdminLoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }else {

                        Intent adminPanelIntent = new Intent(AdminLoginActivity.this, AdminPanelActivity.class);
                        startActivity(adminPanelIntent);
                        adminPanelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();

                    }

                }else {
                    Toast.makeText(AdminLoginActivity.this, "Please enter Username and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
