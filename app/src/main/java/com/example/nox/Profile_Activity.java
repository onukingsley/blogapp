package com.example.nox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile_Activity extends AppCompatActivity {

    ImageView user_coverpage,profile_image;
    TextView fullname,username,date_registered,no_of_posts,no_of_subscribers;
    ListView posts;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<Post_Model> model = new ArrayList<>();
    ArrayList<UserModels> usermodel = new ArrayList<>();
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dialog = new ProgressDialog(Profile_Activity.this);
        dialog.setTitle("Loading profile...");

        dialog.show();

        user_coverpage = findViewById(R.id.user_coverpage);
        profile_image = findViewById(R.id.profile_image);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        date_registered = findViewById(R.id.date_registered);
        no_of_posts  = findViewById(R.id.no_of_posts);
        no_of_subscribers = findViewById(R.id.no_of_subscribers);
        posts = findViewById(R.id.posts);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        Bundle bundle = getIntent().getExtras();

        fullname.setText(bundle.getString("fullname"));
        username.setText(bundle.getString("username"));
        Picasso.with(Profile_Activity.this).load(bundle.getString("userimage")).into(profile_image);
        Picasso.with(Profile_Activity.this).load(bundle.getString("userimage")).into(user_coverpage);

        String blogid = bundle.getString("userid");
        personal_profile_adapter post_adapter = new personal_profile_adapter(Profile_Activity.this,model);
        getposts(blogid);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                posts.setAdapter(post_adapter);
                post_adapter.notifyDataSetChanged();
                no_of_posts.setText(String.valueOf(model.size()));
            }
        },5000);







    }

    public void getposts(String id){
        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            if (snapshot.getString("userid").equals(id)){
                                Post_Model mod = new Post_Model(snapshot.getString("Date_posted"),
                                        snapshot.getString("content"),
                                        snapshot.getString("image"),
                                        snapshot.getString("title"),
                                        snapshot.getString("userid"),
                                        snapshot.getId(),snapshot.getString("avatar"),
                                        snapshot.getString("username"),
                                        snapshot.getString("fullaname"),
                                        true);
                                Toast.makeText(Profile_Activity.this, snapshot.getString("username"), Toast.LENGTH_SHORT).show();

                                model.add(mod);
                            }



                        }
                    }
                });
    }
}