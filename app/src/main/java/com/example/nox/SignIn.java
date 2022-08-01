package com.example.nox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    TextView signup;
    EditText email,password;
    Button signin;
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    NavigationView navbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        email = findViewById(R.id.signin_email);
        password = findViewById(R.id.passwd);
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        navbar = findViewById(R.id.signin_navbar);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtxt = email.getText().toString();
                String passwordtxt = password.getText().toString();
                if (emailtxt.isEmpty() && passwordtxt.isEmpty()){
                    Toast.makeText(SignIn.this, "please fill up all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    signinuser(emailtxt,passwordtxt);
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this,SignUp.class);
                startActivity(i);
            }
        });
        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()){
                    case R.id.home:
                    i= new Intent(SignIn.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                    break;

                    case R.id.login:
                        i= new Intent(SignIn.this,SignIn.class);
                        startActivity(i);
                        finish();
                        break;

                }


                return false;
            }
        });

    }

    public void signinuser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(SignIn.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(SignIn.this, "Wrong infomation", Toast.LENGTH_SHORT).show();
                    Intent i  = new Intent(SignIn.this,SignIn.class);
                    startActivity(i);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignIn.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}