package com.example.nox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

Toolbar toolbars;
ListView listview,listView_posts;
ArrayList<Search_Model> model = new ArrayList<>();
ArrayList<Search_Model> models = new ArrayList<>();
String blogid;
FirebaseFirestore db = FirebaseFirestore.getInstance();
FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        toolbars = view.findViewById(R.id.tool);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbars);
        listview = view.findViewById(R.id.search_userprofile);
        listView_posts = view.findViewById(R.id.search_posts);





/*
        <---this is the search view and your fetchuser Method is going to be called here passing in the name of the user form the searchview-->
*/
        SearchView searchView = view.findViewById(R.id.searchme);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {





                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                model.clear();
                models.clear();
                user(newText);
                getpost(newText);
                Search_adapter searchadapter = new Search_adapter(view.getContext(),model,2);
                Search_adapter postsearch = new Search_adapter(view.getContext(),model,1);
                if (newText.equals(null)){
                    searchadapter.notifyDataSetChanged();
                    postsearch.notifyDataSetChanged();
                }




                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        listview.setAdapter(searchadapter);
                        listView_posts.setAdapter(postsearch);
                        Toast.makeText(view.getContext(), String.valueOf(models.size()), Toast.LENGTH_SHORT).show();
                        searchadapter.notifyDataSetChanged();
                        postsearch.notifyDataSetChanged();
                    }
                },10000);


                return true;
            }
        });
        return view;
    }
    public void user (String text){
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            if(snapshot.getString("username").contains(text)){
                                Search_Model mod = new Search_Model(snapshot.getString("fullname"),
                                        snapshot.getString("username"),
                                        snapshot.getString("avatar"),
                                        snapshot.getId());
/*
                                Toast.makeText(getContext(), snapshot.getString("fullname"), Toast.LENGTH_SHORT).show();
*/
                                model.add(mod);
                            }
                        }
                    }
                });
    }
    public void getpost(String text){
        db.collection("posts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                        if (documentSnapshot.getString("title").contains(text)){
                            blogid = documentSnapshot.getId();



                            Search_Model mod = new Search_Model(documentSnapshot.getString("fullaname")
                                    ,documentSnapshot.getString("username")
                                    ,documentSnapshot.getString("avatar")
                                    ,documentSnapshot.getString("userid")
                                    ,documentSnapshot.getString("Date_posted")
                                    ,documentSnapshot.getString("title")
                                    ,documentSnapshot.getString("image")
                                    ,documentSnapshot.getString("content")
                                    ,documentSnapshot.getId()
                                    );


/*
                                Toast.makeText(getContext(), documentSnapshot.getString("image"), Toast.LENGTH_SHORT).show();
*/


                            models.add(mod);

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
}