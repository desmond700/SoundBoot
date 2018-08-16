package com.example.soundboot.search;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackParseJson {
 
    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";

    ArrayList<HashMap<String, String>> arraylist;
 
    public TrackParseJson() {
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
                    artistModel.setName(dataobj.getJSONArray("artists").getJSONObject(0).getString(ArtistConstants.Params.NAME));
                    artistModel.setImageUrl(dataobj.getJSONObject("album").getJSONArray("images").getJSONObject(1).getString(ArtistConstants.Params.IMAGE));
                    artistModel.setTrackTitle(dataobj.getString(ArtistConstants.Params.TRACK_TITLE));
                    artistModel.setTrackUrl(dataobj.getString(ArtistConstants.Params.TRACK));
                    artistModel.setTrackDuration(milliToMin_Secs(dataobj.getString(ArtistConstants.Params.TRACK_DURATION)));
                    artistModelArrayList.add(artistModel);
                }
            }
 
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artistModelArrayList;
    }

    public String milliToMin_Secs(String str){
        long millis = Long.parseLong(str);
        int minutes = (int)(millis / (1000 * 60));
        int seconds = (int)(millis / 1000) % 60;
        return String.format("%d:%02d",minutes,seconds);
    }
 
}