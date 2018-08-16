package com.example.soundboot.playlist;


import android.app.Activity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaylistJsonParse {

    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";
    private Activity activity;

    ArrayList<HashMap<String, String>> arraylist;

    public PlaylistJsonParse() {

    }

    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("playlists")) {
                return true;
            } else {

                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString(KEY_MSG);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }

    public ArrayList<PlaylistModel> getInfo(String response) {
        Log.d("getInfo json: ", response.toString());
        ArrayList<PlaylistModel> playlistModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("playlists")) {

                arraylist = new ArrayList<>();
                JSONArray dataArray = jsonObject.getJSONObject("playlists").getJSONArray("items");
                Log.d("track objec: ", dataArray.toString());

                for (int i = 0; i < dataArray.length(); i++) {
                    PlaylistModel playlistModel = new PlaylistModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    playlistModel.setName(dataobj.getString(PlaylistConstants.Params.NAME));
                    playlistModel.setImageUri(dataobj.getJSONArray("images").getJSONObject(0).getString(PlaylistConstants.Params.IMAGE));
                    playlistModel.setTracksHref(dataobj.getJSONObject("tracks").getString(PlaylistConstants.Params.TRACKS));
                    playlistModel.setPlaylistUri(dataobj.getString(PlaylistConstants.Params.URI));
                    playlistModelArrayList.add(playlistModel);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlistModelArrayList;
    }

}
