package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser==null){
                    startActivities(new Intent[]{new Intent(Splash_Activity.this, Login_Activity.class)});
                }
                else {
                    startActivities(new Intent[]{new Intent(Splash_Activity.this, MainActivity.class)});
                }
                finish();
            }
        }, 800);
    }
}