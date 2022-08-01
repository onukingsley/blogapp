package com.example.nox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottom_nav;
    NavigationView navbar, mAuth_navbar;
    Button edit;
    TextView no_of_posts, no_of_subscribers,fullname,username;
    ImageView profileimage;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    DrawerLayout drawerlayout;
    FrameLayout framelayout;
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    FirebaseFirestore db;
    String fullnametxt, no_of_poststxt,usernametxt,image;
    ArrayList<Post_Model> postarray = new ArrayList<>();

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        currentdetails();
        get_of_posts();

        drawerlayout = findViewById(R.id.drawer);
     /*   toolbar = (Toolbar) findViewById(R.id.toolba);*/
        NewsfeedFragment news = new NewsfeedFragment();
        SearchFragment search = new SearchFragment();
        Notification_Fragment notification = new Notification_Fragment();
        ProfileFragment profile = new ProfileFragment();

        navbar = findViewById(R.id.unauthorized_user);
        mAuth_navbar = findViewById(R.id.authorized_user);
        bottom_nav = findViewById(R.id.bottom_nav);




        View view = navbar.inflateHeaderView(R.layout.nav_header);
        edit = view.findViewById(R.id.edituserProfile);
        profileimage = view.findViewById(R.id.profile_image);
        no_of_posts = view.findViewById(R.id.no_of_posts);
        no_of_subscribers = view.findViewById(R.id.no_of_subscribers);
        fullname= view.findViewById(R.id.fullname);
        username = view.findViewById(R.id.username);
        framelayout = findViewById(R.id.home_container);

        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,drawerlayout,toolbar,R.string.open,R.string.close);

        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        /*setSupportActionBar(toolbar);*/
       /*  <-I dont really know why i cant assign supportActionBar->


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);*/

        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,news).commit();

        if (currentuser!=null){

            navbar.getMenu().clear();
            navbar.inflateMenu(R.menu.authorized_navbar_menu);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fullname.setText(fullnametxt);
                    username.setText(usernametxt);
                    Picasso.with(HomeActivity.this).load(image).into(profileimage);
                    no_of_posts.setText(String.valueOf(postarray.size()));
                }
            },5000);


        }else{
            navbar.getMenu().clear();
            navbar.inflateMenu(R.menu.unauthorized_navbar_menu);
            fullname.setText("");
            username.setText("");


        }




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "you ckicked on the edit button", Toast.LENGTH_SHORT).show();
            }
        });

        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.home:
                        Toast.makeText(HomeActivity.this, "you clicked on home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.contact_us:
                        Toast.makeText(HomeActivity.this, "you clicked on home", Toast.LENGTH_SHORT).show();
                       break;
                    case R.id.login:
                        intent = new Intent(HomeActivity.this,SignUp.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        mAuth.signOut();
                        intent = new Intent(HomeActivity.this,SignIn.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });
        bottom_nav.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.newsfeed:

                        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,news).commit();
                        break;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,search).commit();
                        break;
                    case R.id.notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,notification).commit();
                        break;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,profile).commit();
                        break;
                }

                return false;
            }
        });
        
        
    }
    public void currentdetails(){
        db.collection("users").document(currentuser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (task.isSuccessful()){
                            fullnametxt = snapshot.getString("fullname");
                            usernametxt = snapshot.getString("username");
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
                                if (snapshot.getString("userid").equals(currentuser.getUid())){
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