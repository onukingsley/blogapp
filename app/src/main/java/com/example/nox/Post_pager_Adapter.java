package com.example.nox;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Post_pager_Adapter extends FragmentPagerAdapter {
    int TotalTabs;
    Context context;


    public Post_pager_Adapter(@NonNull FragmentManager fm, Context context, int TotalTabs) {
        super(fm);
        this.context = context;
        this.TotalTabs = TotalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Personal_post_Fragment profileTab = new Personal_post_Fragment();
                return profileTab;
            case 1:
                SubscribersTab subscribersTab = new SubscribersTab();
                return  subscribersTab;
        }

        return null;
    }

    @Override
    public int getCount() {
        return TotalTabs;
    }
}
