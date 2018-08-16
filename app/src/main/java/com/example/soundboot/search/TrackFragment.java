package com.example.soundboot.search;


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
public class TrackFragment extends Fragment {

    private TrackParseJson parseContent;
    private RecyclerView recyclerView;
    private ArrayList<ArtistModel> artistModelArrayList;
    private TrackAdapter trackAdapter;
    private final int jsoncode = 1;
    private String query;

    public TrackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        parseContent = new TrackParseJson();
        query = getArguments().getString("value");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ArtistConstants.URL = "https://api.spotify.com/v1/search/?type=track&q="+query+"&limit=50";

        try {
            parseJson(view);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJson(final View view) throws IOException, JSONException {

        if (!JsonUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonUtils.showSimpleProgressDialog(getContext());
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void[] params) {
                String response = "";
                HashMap<String, String> map = new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(ArtistConstants.URL, HomeActivity.getToken());
                    //response = req.prepare(HttpRequest.Method.GET).withData(map).sendAndReadString();
                    response = req.json(HttpRequest.Method.GET);
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }

            protected void onPostExecute(String result) {
                //do something with response
                onTaskCompleted(view, result, jsoncode);
            }
        }.execute();
    }


    public void onTaskCompleted(View view, String response, int serviceCode) {
        switch (serviceCode) {
            case jsoncode:

                if (parseContent.isSuccess(response)) {
                    JsonUtils.removeSimpleProgressDialog();  //will remove progress dialog
                    recyclerView = view.findViewById(R.id.recyclerview);
                    artistModelArrayList = parseContent.getInfo(response);
                    trackAdapter = new TrackAdapter(getContext(),artistModelArrayList);
                    recyclerView.setAdapter(trackAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }else {
                    Toast.makeText(getContext(), parseContent.getErrorCode(response), Toast.LENGTH_SHORT).show();
                }
        }
    }
}
