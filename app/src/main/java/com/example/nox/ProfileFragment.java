package com.example.nox;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {
TabLayout tabLayout;
FloatingActionButton new_post;
ImageView profile_image,user_coverpage;
TextView fullname,username,date_registered,no_of_posts,no_of_subscribers;
String fullnametxt,usernametxt,date_radisteredtxt,poststxt,subscriberstxt,image;
CardView edit_profile;
ViewPager viewPager;
ArrayList<Post_Model> postarray = new ArrayList<>();
FirebaseFirestore db = FirebaseFirestore.getInstance();
FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        get_of_posts();
        getuserdetails();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile, container, false);
       new_post = view.findViewById(R.id.new_post);

       username = view.findViewById(R.id.username);
       fullname = view.findViewById(R.id.fullname);
       date_registered = view.findViewById(R.id.date_registered);
       profile_image = view.findViewById(R.id.profile_image);
       user_coverpage = view.findViewById(R.id.user_coverpage);
       no_of_posts = view.findViewById(R.id.no_of_posts);
       no_of_subscribers = view.findViewById(R.id.no_of_subscribers);
       no_of_subscribers = view.findViewById(R.id.no_of_subscribers);
       edit_profile = view.findViewById(R.id.edit_profile);
       viewPager = view.findViewById(R.id.viewpager);

       new_post.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(view.getContext(),New_Post.class);
               startActivity(intent);
           }
       });
        get_of_posts();
        getuserdetails();


       tabLayout = view.findViewById(R.id.tabview);
       tabLayout.addTab(tabLayout.newTab().setText(R.string.posts));
       tabLayout.addTab(tabLayout.newTab().setText(R.string.subscribers));
       Post_pager_Adapter postpageradapter = new Post_pager_Adapter(getChildFragmentManager(),view.getContext(),tabLayout.getTabCount());
       tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       viewPager.setAdapter(postpageradapter);
       viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fullname.setText(fullnametxt);
                username.setText(usernametxt);
                Picasso.with(((AppCompatActivity)getActivity())).load(image).into(profile_image);
                Picasso.with(((AppCompatActivity)getActivity())).load(image).into(user_coverpage);
            }
        },5000);



        return view;
    }
    public void getuserdetails(){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            usernametxt = snapshot.getString("username");
                            fullnametxt = snapshot.getString("fullname");
                            image = snapshot.getString("avatar");

                        }
                    }
                });
    }
    public void get_of_posts(){
        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                if (snapshot.getString("userid").equals(mAuth.getUid())){
                                    Post_Model model = new Post_Model();
                                    model.user_id = snapshot.getString("userid");

                                    postarray.add(model);
                                }
                            }
                        }
                    }
                });
    }
}
