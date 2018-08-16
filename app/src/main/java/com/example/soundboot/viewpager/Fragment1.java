package com.example.soundboot.viewpager;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.soundboot.HomeActivity;
import com.example.soundboot.R;
import com.example.soundboot.jsonfetch.HttpRequest;
import com.example.soundboot.jsonfetch.JsonUtils;
import com.example.soundboot.playlist.PlaylistAdapter;
import com.example.soundboot.playlist.PlaylistConstants;
import com.example.soundboot.playlist.PlaylistJsonParse;
import com.example.soundboot.playlist.PlaylistModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    private PlaylistJsonParse parseContent;
    private RecyclerView recyclerView;
    private ArrayList<PlaylistModel> playlistModelArrayList;
    private PlaylistAdapter playlistAdapter;

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        parseContent = new PlaylistJsonParse();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        PlaylistConstants.URL = "https://api.spotify.com/v1/browse/featured-playlists?limit=50";
        try {
            parseJson(view);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJson(final View view) throws IOException, JSONException {

        if (!JsonUtils.isNetworkAvailable(view.getContext())) {
            Toast.makeText(view.getContext(), "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonUtils.showSimpleProgressDialog(view.getContext());
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {

                    HttpRequest req = new HttpRequest(PlaylistConstants.URL, HomeActivity.getToken());
                    //response = req.prepare(HttpRequest.Method.GET).withData(map).sendAndReadString();
                    response = req.json(HttpRequest.Method.GET);
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }
            @Override
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss", result);
                onTaskCompleted(result, view);
            }
        }.execute();
    }


    public void onTaskCompleted(String response, View view) {
        Log.d("responsejson", response.toString());


        if (parseContent.isSuccess(response)) {
            Log.d("isSuccess", "success");
            JsonUtils.removeSimpleProgressDialog();  //will remove progress dialog
            recyclerView = view.findViewById(R.id.recyclerview);
            Log.d("RC", "recyclerView");
            playlistModelArrayList = parseContent.getInfo(response);
            Log.d("getInfo", playlistModelArrayList.toString());
            playlistAdapter = new PlaylistAdapter(view.getContext(), playlistModelArrayList);
            recyclerView.setAdapter(playlistAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        }else {
            Toast.makeText(view.getContext(), parseContent.getErrorCode(response), Toast.LENGTH_SHORT).show();
        }
    }

}
