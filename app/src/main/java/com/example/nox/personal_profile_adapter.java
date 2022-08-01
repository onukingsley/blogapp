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
import java.util.Map;

public class personal_profile_adapter extends ArrayAdapter<Post_Model> {
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
    int no_of_dislike, no_of_likes, no_of_comment;



    public personal_profile_adapter(@NonNull Context context, ArrayList<Post_Model> models) {
        super(context, 0, models);
        this.context = context;
        this.models=models;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(context).inflate(R.layout.profile_single_post,parent,false);



        TextView username,fullname,time,post_title, post_content,likes,dislikes,comment,share;
        ImageView post_image,edit,delete;
        ImageButton comment_btn,share_btn;


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
        post_image= view.findViewById(R.id.post_img);
        like_btn = view.findViewById(R.id.like_btn);
        dislike_btn = view.findViewById(R.id.dislike_btn);
        comment_btn = view.findViewById(R.id.comment_btn);
        edit = view.findViewById(R.id.edit);
        delete = view.findViewById(R.id.delete);

        post_title.setText( models.get(position).getTitle());
        post_content.setText(models.get(position).getContent());
        Picasso.with(context).load(models.get(position).getImage()).into(post_image);
        time.setText(models.get(position).getDateposted());
        username.setText(models.get(position).username);
        Picasso.with(context).load(models.get(position).user_image).into(profile_image);
        fullname.setText(models.get(position).fullname);



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
                Intent i = new Intent(view.getContext(),HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(bundle);
                ((AppCompatActivity)getContext()).startActivity(i);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletepost(models.get(position).blog_id);
                remove(models.get(position));
            }
        });












        return view;
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
