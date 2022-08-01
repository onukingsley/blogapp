package com.example.nox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Personal_post_Fragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<Post_Model> postModels = new ArrayList<>();
    boolean like;
    String username,fullname,profile_image,timepublished,post_title,post_content;
    ListView postContainer;
    personal_profile_adapter adapter;
    public Personal_post_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        get_posts();
        adapter = new personal_profile_adapter(getContext(),postModels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_personal_post, container, false);
        postContainer = view.findViewById(R.id.postContainer);

        personal_profile_adapter adapter = new personal_profile_adapter(view.getContext(),postModels);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postContainer.setAdapter(adapter);
            }
        },5000);


        return view;
    }
    public void get_posts(){
        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                if (snapshot.getString("userid").equals(mAuth.getUid())){
                                    Post_Model model = new Post_Model(snapshot.getString("Date_published"),
                                            snapshot.getString("content"),
                                            snapshot.getString("image"),
                                            snapshot.getString("title"),
                                            snapshot.getString("userid"),
                                            snapshot.getId(),
                                            snapshot.getString("avatar"),
                                            snapshot.getString("username"),
                                            snapshot.getString("fullname"),like
                                    );
                                    Toast.makeText(getContext(), model.username, Toast.LENGTH_SHORT).show();
                                    postModels.add(model);
                                    adapter.notifyDataSetChanged();
                                }

                            };
                        }
                    }
                });
    }
}