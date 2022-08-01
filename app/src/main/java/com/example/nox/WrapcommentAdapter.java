package com.example.nox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class WrapcommentAdapter extends ArrayAdapter {

    ArrayList<Comment_Model> model = new ArrayList<>();
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();





    public WrapcommentAdapter(@NonNull Context context, ArrayList model) {
        super(context, 0, model);
        this.model = model;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(context).inflate(R.layout.single_comment,parent,false);

        if (model.get(position).user_id.equals(mAuth.getCurrentUser().getUid())){
            view = LayoutInflater.from(context).inflate(R.layout.single_user_comment,parent,false);
            ImageView profileimage = view.findViewById(R.id.profile_image);
            TextView fullname = view.findViewById(R.id.fullname);
            TextView username = view.findViewById(R.id.username);
            TextView time = view.findViewById(R.id.publishedate);
            TextView content = view.findViewById(R.id.content);
            ImageView edit_comment = view.findViewById(R.id.edit_profile);
            ImageView delete_comment = view.findViewById(R.id.comment_btn);


            fullname.setText(model.get(position).fullname);
            username.setText(model.get(position).username);
            time.setText(model.get(position).date_commented);
            content.setText(model.get(position).comment);
            Picasso.with(context).load(model.get(position).user_image).into(profileimage);
            edit_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            delete_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletecomment(model.get(position).comment_id,model.get(position).post_id);
                }
            });


        }
        ImageView profileimage = view.findViewById(R.id.profile_image);
        TextView fullname = view.findViewById(R.id.fullname);
        TextView username = view.findViewById(R.id.username);
        TextView time = view.findViewById(R.id.publishedate);
        TextView content = view.findViewById(R.id.content);


            fullname.setText(model.get(position).fullname);
            username.setText(model.get(position).username);
            time.setText(model.get(position).date_commented);
            content.setText(model.get(position).comment);
            Picasso.with(context).load(model.get(position).user_image).into(profileimage);








        return view;
    }


    public void deletecomment(String id,String post_id){
        db.collection("posts").document(post_id).collection("comment")
                .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "comment deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
