package com.example.abhishek.foodie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhishek on 14-03-2016.
 */
//Just a prototype
public class DataFromWeb {


    TextView output;
    static String loginURL = "https://iems-demo.herokuapp.com/api/v1/users";
    static String data = "";


    static RequestQueue requestQueue;

    static Map<Long, User> GetUsersList() {
        final Map<Long, User> m = new HashMap<Long, User>();
        p("started looking for data");
        requestQueue = Volley.newRequestQueue(MainActivity.context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, loginURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ja = response.getJSONArray("users");
                            p("Data from web" + ja.toString());
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jsonObject = ja.getJSONObject(i);
                                long user_id = jsonObject.getLong("id");
                                String user_name = jsonObject.getString("name");
                                Boolean is_guest = jsonObject.getBoolean("guest");
                                m.put(user_id, new User(user_id, user_name, is_guest));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        requestQueue.add(jor);
        return m;
    }

    static void p(String str) {
        System.out.println("" + str);
    }

}
