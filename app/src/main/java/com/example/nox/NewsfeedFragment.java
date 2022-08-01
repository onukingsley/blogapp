package com.example.nox;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;


public class NewsfeedFragment extends Fragment {
        ActionBarDrawerToggle actionBarDrawerToggle;
        ListView listView;
        Toolbar toolbars;
        LayoutInflater layoutInflater;
        ArrayList<UserModels>userModels = new ArrayList<>();
        ArrayList<String>no_comment = new ArrayList<>();
        ArrayList<Post_Model>models = new ArrayList<>();
        Post_Adapter post_adapter;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        String blogid, ref;
        boolean like;
        int no_of_dislike, no_of_likes, no_of_comment;




    public NewsfeedFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater= LayoutInflater.from(getContext());
        setHasOptionsMenu(true);



        post_adapter= new Post_Adapter(((AppCompatActivity)getContext()),models,userModels);
        post_adapter.notifyDataSetChanged();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        toolbars = view.findViewById(R.id.toolba);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbars);
        getpost();


        listView = view.findViewById(R.id.newsfee);
        post_adapter= new Post_Adapter(view.getContext(),models,userModels);
        listView.setAdapter(post_adapter);
        post_adapter.notifyDataSetChanged();




        return view;
    }
    public void getpost(){
        db.collection("posts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                        blogid = documentSnapshot.getId().toString();
                        getlike(blogid);


                       Post_Model new_postmodel = new Post_Model(documentSnapshot.getString("Date_posted")
                       ,documentSnapshot.getString("content")
                       ,documentSnapshot.getString("image")
                       ,documentSnapshot.getString("title")
                       ,documentSnapshot.getString("userid")
                       ,documentSnapshot.getId()
                       ,documentSnapshot.getString("avatar")
                       ,documentSnapshot.getString("username")
                       ,documentSnapshot.getString("fullaname")
                       ,like,no_comment.size(),no_comment.size(),no_comment.size());
                       ;



                       models.add(new_postmodel);

                       post_adapter.notifyDataSetChanged();

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getlike(String id){
        db.collection("posts").document(id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot snapshot : task.getResult() ){

                        if (snapshot.getString("user_id").toString().equals(mAuth.getCurrentUser().getUid())){
                            like = true;
                            ref = "true";
                          /*  like_btn.setButtonDrawable(R.drawable.green_like);
                            like_btn.setChecked(true);*/


                         /*   Toast.makeText(context, "something" + snapshot.getString("username")+likeref, Toast.LENGTH_SHORT).show();
*/
                        }else{

                            ref = "false";

                            /* Toast.makeText(context, "nothing" + snapshot.getString("username")+likeref, Toast.LENGTH_SHORT).show();
                            like_btn.setButtonDrawable(R.drawable.like);
*/
                        }
                    }
                }
            }
        });
    }
    public void getdislike(String id){
        db.collection("posts").document(id).collection("dislike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                        no_of_dislike = no_of_dislike + 1;
                    }
                }
            }
        });
    }
    public void getcomment(String id){
        db.collection("posts").document(id).collection("comment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                       no_comment.add(snapshot.getId());
/*
                        Toast.makeText(getContext(), String.valueOf(no_comment.size()), Toast.LENGTH_SHORT).show();
*/
                    }
                }
            }
        });
    }
  /*  public void getuserdetails(){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                userprofileimg = documentSnapshot.getString("avatar");
                username = documentSnapshot.getString("username");
                fullname = documentSnapshot.getString("fullname");

            }
        });
    }*/

  /*  public void getuserdetails(String id){

            db.collection("users").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                UserModels newusermodel = new UserModels(documentSnapshot.getString("avatar")
                                        ,documentSnapshot.getString("emailtxt")
                                        ,documentSnapshot.getString("fullname")
                                        ,documentSnapshot.getString("username"));

                                userModels.add(newusermodel);
                                post_adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }*/


}