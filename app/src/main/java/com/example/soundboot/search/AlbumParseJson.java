package com.example.soundboot.search;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AlbumParseJson {

    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";

    ArrayList<HashMap<String, String>> arraylist;

    public AlbumParseJson() {

    }
 
   public boolean isSuccess(String response) {
 
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("tracks")) {
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
 
   public ArrayList<ArtistModel> getInfo(String response) {
       ArrayList<ArtistModel> artistModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("tracks")) {

                arraylist = new ArrayList<>();
                JSONArray dataArray = jsonObject.getJSONObject("tracks").getJSONArray("items");

                for (int i = 0; i < dataArray.length(); i++) {
                    ArtistModel artistModel = new ArtistModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    artistModel.setAlbumTitle(dataobj.getJSONObject("album").getString(ArtistConstants.Params.NAME));
                    artistModel.setAlbumCover(dataobj.getJSONObject("album").getJSONArray("images").getJSONObject(1).getString(ArtistConstants.Params.IMAGE));
                    artistModel.setAlbumUri(dataobj.getJSONObject("album").getString(ArtistConstants.Params.TRACK));
                    artistModelArrayList.add(artistModel);
                }
            }
 
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artistModelArrayList;
    }
}