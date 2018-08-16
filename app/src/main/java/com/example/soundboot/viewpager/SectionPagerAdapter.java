package com.example.soundboot.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.soundboot.HomeActivity;
import com.example.soundboot.R;

import java.util.Locale;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public SectionPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0)
            fragment = new Fragment1();
        else if(position == 1)
            fragment = new Fragment2();


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
                return mContext.getString(R.string.featured).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.genres).toUpperCase(l);
        }

        return null;
    }
}
