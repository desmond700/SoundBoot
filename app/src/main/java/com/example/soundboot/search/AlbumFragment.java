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
import com.example.soundboot.genreplaylist.GenreConstants;
import com.example.soundboot.genreplaylist.GenreModel;
import com.example.soundboot.genres.GenreAdapter;
import com.example.soundboot.genres.GenreJsonParse;
import com.example.soundboot.jsonfetch.HttpRequest;
import com.example.soundboot.jsonfetch.JsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    private AlbumParseJson parseContent;
    private RecyclerView recyclerView;
    private ArrayList<ArtistModel> albumModelArrayList;
    private AlbumAdapter albumAdapter;
    private String query;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        parseContent = new AlbumParseJson();
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

        if (!JsonUtils.isNetworkAvailable(view.getContext())) {
            Toast.makeText(view.getContext(), "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonUtils.showSimpleProgressDialog(view.getContext());
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
                onTaskCompleted(result, view);
            }
        }.execute();
    }


    public void onTaskCompleted(String response, View view) {


        if (parseContent.isSuccess(response)) {
            JsonUtils.removeSimpleProgressDialog();  //will remove progress dialog
            recyclerView = view.findViewById(R.id.recyclerview);
            albumModelArrayList = parseContent.getInfo(response);
            albumAdapter = new AlbumAdapter(view.getContext(), albumModelArrayList);
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        }else {
            Toast.makeText(view.getContext(), parseContent.getErrorCode(response), Toast.LENGTH_SHORT).show();
        }
    }

}
