package com.example.soundboot;

import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundboot.jsonfetch.HttpRequest;
import com.example.soundboot.jsonfetch.JsonUtils;
import com.example.soundboot.profile.Profile;
import com.example.soundboot.profile.ProfileConstant;
import com.example.soundboot.profile.ProfileJsonParse;
import com.example.soundboot.profile.ProfileModel;
import com.example.soundboot.viewpager.SectionPagerAdapter;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements
        Player.NotificationCallback, ConnectionStateCallback {


    @SuppressWarnings("spellCheckingInspection")
    private static final String CLIENT_ID = "af0e0e7bbb5749c5b0140446c15265db";

    @SuppressWarnings("spellCheckingInspection")
    private static final String REDIRECT_URI = "https://github.com/spotify/web-api/issues/487";

    private static String token;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;


    public static final String TAG = "SpotifySdkDemo";

    private SpotifyPlayer mPlayer;

    private PlaybackState mCurrentPlaybackState;

    /**
     * Used to get notifications from the system about the current network state in order
     * to pass them along to
     * {@link com.spotify.sdk.android.player.SpotifyPlayer#setConnectivityStatus(Player.OperationCallback, Connectivity)}
     * Note that this implies <pre>android.permission.ACCESS_NETWORK_STATE</pre> must be
     * declared in the manifest. Not setting the correct network state in the SDK may
     * result in strange behavior.
     */
    private BroadcastReceiver mNetworkStateReceiver;

    /**
     * Used to log messages to a {@link android.widget.TextView} in this activity.
     */
    private TextView name;
    private ImageView image;
    private TextView mMetadataText;
    private EditText mSeekEditText;
    private ScrollView mStatusTextScrollView;
    private DrawerLayout mDrawerLayout;
    private Metadata mMetadata;

    private SectionPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProfileJsonParse parsejson;
    private Profile profile;
	private final int jsoncode = 1;

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            logStatus("OK!");
        }

        @Override
        public void onError(Error error) {
            logStatus("ERROR:" + error);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
        }



        parsejson = new ProfileJsonParse();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener(){
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem){
					// Set item as selected to persist highlight

                    if(menuItem.getTitle().equals("Home")){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                    menuItem.setChecked(true);
					// Close drawer when item is tapped
					mDrawerLayout.closeDrawers();


					return true;
				}
			});

        // Get a reference to any UI widgets that we'll need to use later
        mMetadataText =  findViewById(R.id.metadata);

        // The only thing that's different is we added the 5 lines below.
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    public static String getToken(){
        return token;
    }
	
	
	public void profile(){
        ProfileConstant.URL = "https://api.spotify.com/v1/users/eoz655s86p0zrct3t64isfuzx";

		if (!JsonUtils.isNetworkAvailable(HomeActivity.this)) {
            Toast.makeText(HomeActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        //JsonUtils.showSimpleProgressDialog(HomeActivity.this);
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                Log.d("Profile Url: ", ProfileConstant.URL);
                Log.d("Profile Token: ", HomeActivity.getToken());
                try {
                    HttpRequest req = new HttpRequest(ProfileConstant.URL, HomeActivity.getToken());
                    //response = req.prepare(HttpRequest.Method.GET).withData(map).sendAndReadString();
                    response = req.json(HttpRequest.Method.GET);
                    Log.d("Profile Json: ", response.toString());
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                //do something with response
                Log.d("Profile", result);
                onTaskCompleted(result, jsoncode);
            }
        }.execute();
	}
	
	public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case jsoncode:
                ArrayList<ProfileModel> profileModelArrayList;
                if (parsejson.isSuccess(response)) {
                    //JsonUtils.removeSimpleProgressDialog();  //will remove progress dialog
                    profileModelArrayList = parsejson.getInfo(response);
                    profile = new Profile(this, profileModelArrayList);

                    NavigationView mView = findViewById(R.id.nav_view);

                    View navView = mView.inflateHeaderView(R.layout.nav_header);

                    name = navView.findViewById(R.id.username);
                    image = navView.findViewById(R.id.profile_img);

                    Log.d("Profile name", profile.getUsername());
                    Log.d("Profile image", profile.getImageUrl());

                    Picasso.get()
                            .load(profile.getImageUrl())
                            .resize(90,90)
                            .noFade()
                            .into(image);
                    name.setText(profile.getUsername());
					
                }else {
                    Toast.makeText(HomeActivity.this, parsejson.getErrorCode(response), Toast.LENGTH_SHORT).show();
                }
        }
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

    /**
     * Registering for connectivity changes in Android does not actually deliver them to
     * us in the delivered intent.
     *
     * @param context Android context
     * @return Connectivity state to be passed to the SDK
     */
    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    //     _         _   _                _   _           _   _
    //    / \  _   _| |_| |__   ___ _ __ | |_(_) ___ __ _| |_(_) ___  _ __
    //   / _ \| | | | __| '_ \ / _ \ '_ \| __| |/ __/ _` | __| |/ _ \| '_  \
    //  / ___ \ |_| | |_| | | |  __/ | | | |_| | (_| (_| | |_| | (_) | | | |
    // /_/   \_\__,_|\__|_| |_|\___|_| |_|\__|_|\___\__,_|\__|_|\___/|_| |_|
    //

    private void openLoginWindow() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    onAuthenticationComplete(response);
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.d("Auth error: ", response.getError().toString());
                    break;

                // Most likely auth flow was cancelled
                default:
            }
        }
    }

    private void onAuthenticationComplete(AuthenticationResponse authResponse) {
        // Once we have obtained an authorization token, we can proceed with creating a Player.
        Log.d("Status","Got authentication token");

        token = authResponse.getAccessToken();

        if (mPlayer == null) {
            Config playerConfig = new Config(getApplicationContext(), authResponse.getAccessToken(), CLIENT_ID);
            // Since the Player is a static singleton owned by the Spotify class, we pass "this" as
            // the second argument in order to refcount it properly. Note that the method
            // Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
            // one passed in here. If you pass different instances to Spotify.getPlayer() and
            // Spotify.destroyPlayer(), that will definitely result in resource leaks.

            mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer player) {
                    logStatus("-- Player initialized --");
                    if(mOperationCallback != null){
                        player.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(HomeActivity.this));
                        player.addNotificationCallback(HomeActivity.this);
                        player.addConnectionStateCallback(HomeActivity.this);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    logStatus("Error in initialization: " + error.getMessage());
                }
            });
        } else {
            mPlayer.login(authResponse.getAccessToken());
        }
		
		profile();

        mSectionsPagerAdapter = new SectionPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void updateView() {
        boolean loggedIn = isLoggedIn();

        // Login button should be the inverse of the logged in state
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setText(loggedIn ? R.string.logout_button_label : R.string.login_button_label);


        if (mMetadata != null) {
            findViewById(R.id.skip_next_button).setEnabled(mMetadata.nextTrack != null);
            findViewById(R.id.skip_prev_button).setEnabled(mMetadata.prevTrack != null);
            findViewById(R.id.pause_button).setEnabled(mMetadata.currentTrack != null);
        }

        final ImageView coverArtView = findViewById(R.id.cover_art);
        if (mMetadata != null && mMetadata.currentTrack != null) {
            final String durationStr = String.format(" (%dms)", mMetadata.currentTrack.durationMs);
            mMetadataText.setText(mMetadata.contextName + "\n" + mMetadata.currentTrack.name + " - " + mMetadata.currentTrack.artistName + durationStr);

            Picasso.get()
                    .load(mMetadata.currentTrack.albumCoverWebUrl)
                    .transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            // really ugly darkening trick
                            final Bitmap copy = source.copy(source.getConfig(), true);
                            source.recycle();
                            final Canvas canvas = new Canvas(copy);
                            canvas.drawColor(0xbb000000);
                            return copy;
                        }

                        @Override
                        public String key() {
                            return "darken";
                        }
                    })
                    .into(coverArtView);
        } else {
            mMetadataText.setText("<nothing is playing>");
            coverArtView.setBackground(null);
        }

    }


    private void logStatus(String status) {
        Log.i(TAG, status);

    }

    private boolean isLoggedIn() {
        return mPlayer != null && mPlayer.isLoggedIn();
    }

    @Override
    public void onLoggedIn() {
        logStatus("Login complete");
    }

    @Override
    public void onLoggedOut() {
        logStatus("Logout complete");
    }

    public void onLoginFailed(Error error) {
        logStatus("Login error "+ error);
    }

    @Override
    public void onTemporaryError() {
        logStatus("Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(final String message) {
        logStatus("Incoming connection message: " + message);
    }


    @Override
    public void onPlaybackEvent(PlayerEvent event) {
        // Remember kids, always use the English locale when changing case for non-UI strings!
        // Otherwise you'll end up with mysterious errors when running in the Turkish locale.
        // See: http://java.sys-con.com/node/46241
        logStatus("Event: " + event);
        mCurrentPlaybackState = mPlayer.getPlaybackState();
        mMetadata = mPlayer.getMetadata();
        Log.i(TAG, "Player state: " + mCurrentPlaybackState);
        Log.i(TAG, "Metadata: " + mMetadata);
    }

    @Override
    public void onPlaybackError(Error error) {
        logStatus("Err: " + error);
    }

}






