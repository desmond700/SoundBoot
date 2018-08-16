package com.example.soundboot.profile;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.soundboot.R;
import com.example.soundboot.playlist.PlaylistModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile {

    private ArrayList<ProfileModel> profileModelArrayList;
    private ImageView image;
    TextView name;


    public Profile(Context context, ArrayList<ProfileModel> profileModelArrayList) {

        this.profileModelArrayList = profileModelArrayList;
    }


    public String getImageUrl(){
        return profileModelArrayList.get(0).getImageUrl();
    }

    public String getUsername(){
        return profileModelArrayList.get(0).getName();
    }
}
