package com.example.abdelrahim.abdochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ForgetPassword extends AppCompatActivity {

    private Toolbar Ftoolbar;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Ftoolbar=(Toolbar) findViewById(R.id.f_toolbar);
        setSupportActionBar(Ftoolbar);
        getSupportActionBar().setTitle("Abdo chat");

    }
}
