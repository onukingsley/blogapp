package com.example.nox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class New_Post extends AppCompatActivity {
    ImageButton post_image;
    EditText post_title, post_content;
    Button submit;
    TextView home;
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    FirebaseUser currentuser;
    Uri image;
    String blogimage, blogtitle, blogcontent , username,avatar,fullname;
    int requestcode = 200;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        progressDialog = new ProgressDialog(New_Post.this);

        post_image = findViewById(R.id.ima);
        post_title = findViewById(R.id.title);
        post_content = findViewById(R.id.content);
        submit = findViewById(R.id.submit);
        home = findViewById(R.id.homepage);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        getuserdetails();

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bcontent = post_title.getText().toString();
                String btitle = post_title.getText().toString();
                if (!bcontent.isEmpty() && !btitle.isEmpty() ){
                    upload();
                }
            }
        });


    }
    public void chooseimage(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i,"selected image"),requestcode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == requestcode){
            image = data.getData();
            if (image!= null){
                post_image.setImageURI(image);

            }
        }

    }

    public void upload(){
        StorageReference ref = storageReference
                .child("image/posts/*"+ UUID.randomUUID());
        ref.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        blogimage = task.getResult().toString();

                        post();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(New_Post.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(New_Post.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void post(){
        Map<String, Object> newpost = new HashMap<>();

        blogtitle = post_title.getText().toString();
        blogcontent = post_content.getText().toString();
        SimpleDateFormat formater = new SimpleDateFormat("mm/hh, MM/yy");
        Date date = new Date();

        newpost.put("title", blogtitle);
        newpost.put("content", blogcontent);
        newpost.put("image", blogimage);
        newpost.put("Date_posted", formater.format(date));
        newpost.put("userid", currentuser.getUid());
        newpost.put("username", username);
        newpost.put("avatar", avatar);
        newpost.put("fullaname", fullname);
        newpost.put("no_of_comment", 0);

        db.collection("posts")
                .add(newpost).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(New_Post.this, "your post has been uploaded successfully", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(New_Post.this);
                builder.setTitle("post uploaded successfully");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(New_Post.this,HomeActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        post_content.setText("");
                        post_title.setText("");
                        post_image.setImageURI(null);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setMessage("Do you want to upload another post");
                dialog.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(New_Post.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
       public void getuserdetails(){

            db.collection("users").document(mAuth.getCurrentUser().getUid().toString()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                               username = documentSnapshot.getString("username");
                               avatar = documentSnapshot.getString("avatar");
                               fullname = documentSnapshot.getString("fullname");


                            }
                        }
                    });
        }
}