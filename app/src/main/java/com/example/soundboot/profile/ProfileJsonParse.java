package com.example.soundboot.profile;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileJsonParse {

    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";
    private Activity activity;

    ArrayList<HashMap<String, String>> arraylist;

    public ProfileJsonParse() {

    }

    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("display_name")) {
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

    public ArrayList<ProfileModel> getInfo(String response) {
        Log.d("getInfo json: ", response);
        ArrayList<ProfileModel> profileModelArrayList = new ArrayList<>();
        ProfileModel profileModel = new ProfileModel();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("display_name")) {

                profileModel.setName(jsonObject.getString(ProfileConstant.Params.NAME));
                profileModel.setImageUrl(jsonObject.getJSONArray("images").getJSONObject(0).getString(ProfileConstant.Params.IMAGE));
                profileModelArrayList.add(profileModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return profileModelArrayList;
    }


}
