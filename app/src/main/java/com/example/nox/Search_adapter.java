package com.example.nox;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Search_adapter extends ArrayAdapter {
    ArrayList<Search_Model> model = new ArrayList<>();
    Context context;
    int num;


    public Search_adapter(@NonNull Context context,ArrayList<Search_Model> model, int num) {
        super(context, 0, model);
        this.context = context;
        this.model= model;
        this.num = num;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (num == 1){

            view = LayoutInflater.from(context).inflate(R.layout.profile_single_post,parent,false);

            TextView fullname = view.findViewById(R.id.fullname);
            TextView username = view.findViewById(R.id.username);
            TextView time = view.findViewById(R.id.publishedate);
            TextView title = view.findViewById(R.id.post_title);
            TextView content = view.findViewById(R.id.post_content);
            ImageView user_profile = view.findViewById(R.id.profile_image);
            ImageView post_image = view.findViewById(R.id.post_img);

            fullname.setText(model.get(position).fullname);
            username.setText(model.get(position).username);
            time.setText(model.get(position).datepublished);
            content.setText(model.get(position).content);
            title.setText(model.get(position).title);


            Picasso.with(getContext()).load(model.get(position).profileimage).into(user_profile);
            Picasso.with(getContext()).load(model.get(position).image).into(post_image);



        }
        else if (num == 2){

            view = LayoutInflater.from(context).inflate(R.layout.single_user_card,parent,false);

            TextView fullname = view.findViewById(R.id.fullname);
            TextView username = view.findViewById(R.id.username);
            TextView no_of_posts = view.findViewById(R.id.no_of_posts);
            ImageView user_profile = view.findViewById(R.id.user_profile);

            fullname.setText(model.get(position).fullname);
            username.setText(model.get(position).username);
            no_of_posts.setText(String.valueOf(model.size()));
            Picasso.with(getContext()).load(model.get(position).profileimage).into(user_profile);



        }




        return view;
    }
}
