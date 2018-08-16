package com.example.soundboot.player;

import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundboot.HomeActivity;
import com.example.soundboot.R;
import com.example.soundboot.search.TrackAdapter;
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

public class PlayerActivity extends AppCompatActivity implements
        Player.NotificationCallback, ConnectionStateCallback {

    //   ____                _              _
    //  / ___|___  _ __  ___| |_ __ _ _ __ | |_ ___
    // | |   / _ \| '_ \/ __| __/ _` | '_ \| __/ __|
    // | |__| (_) | | | \__ \ || (_| | | | | |_\__ \
    //  \____\___/|_| |_|___/\__\__,_|_| |_|\__|___/
    //

    @SuppressWarnings("spellCheckingInspection")
    private static final String CLIENT_ID = "af0e0e7bbb5749c5b0140446c15265db";

    @SuppressWarnings("spellCheckingInspection")
    private static final String REDIRECT_URI = "https://github.com/spotify/web-api/issues/487";

    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_SONG_URI = "spotify:track:6KywfgRqvgvfJc3JRwaZdZ";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_SONG_MONO_URI = "spotify:track:1FqY3uJypma5wkYw66QOUi";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_SONG_48kHz_URI = "spotify:track:3wxTNS3aqb9RbBLZgJdZgH";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_PLAYLIST_URI = "spotify:user:spotify:playlist:2yLXxKhhziG2xzy7eyD4TD";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_ALBUM_URI = "spotify:album:2lYmxilk8cXJlxxXmns1IU";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_QUEUE_SONG_URI = "spotify:track:5EEOjaJyWvfMglmEwf9bG3";

    protected static String token;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    /**
     * UI controls which may only be enabled after the player has been initialized,
     * (or effectively, after the user has logged in).
     */
    private static final int[] REQUIRES_INITIALIZED_STATE = {
            //R.id.play_album_button,
            //R.id.play_playlist_button,
            R.id.pause_button,
            };

    /**
     * UI controls which should only be enabled if the player is actively playing.
     */
    private static final int[] REQUIRES_PLAYING_STATE = {
            R.id.skip_next_button,
            R.id.skip_prev_button,
            //R.id.queue_song_button,
            R.id.toggle_shuffle_button,
            R.id.toggle_repeat_button,
            };
    public static final String TAG = "SpotifySdkDemo";

    //  _____ _      _     _
    // |  ___(_) ___| | __| |___
    // | |_  | |/ _ \ |/ _` / __|
    // |  _| | |  __/ | (_| \__ \
    // |_|   |_|\___|_|\__,_|___/
    //

    /**
     * The player used by this activity. There is only ever one instance of the player,
     * which is owned by the {@link com.spotify.sdk.android.player.Spotify} class and refcounted.
     * This means that you may use the Player from as many Fragments as you want, and be
     * assured that state remains consistent between them.
     * <p/>
     * However, each fragment, activity, or helper class <b>must</b> call
     * {@link com.spotify.sdk.android.player.Spotify#destroyPlayer(Object)} when they are no longer
     * need that player. Failing to do so will result in leaked resources.
     */
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
    private TextView mMetadataText;

    private EditText mSeekEditText;

    private String uri;
    private Metadata mMetadata;
    private TextView mDuration;
    private TextView mCounter;
    private DrawerLayout mDrawerLayout;
    private SeekBar seekBar;
    private boolean mUserIsSeeking = false;
    private int timeElapsed;
    private ImageButton btnPlay;
    private ImageButton btnShuffle;
    private ImageButton btnRepeat;
    private ShareActionProvider mShareActionProvider;
    private Handler durationHandler = new Handler();

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

    //  ___       _ _   _       _ _          _   _
    // |_ _|_ __ (_) |_(_) __ _| (_)______ _| |_(_) ___  _ __
    //  | || '_ \| | __| |/ _` | | |_  / _` | __| |/ _ \| '_ \
    //  | || | | | | |_| | (_| | | |/ / (_| | |_| | (_) | | | |
    // |___|_| |_|_|\__|_|\__,_|_|_/___\__,_|\__|_|\___/|_| |_|
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        uri = intent.getStringExtra(TrackAdapter.EXTRA_MESSAGE);

        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Player");
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Get a reference to any UI widgets that we'll need to use later
        mMetadataText =  findViewById(R.id.metadata);
        mDuration = findViewById(R.id.duration);
        mCounter = findViewById(R.id.counter);
        btnPlay = findViewById(REQUIRES_INITIALIZED_STATE[0]);
        btnShuffle = findViewById(REQUIRES_PLAYING_STATE[2]);
        btnRepeat = findViewById(REQUIRES_PLAYING_STATE[3]);

        updateView();
        logStatus("Ready");

        // The only thing that's different is we added the 5 lines below.
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_search:
                // User chose the "Search" item, show the app search ui...
                return true;
            case R.id.action_share:
                // User chose the "favourite" action, mark the current item
                // as a favourite...
                Log.d("share click", "share clicked");
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBodyText = "Chsck it out. Your message goes here";
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(shareIntent, "Shearing Option"));

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
        inflater.inflate(R.menu.player_actionbuttons, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = "Chsck it out. Your message goes here";
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        mShareActionProvider.setShareIntent(shareIntent);
        //startActivity(Intent.createChooser(shareIntent, "Shearing Option"));

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent){
        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set up the broadcast receiver for network events. Note that we also unregister
        // this receiver again in onPause().
        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mPlayer != null) {
                    Connectivity connectivity = getNetworkConnectivity(getBaseContext());
                    logStatus("Network state changed: " + connectivity.toString());
                    mPlayer.setConnectivityStatus(mOperationCallback, connectivity);
                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);

        if (mPlayer != null) {
            mPlayer.addNotificationCallback(PlayerActivity.this);
            mPlayer.addConnectionStateCallback(PlayerActivity.this);
        }
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
    //   / _ \| | | | __| '_ \ / _ \ '_ \| __| |/ __/ _` | __| |/ _ \| '_ \
    //  / ___ \ |_| | |_| | | |  __/ | | | |_| | (_| (_| | |_| | (_) | | | |
    // /_/   \_\__,_|\__|_| |_|\___|_| |_|\__|_|\___\__,_|\__|_|\___/|_| |_|
    //

    /*private void openLoginWindow() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("uri: ", uri);
        onAuthenticationComplete();
        mPlayer.playUri(mOperationCallback, uri, 0, 0);
    }

    private void onAuthenticationComplete() {
        // Once we have obtained an authorization token, we can proceed with creating a Player.
        logStatus("Got authentication token");

        if (mPlayer == null) {
            Config playerConfig = new Config(getApplicationContext(), HomeActivity.getToken(), CLIENT_ID);
            // Since the Player is a static singleton owned by the Spotify class, we pass "this" as
            // the second argument in order to refcount it properly. Note that the method
            // Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
            // one passed in here. If you pass different instances to Spotify.getPlayer() and
            // Spotify.destroyPlayer(), that will definitely result in resource leaks.
            token = HomeActivity.getToken();

            mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer player) {
                    logStatus("-- Player initialized --");
                    player.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(PlayerActivity.this));
                    player.addNotificationCallback(PlayerActivity.this);
                    player.addConnectionStateCallback(PlayerActivity.this);
                    // Trigger UI refresh
                    updateView();
                }

                @Override
                public void onError(Throwable error) {
                    logStatus("Error in initialization: " + error.getMessage());
                }
            });
        } else {
            mPlayer.login(HomeActivity.getToken());
        }
    }

    //  _   _ ___   _____                 _
    // | | | |_ _| | ____|_   _____ _ __ | |_ ___
    // | | | || |  |  _| \ \ / / _ \ '_ \| __/ __|
    // | |_| || |  | |___ \ V /  __/ | | | |_\__ \
    //  \___/|___| |_____| \_/ \___|_| |_|\__|___/
    //



    // Handler to change seekBarTime


    public String milliToMinSecs(long ms){
        long millis = ms;
        int minutes = (int)(millis / (1000 * 60));
        int seconds = (int)(millis / 1000) % 60;
        return String.format("%d:%02d",minutes,seconds);
    }

    private void updateView() {
        boolean loggedIn = true;

        // Login button should be the inverse of the logged in state
        Button loginButton = findViewById(R.id.login_button);
        //loginButton.setText(loggedIn ? R.string.logout_button_label : R.string.login_button_label);

        // Set enabled for all widgets which depend on initialized state
        for (int id : REQUIRES_INITIALIZED_STATE) {
            findViewById(id).setEnabled(loggedIn);
        }

        // Same goes for the playing state
        final boolean playing = loggedIn && mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying;
        for (int id : REQUIRES_PLAYING_STATE) {
            findViewById(id).setEnabled(playing);
        }

        if(playing)
            btnPlay.setImageResource(R.drawable.ic_outline_pause_circle_outline_24px);
        else
            btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24px);


        if (mMetadata != null) {
            findViewById(R.id.skip_next_button).setEnabled(mMetadata.nextTrack != null);
            findViewById(R.id.skip_prev_button).setEnabled(mMetadata.prevTrack != null);
            findViewById(R.id.pause_button).setEnabled(mMetadata.currentTrack != null);
        }

        final ImageView coverArtView = findViewById(R.id.cover_art);
        if (mMetadata != null && mMetadata.currentTrack != null) {
            final String durationStr = String.format(" (%dms)", mMetadata.currentTrack.durationMs);
            mMetadataText.setText(mMetadata.contextName + "\n" + mMetadata.currentTrack.name + " - " + mMetadata.currentTrack.artistName + durationStr);
            mDuration.setText(milliToMinSecs(mMetadata.currentTrack.durationMs));
            Picasso.get()
                    .load(mMetadata.currentTrack.albumCoverWebUrl)
                    .into(coverArtView);
            seekerBar(1);
            durationHandler.postDelayed(runnable, 1000);
        } else {
            mMetadataText.setText("<nothing is playing>");
            coverArtView.setBackground(null);
        }



    }

    public void seekerBar(long positionms){

        // initiate views
        seekBar = findViewById(R.id.seekBar);
        seekBar.setClickable(true);

        seekBar.setMax((int)mMetadata.currentTrack.durationMs);
        // Perform seek bar change listener event used for getting the progress value
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userSelectedPosition = 0;

            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {
                    userSelectedPosition = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = false;
                Toast.makeText(PlayerActivity.this, "Seek bar progress is: " + userSelectedPosition, Toast.LENGTH_SHORT).show();
                mPlayer.seekToPosition(mOperationCallback, userSelectedPosition);

            }
        });
    }

        // Set seekBar progress


    public Runnable runnable = new Runnable(){
            @Override
            public void run() {
                int min, currentPosition;
                String sec;

                currentPosition = (int) mPlayer.getPlaybackState().positionMs;
                //timeElapsed = (int) mMetadata.currentTrack.durationMs - currentPosition;
                seekBar.setProgress((int) mPlayer.getPlaybackState().positionMs);
                min = (currentPosition / (1000 * 60)) < 0 ? 0 : (currentPosition / (1000 * 60));
                sec = ((currentPosition / 1000) % 60) < 10 && ((currentPosition / 1000) % 60) >= 0 ? "0" + (currentPosition / 1000) % 60 : ""+(currentPosition / 1000) % 60;

                mCounter.setText(min + ":" + sec);

                durationHandler.postDelayed(this,1000);
            }
        };


    private boolean isLoggedIn() {
        return mPlayer != null && mPlayer.isLoggedIn(


        );
    }

    /*public void onLoginButtonClicked(View view) {
        if (!isLoggedIn()) {
            logStatus("Logging in");
            openLoginWindow();
        } else {
            mPlayer.logout();
        }
    }*/

    public void onPlayButtonClicked(View view) {

        logStatus("Starting playback for " + uri);
        mPlayer.playUri(mOperationCallback, uri, 0, 0);
    }

    public void onPauseButtonClicked(View view) {
        if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
            mPlayer.pause(mOperationCallback);
            btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24px);
        } else {
            mPlayer.resume(mOperationCallback);
            btnPlay.setImageResource(R.drawable.ic_outline_pause_circle_outline_24px);
        }

    }

    public void onSkipToPreviousButtonClicked(View view) {
        mPlayer.skipToPrevious(mOperationCallback);
    }

    public void onSkipToNextButtonClicked(View view) {
        mPlayer.skipToNext(mOperationCallback);
    }

    public void onQueueSongButtonClicked(View view) {
        mPlayer.queue(mOperationCallback, TEST_QUEUE_SONG_URI);
        Toast toast = Toast.makeText(this, R.string.song_queued_toast, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onToggleShuffleButtonClicked(View view) {
        mPlayer.setShuffle(mOperationCallback, !mCurrentPlaybackState.isShuffling);
    }

    public void onToggleRepeatButtonClicked(View view) {
        mPlayer.setRepeat(mOperationCallback, !mCurrentPlaybackState.isRepeating);

    }

    public void onSeekButtonClicked(View view) {
        final Integer seek = Integer.valueOf(mSeekEditText.getText().toString());
        mPlayer.seekToPosition(mOperationCallback, seek);
    }


    //   ____      _ _ _                _      __  __      _   _               _
    //  / ___|__ _| | | |__   __ _  ___| | __ |  \/  | ___| |_| |__   ___   __| |___
    // | |   / _` | | | '_ \ / _` |/ __| |/ / | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
    // | |__| (_| | | | |_) | (_| | (__|   <  | |  | |  __/ |_| | | | (_) | (_| \__ \
    //  \____\__,_|_|_|_.__/ \__,_|\___|_|\_\ |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
    //

    @Override
    public void onLoggedIn() {
        logStatus("Login complete");
        updateView();
    }

    @Override
    public void onLoggedOut() {
        logStatus("Logout complete");
        updateView();
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

    //  _____                       _   _                 _ _ _
    // | ____|_ __ _ __ ___  _ __  | | | | __ _ _ __   __| | (_)_ __   __ _
    // |  _| | '__| '__/ _ \| '__| | |_| |/ _` | '_ \ / _` | | | '_ \ / _` |
    // | |___| |  | | | (_) | |    |  _  | (_| | | | | (_| | | | | | | (_| |
    // |_____|_|  |_|  \___/|_|    |_| |_|\__,_|_| |_|\__,_|_|_|_| |_|\__, |
    //                                                                 |___/

    /**
     * Print a status message from a callback (or some other place) to the TextView in this
     * activity
     *
     * @param status Status message
     */
    private void logStatus(String status) {
        Log.i(TAG, status);
    }

    //  ____            _                   _   _
    // |  _ \  ___  ___| |_ _ __ _   _  ___| |_(_) ___  _ __
    // | | | |/ _ \/ __| __| '__| | | |/ __| __| |/ _ \| '_ \
    // | |_| |  __/\__ \ |_| |  | |_| | (__| |_| | (_) | | | |
    // |____/ \___||___/\__|_|   \__,_|\___|\__|_|\___/|_| |_|
    //

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkStateReceiver);

        // Note that calling Spotify.destroyPlayer() will also remove any callbacks on whatever
        // instance was passed as the refcounted owner. So in the case of this particular example,
        // it's not strictly necessary to call these methods, however it is generally good practice
        // and also will prevent your application from doing extra work in the background when
        // paused.
        if (mPlayer != null) {
            mPlayer.removeNotificationCallback(PlayerActivity.this);
            mPlayer.removeConnectionStateCallback(PlayerActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        // *** ULTRA-IMPORTANT ***
        // ALWAYS call this in your onDestroy() method, otherwise you will leak native resources!
        // This is an unfortunate necessity due to the different memory management models of
        // Java's garbage collector and C++ RAII.
        // For more information, see the documentation on Spotify.destroyPlayer().
        Spotify.destroyPlayer(this);
        super.onDestroy();
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
        updateView();
    }

    @Override
    public void onPlaybackError(Error error) {
        logStatus("Err: " + error);
    }
}
