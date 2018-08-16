package com.example.soundboot.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.soundboot.R;

import java.util.Locale;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;
    protected Bundle query = new Bundle();

    public SectionPagerAdapter(Context context, FragmentManager fm, String query) {
        super(fm);
        mContext = context;
        this.query.putString("value",query);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0){
            fragment = new TrackFragment();
            fragment.setArguments(query);
        }
        else if(position == 1){
            fragment = new AlbumFragment();
            fragment.setArguments(query);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public  CharSequence getPageTitle(int position){
        Locale l = Locale.getDefault();

        switch (position){
            case 0:
                return mContext.getString(R.string.tracks).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.albums).toUpperCase(l);
        }

        return null;
    }
}
