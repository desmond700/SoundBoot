package com.example.soundboot;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.soundboot.genres.GenreAdapter;
import com.example.soundboot.genreplaylist.GenreConstants;
import com.example.soundboot.genreplaylist.GenrePagerAdapter;

public class GenreActivity extends AppCompatActivity {

    private GenrePagerAdapter mGenrePagerAdapter;
    private ViewPager mViewPager;
	private String genre;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Genres");
        }
		
		Intent intent = getIntent();
		genre = intent.getStringExtra(GenreAdapter.EXTRA_MESSAGE).toLowerCase();
		
		GenreConstants.URL = genre+"/playlists?limit=50";

        mGenrePagerAdapter = new GenrePagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mGenrePagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem){

                        if(menuItem.getTitle().equals("Home")){
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                        // Set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();


                        return true;
                    }
                });
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
                mDrawerLayout.openDrawer(GravityCompat.START);

                return true;
            default:
                // If we got here, the user's action was not rescognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbuttons, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        return true;
    }
}
