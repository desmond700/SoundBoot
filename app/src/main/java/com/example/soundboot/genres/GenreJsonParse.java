package com.example.soundboot.genres;

import android.app.Activity;
import android.util.Log;

import com.example.soundboot.genreplaylist.GenreConstants;
import com.example.soundboot.genreplaylist.GenreModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GenreJsonParse {

    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";

    ArrayList<HashMap<String, String>> arraylist;

    public GenreJsonParse() {

    }

    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("categories")) {
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

    public ArrayList<GenreModel> getInfo(String response) {
        Log.d("getInfo json: ", response.toString());
        ArrayList<GenreModel> genreModelArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("categories")) {

                arraylist = new ArrayList<>();
                JSONArray dataArray = jsonObject.getJSONObject("categories").getJSONArray("items");
                Log.d("track objec: ", dataArray.toString());

                for (int i = 0; i < dataArray.length(); i++) {
                    GenreModel genreModel = new GenreModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    genreModel.setName(dataobj.getString(GenreConstants.Params.NAME));
                    genreModel.setImageUrl(dataobj.getJSONArray("icons").getJSONObject(0).getString(GenreConstants.Params.IMAGE));
                    genreModel.setGenreHref(dataobj.getString(GenreConstants.Params.genreHref));
                    genreModelArrayList.add(genreModel);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return genreModelArrayList;
    }

}
