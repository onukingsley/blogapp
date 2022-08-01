package com.example.nox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comment_Activity extends AppCompatActivity {
    TextView fullname, username, post_title, post_content, post_time,comment,likes,dislike;
    ImageView post_image, userimage;
    EditText commentbox;
    ArrayList<Comment_Model> model = new ArrayList<>();
    ArrayList<Comment_Model> user_model = new ArrayList<>();
    ArrayList<Comment_Model> all_model = new ArrayList<>();
    ArrayList<Comment_Model> all_likes = new ArrayList<>();
    ArrayList<Comment_Model> all_dislike = new ArrayList<>();
    Integer noofcomment;
    Button comment_btn;
    FirebaseAuth mAuth;
    LinearLayout usercomment_container,comment_container,new_comment;
    ListView usercommentlist,commentlist;
    FirebaseFirestore db;
    FloatingActionButton addcomment;
    WrapcommentAdapter wrapcommentAdapter;
    int no_comment;
    String userprofileimg, usernametxt, fullnametxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
         wrapcommentAdapter = new WrapcommentAdapter(Comment_Activity.this,all_model);

        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        post_title = findViewById(R.id.post_title);
        post_image = findViewById(R.id.post_image);
        post_content = findViewById(R.id.post_content);
        post_time = findViewById(R.id.publishedate);
        commentbox = findViewById(R.id.comment_text);
        comment = findViewById(R.id.comment);
        likes = findViewById(R.id.likes);
        dislike = findViewById(R.id.dislikes);
        comment_btn = findViewById(R.id.commen_btn);
        userimage = findViewById(R.id.profile_image);
        commentlist = findViewById(R.id.commentlist);
        addcomment = findViewById(R.id.add_comment);
        new_comment = findViewById(R.id.new_comment);
        /*usercommentlist = findViewById(R.id.usercommentlist);*/
        usercomment_container = findViewById(R.id.usercomment_container);
        comment_container = findViewById(R.id.comment_container);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Bundle i = getIntent().getExtras();
        getcomment(i.getString("blog_id"));
        getallcomment(i.getString("blog_id"));
        get_user_comment(i.getString("blog_id"));
        getlike(i.getString("blog_id"));
        getdislike(i.getString("blog_id"));
        fullname.setText(i.getString("fullname"));
        username.setText(i.getString("username"));
        post_title.setText(i.getString("title"));
        post_content.setText(i.getString("content"));
        post_time.setText(i.getString("time"));
        Picasso.with(Comment_Activity.this).load(i.getString("userimage")).into(userimage);
        Picasso.with(Comment_Activity.this).load(i.getString("image")).into(post_image);
        getuserdetails();

     /*   db.collection("posts").document(i.getString("blog_id")).collection("comment")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Comment_Model commodel = new Comment_Model(documentSnapshot.getString("username"),
                            documentSnapshot.getString("fullname"),
                            documentSnapshot.getString("user_image"),
                            documentSnapshot.getString("post_id"),
                            documentSnapshot.getString("date_commented"),
                            documentSnapshot.getString("user_id"),
                            documentSnapshot.getString("comment"));

                    all_model.add(commodel);
                    Toast.makeText(Comment_Activity.this, String.valueOf(all_model.size()), Toast.LENGTH_SHORT).show();
                    wrapcommentAdapter.notifyDataSetChanged();


                }



            }
        });
*/


        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_comment.setVisibility(View.VISIBLE);
            }
        });

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment(i.getString("blog_id"));
                new_comment.setVisibility(View.GONE);
                db.collection("posts").document(i.getString("blog_id")).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();

                                noofcomment = (Integer)document.get("no_of_comment");
                                noofcomment += 1;
                                Toast.makeText(Comment_Activity.this, noofcomment, Toast.LENGTH_SHORT).show();


                            }
                        });
            }
        });




        commentlist.setAdapter(wrapcommentAdapter);
        wrapcommentAdapter.notifyDataSetChanged();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                comment.setText(String.valueOf(all_model.size())) ;
                likes.setText(String.valueOf(all_likes.size())) ;
                dislike.setText(String.valueOf(all_dislike.size())) ;
            }
        },5000);




       /* if (model.size()==0){
            Toast.makeText(this, "no comment yet", Toast.LENGTH_SHORT).show();
        }*/
  /*      if (model.size()<=4 && model.size()>0){
            for (int mod = 0 ; mod<=model.size();mod++){
                View view = LayoutInflater.from(Comment_Activity.this).inflate(R.layout.single_comment,comment_container,false);
                ImageView user_image = view.findViewById(R.id.profile_image);
                TextView fullname = view.findViewById(R.id.fullname);
                TextView username = view.findViewById(R.id.username);
                TextView time = view.findViewById(R.id.publishedate);
                TextView content = view.findViewById(R.id.content);

                fullname.setText(model.get(mod).getFullname());
                username.setText(model.get(mod).getUsername());
                time.setText(model.get(mod).date_commented);
                content.setText(model.get(mod).comment);
                Picasso.with(Comment_Activity.this).load(model.get(mod).user_id).into(user_image);

                usercomment_container.addView(view);
            }
        }else{
            for (int mod = 0 ; mod<=4;mod++){
                View view = LayoutInflater.from(Comment_Activity.this).inflate(R.layout.single_comment,comment_container,false);
                ImageView user_image = view.findViewById(R.id.profile_image);
                TextView fullname = view.findViewById(R.id.fullname);
                TextView username = view.findViewById(R.id.username);
                TextView time = view.findViewById(R.id.publishedate);
                TextView content = view.findViewById(R.id.content);

                fullname.setText(model.get(mod).fullname);
                username.setText(model.get(mod).username);
                time.setText(model.get(mod).date_commented);
                content.setText(model.get(mod).comment);
                Picasso.with(Comment_Activity.this).load(model.get(mod).user_id).into(user_image);

                usercomment_container.addView(view);
            }
        }

*/



    }

    public void comment(String blog_id) {


        SimpleDateFormat format = new SimpleDateFormat("MM/hh  dd-mm");
        Date date = new Date();
        Map<String, Object> comment = new HashMap<>();
        comment.put("username", usernametxt);
        comment.put("fullname", fullnametxt);
        comment.put("user_image", userprofileimg);
        comment.put("post_id", blog_id);
        comment.put("date_commented", format.format(date));
        comment.put("user_id", mAuth.getCurrentUser().getUid());
        comment.put("comment", commentbox.getText().toString());

        db.collection("posts").document(blog_id).collection("comment")
                .add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Comment_Activity.this, "You have successfully commented on this post", Toast.LENGTH_SHORT).show();
                commentbox.setText("");
            }
        });
    }

    public void getallcomment(String blog_id) {
        db.collection("posts").document(blog_id).collection("comment")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Comment_Model commodel = new Comment_Model(documentSnapshot.getString("username"),
                                documentSnapshot.getString("fullname"),
                                documentSnapshot.getString("user_image"),
                                documentSnapshot.getString("post_id"),
                                documentSnapshot.getString("date_commented"),
                                documentSnapshot.getString("user_id"),
                                documentSnapshot.getString("comment"),
                                documentSnapshot.getId());
                                no_comment = no_comment +1;
                        all_model.add(commodel);
/*
                        Toast.makeText(Comment_Activity.this, String.valueOf(all_model.size()), Toast.LENGTH_SHORT).show();
*/
                        wrapcommentAdapter.notifyDataSetChanged();


                    }



            }
        });

    }
    public void getcomment(String blog_id) {
        db.collection("posts").document(blog_id).collection("comment")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (int i = 0; i < 4; i++) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Comment_Model cmodel = new Comment_Model(documentSnapshot.getString("username"),
                                documentSnapshot.getString("fullname"),
                                documentSnapshot.getString("user_image"),
                                documentSnapshot.getString("post_id"),
                                documentSnapshot.getString("date_commented"),
                                documentSnapshot.getString("user_id"),
                                documentSnapshot.getString("comment"));

                        model.add(cmodel);
/*
                        Toast.makeText(Comment_Activity.this, cmodel.fullname, Toast.LENGTH_SHORT).show();
*/
                        wrapcommentAdapter.notifyDataSetChanged();

                    }
                }


            }
        });

    }

    public void get_user_comment(String blog_id) {
        db.collection("posts").document(blog_id).collection("comment")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (documentSnapshot.getString("user_id").equals(mAuth.getCurrentUser().getUid())){
                        Comment_Model cmodel = new Comment_Model(documentSnapshot.getString("username"),
                                documentSnapshot.getString("fullname"),
                                documentSnapshot.getString("user_image"),
                                documentSnapshot.getString("post_id"),
                                documentSnapshot.getString("date_commented"),
                                documentSnapshot.getString("user_id"),
                                documentSnapshot.getString("comment"));

                        user_model.add(cmodel);
                        wrapcommentAdapter.notifyDataSetChanged();
                    }


                }
            }
        });



    }

    public void getuserdetails(){
        db.collection("users").document(mAuth.getCurrentUser().getUid().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    userprofileimg = documentSnapshot.getString("avatar");
                    usernametxt = documentSnapshot.getString("username");
                    fullnametxt = documentSnapshot.getString("fullname");

                }

            }
        });
    }


    public void getlike(String id ){

        db.collection("posts").document(id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                        Comment_Model model = new Comment_Model();
                        model.user_id = snapshot.getId();
                        all_likes.add(model);
                    }
                }
            }
        });
    }
    public void getdislike(String id ){

        db.collection("posts").document(id).collection("dislike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                        Comment_Model model = new Comment_Model();
                        model.user_id = snapshot.getId();
                        all_dislike.add(model);
                    }
                }

            }
        });
    }
}