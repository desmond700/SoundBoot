package com.example.soundboot;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class jsonParser {

    ListView fruitsList;
    ProgressDialog dialog;
    StringRequest json;

    jsonParser(final Context context, String url, final ListView listView){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(context, string, listView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        json = request;
    }

    public StringRequest getJson(){
        return json;
    }

    void parseJsonData(Context context,String jsonString, ListView listView) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray fruitsArray = object.getJSONArray("artists");
            ArrayList al = new ArrayList();

            for(int i = 0; i < fruitsArray.length(); ++i) {
                al.add(fruitsArray.getString(i));
            }

            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, al);
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }
}
