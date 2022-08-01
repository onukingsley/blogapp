package com.example.nox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    EditText username,fullname,email,password;
    ImageView avatar;
    Button signup;
    TextView signin;
    int requestcode = 200;
    Uri profile;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;

    String usernametxt,fullnametxt,emailtxt,avatarurl,passwordtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        avatar = findViewById(R.id.image);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagepicker();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernametxt = username.getText().toString();
                emailtxt = email.getText().toString();
                fullnametxt = fullname.getText().toString();
                passwordtxt = password.getText().toString();
                Signup(emailtxt,passwordtxt);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this,SignIn.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void imagepicker(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i,"image selector"),requestcode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==requestcode){
            profile = data.getData();
            if (profile != null){
                avatar.setImageURI(profile);
            }
        }

    }
    public void Signup(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                uploadimage();
            }
        });

    }


    public void uploadimage(){
        StorageReference ref = storageReference.child("image/profile/*"+ UUID.randomUUID().toString());

        ref.putFile(profile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        avatarurl = task.getResult().toString();
                        postuser();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void postuser(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();



        Map<String, Object> user = new HashMap<>();
        user.put("username",usernametxt);
        user.put("fullname",fullnametxt);
        user.put("emailtxt",emailtxt);
        user.put("Date_registered",format.format(date));
        user.put("avatar",avatarurl);

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SignUp.this, "registered sucessfully", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SignUp.this,HomeActivity.class);
                        startActivity(i);
                    }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}