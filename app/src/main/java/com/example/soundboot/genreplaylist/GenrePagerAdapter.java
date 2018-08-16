package com.example.soundboot.genreplaylist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.soundboot.R;

import java.util.Locale;

public class GenrePagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public GenrePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        GenrePlaylistFragment fragment = null;
        if(position == 0)
            fragment = new GenrePlaylistFragment();
        else if(position == 1)
            fragment = new GenrePlaylistFragment();


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
                return mContext.getString(R.string.playlist).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.releases).toUpperCase(l);
        }

        return null;
    }
}
