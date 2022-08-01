package com.example.nox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Post_Adapter extends ArrayAdapter {
    ArrayList<Post_Model> models;
    ArrayList<UserModels> usermodels;
    ArrayList<Comment_Model> nol = new ArrayList<>();
    ArrayList<Comment_Model> nod = new ArrayList<>();
    ArrayList<Comment_Model> noc = new ArrayList<>();
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    String userprofileimg, username, fullname;
    CheckBox like_btn,dislike_btn;
    DocumentSnapshot documentSnapshot;
    boolean like;
    int num;
    int no_of_dislike, no_of_likes, no_of_comment;



    public Post_Adapter(@NonNull Context context, ArrayList<Post_Model> models,ArrayList<UserModels>usermodels) {
        super(context, 0, models);
        this.context = context;
        this.models=models;
        this.usermodels = usermodels;
    }

    public Post_Adapter(@NonNull Context context, ArrayList<Post_Model> models,int num) {
        super(context, 0, models);
        this.context = context;
        this.models=models;
        this.num = num;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       if (num == 1){
           View view = convertView;
           view = LayoutInflater.from(context).inflate(R.layout.profile_single_post,parent,false);


           TextView username,fullname,time,post_title, post_content,likes,dislikes,comment,share;
           ImageView post_image;
           ImageButton  comment_btn,share_btn;


           ImageView profile_image = view.findViewById(R.id.profile_image);
           username = view.findViewById(R.id.username);
           fullname = view.findViewById(R.id.fullname);
           time = view.findViewById(R.id.publishedate);
           post_title = view.findViewById(R.id.post_title);
           post_content = view.findViewById(R.id.post_content);
           post_image= view.findViewById(R.id.post_image);


           post_title.setText( models.get(position).getTitle());
           post_content.setText(models.get(position).getContent());
           Picasso.with(context).load(models.get(position).getImage()).into(post_image);
           time.setText(models.get(position).getDateposted());
           username.setText(models.get(position).username);
           Picasso.with(context).load(models.get(position).user_image).into(profile_image);
           fullname.setText(models.get(position).fullname);

           return view;
       }

        View view = convertView;
        view = LayoutInflater.from(context).inflate(R.layout.singlepost,parent,false);
        no_of_likes = 0;
        no_of_comment = 0;
        no_of_dislike = 0;


        TextView username,fullname,time,post_title, post_content,likes,dislikes,comment,share;
        ImageView post_image;
        ImageButton  comment_btn,share_btn;


        ImageView profile_image = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        fullname = view.findViewById(R.id.fullname);
        time = view.findViewById(R.id.publishedate);
        post_title = view.findViewById(R.id.post_title);
        post_content = view.findViewById(R.id.post_content);
        likes = view.findViewById(R.id.likes);
        dislikes = view.findViewById(R.id.dislikes);
        comment = view.findViewById(R.id.comment);
        share = view.findViewById(R.id.share);
        post_image= view.findViewById(R.id.post_image);
        like_btn = view.findViewById(R.id.like_btn);
        dislike_btn = view.findViewById(R.id.dislike_btn);
        comment_btn = view.findViewById(R.id.comment_btn);

        post_title.setText( models.get(position).getTitle());
        post_content.setText(models.get(position).getContent());
        Picasso.with(context).load(models.get(position).getImage()).into(post_image);
        time.setText(models.get(position).getDateposted());
        username.setText(models.get(position).username);
        Picasso.with(context).load(models.get(position).user_image).into(profile_image);
        fullname.setText(models.get(position).fullname);
            /*likes.setText(String.valueOf(no_of_likes));
            comment.setText(String.valueOf(noc.size()));
            dislikes.setText(String.valueOf(no_of_dislike));*/
        getcomment(models.get(position).blog_id,comment);
        getlike(models.get(position).blog_id,likes);
        getdislike(models.get(position).blog_id,dislikes);


     profile_image.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Bundle bundle = new Bundle();
             bundle.putString("blog_id", models.get(position).blog_id);
             bundle.putString("time", models.get(position).dateposted);
             bundle.putString("fullname", models.get(position).fullname);
             bundle.putString("username", models.get(position).username);
             bundle.putString("userimage", models.get(position).user_image);
             bundle.putString("userid", models.get(position).user_id);
             bundle.putString("image", models.get(position).image);
             bundle.putString("title", models.get(position).title);
             bundle.putString("content", models.get(position).content);
             Intent i = new Intent(view.getContext(),Profile_Activity.class);

             i.putExtras(bundle);
             ((AppCompatActivity)getContext()).startActivity(i);

         }
     });

     comment_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             Bundle bundle = new Bundle();
             bundle.putString("blog_id", models.get(position).blog_id);
             bundle.putString("time", models.get(position).dateposted);
             bundle.putString("fullname", models.get(position).fullname);
             bundle.putString("username", models.get(position).username);
             bundle.putString("userimage", models.get(position).user_image);
             bundle.putString("userid", models.get(position).user_id);
             bundle.putString("image", models.get(position).image);
             bundle.putString("title", models.get(position).title);
             bundle.putString("content", models.get(position).content);
             Intent i = new Intent(view.getContext(),Comment_Activity.class);
             i.putExtras(bundle);
             ((AppCompatActivity)getContext()).startActivity(i);



         }

     });

     /*   getuserdetails(username,models.get(position).user_id,fullname,profile_image);*/

        db.collection("posts").document(models.get(position).blog_id).collection("dislike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                        if (snapshot.getString("user_id").toString().equals(mAuth.getCurrentUser().getUid())){
                            like = true;
                            dislike_btn.setButtonDrawable(R.drawable.red_dislike);
                            dislike_btn.setChecked(true);





                        }else{

                            dislike_btn.setButtonDrawable(R.drawable.dislike);

                        }
                    }
                }
            }
        });


    dislike_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
            if (ischecked){
                dislike(models.get(position).blog_id);
                dislike_btn.setButtonDrawable(R.drawable.red_dislike);
               /* like_btn.setClickable(false);*/
                deletelike(models.get(position).blog_id);


            }
            else{
                deletedislike(models.get(position).blog_id);

            }

        }
    });





        db.collection("posts").document(models.get(position).blog_id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                        if (snapshot.getString("user_id").toString().equals(mAuth.getCurrentUser().getUid())){
                            like = true;
                            like_btn.setButtonDrawable(R.drawable.green_like);
                            like_btn.setChecked(true);





                        }else{

                            like_btn.setButtonDrawable(R.drawable.like);

                        }
                    }
                }
            }
        });



  /*      if (models.get(position).like){
            like_btn.setChecked(true);
            like_btn.setButtonDrawable(R.drawable.green_like);
            Toast.makeText(context, "something", Toast.LENGTH_SHORT).show();
        }else if (models.get(position).like = false){
            like_btn.setButtonDrawable(R.drawable.like);
            Toast.makeText(context, "nothing", Toast.LENGTH_SHORT).show();
        }*/


        like_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {

             if (ischecked){

                       like(models.get(position).blog_id);
                       /*dislike_btn.setClickable(false);*/
                       deletedislike(models.get(position).blog_id);
                      /* like_btn.setButtonDrawable(R.drawable.green_like);*/


              }else {

                       deletelike(models.get(position).blog_id);

                  /*     like_btn.setButtonDrawable(R.drawable.like);*/
                      /* Toast.makeText(context, "you dislike"+likeref, Toast.LENGTH_SHORT).show();*/
                   }

          };



        });


        return view;
    }
/*    public void getuserdetails(TextView view , String id ,TextView fullname, ImageView image){
        db.collection("users").document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               DocumentSnapshot documentSnapshot = task.getResult();
               view.setText(documentSnapshot.getString("username"));
               fullname.setText(documentSnapshot.getString("fullname"));
               Picasso.with(context).load(documentSnapshot.getString("avatar")).into(image);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/




    public void like(String id){

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                userprofileimg = documentSnapshot.getString("avatar");
                username = documentSnapshot.getString("username");
                fullname = documentSnapshot.getString("fullname");

            }
        });

        Map<String, Object> likes = new HashMap<>();
        likes.put("userprofileimg" , userprofileimg);
        likes.put("username" , username);
        likes.put("fullname" , fullname);
        likes.put("postid", id);
        likes.put("user_id", mAuth.getCurrentUser().getUid());
        db.collection("posts").document(id).collection("likes").document(mAuth.getCurrentUser().getUid())
                .set(likes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                /*Toast.makeText(context, "you have sucessfully liked this post", Toast.LENGTH_SHORT).show();*/
            }
        });



    }
    public void deletelike(String id ){
        db.collection("posts").document(id).collection("likes").document(mAuth.getCurrentUser().getUid())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

/*
                Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
*/
            }
        });
    }
    public void deletelik(String id ){
        db.collection("posts").document(id).collection("likes").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                snapshot.getReference().delete();
            }
        });
    }

    public void dislike(String id){

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                userprofileimg = documentSnapshot.getString("avatar");
                username = documentSnapshot.getString("username");
                fullname = documentSnapshot.getString("fullname");

            }
        });

        Map<String, Object> dislikes = new HashMap<>();
        dislikes.put("userprofileimg" , userprofileimg);
        dislikes.put("username" , username);
        dislikes.put("fullname" , fullname);
        dislikes.put("postid", id);
        dislikes.put("user_id", mAuth.getCurrentUser().getUid());
        db.collection("posts").document(id).collection("dislike").document(mAuth.getCurrentUser().getUid())
                .set(dislikes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                /*Toast.makeText(context, "you have sucessfully liked this post", Toast.LENGTH_SHORT).show();*/
            }
        });


    }

    public void deletedislike(String id ) {
        db.collection("posts").document(id).collection("dislike").document(mAuth.getCurrentUser().getUid())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

/*
                Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
*/
            }
        });
    }

        public void getlike(String id, TextView like ){

            db.collection("posts").document(id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        nol.clear();
                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                            Comment_Model model = new Comment_Model();
                            model.user_id = snapshot.getId();
                            nol.add(model);
                        }
                    }like.setText(String.valueOf(nod.size()));
                }
            });
        }
        public void getdislike(String id , TextView dislike){

            db.collection("posts").document(id).collection("dislike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        nod.clear();
                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                            Comment_Model model = new Comment_Model();
                            model.user_id = snapshot.getId();
                            nod.add(model);
                        }
                    }dislike.setText(String.valueOf(nod.size()));

                }
            });
        }
        public void getcomment(String id, TextView comment){

            db.collection("posts").document(id).collection("comment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        noc.clear();
                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                            Comment_Model model = new Comment_Model();
                            model.user_id = snapshot.getString("user_id");
                            noc.add(model);
                        }
                        comment.setText(String.valueOf(noc.size()));
                    }
                }
            });
        }
    public void deletepost(String id ) {
        db.collection("posts").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "post successfully deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }







}
