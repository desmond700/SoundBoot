package com.example.soundboot;

import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.soundboot.search.ArtistConstants;
import com.example.soundboot.search.ArtistModel;
import com.example.soundboot.search.TrackAdapter;
import com.example.soundboot.search.TrackParseJson;
import com.example.soundboot.search.SectionPagerAdapter;


import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class SearchableActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout = null;
	//TextView txtJson;
    private String query;
    private SectionPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //txtJson = findViewById(R.id.textView3);
        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Search");
        }

        // Get the intent, verify the action and get the query
        handleIntent(getIntent());


    }


    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_search:
                // User chose the "Search" item, show the app search ui...
                return true;
            case R.id.action_about:
                // User chose the "favourite" action, mark the current item
                // as a favourite...


                return true;
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if(NavUtils.shouldUpRecreateTask(this, upIntent)){
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                }else{
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbuttons, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.expandActionView();

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setQuery(getIntent().getStringExtra(SearchManager.QUERY),true);
        searchView.clearFocus();

        return true;
    }


    public void handleIntent(Intent intent){
        Log.d("Action search", Intent.ACTION_SEARCH);
        Log.d("intent action", intent.getAction());
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            query = intent.getStringExtra(SearchManager.QUERY).replace(" ", "%20");
            doSearch(query);
        }
    }


    public void doSearch(String query){
        mSectionsPagerAdapter = new SectionPagerAdapter(this, getSupportFragmentManager(), query);

        // Set up the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


}
