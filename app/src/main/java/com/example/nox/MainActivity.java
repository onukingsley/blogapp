package com.example.nox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!= null){
                    Intent i = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(MainActivity.this,SignIn.class);
                    startActivity(i);
                    finish();
                }

            }
        },3000);
    }


}