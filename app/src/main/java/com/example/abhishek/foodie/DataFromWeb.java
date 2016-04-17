package com.example.abhishek.foodie;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhishek on 14-03-2016.
 */

public class DataFromWeb {
    TextView output;
    static String loginURL = "https://iems-demo.herokuapp.com/api/v1/users";
    static boolean is_user_list_ready = false;
    static RequestQueue requestQueue;

    static ArrayList<User> GetUsersList() {
        //TODO: check internet connection
        //if not connected, fetch from the database.
        //show progress dialogue.
        final ArrayList<User> m = new ArrayList<User>();
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
                                p("Adding:" + user_name);
                                m.add(new User(user_id, user_name, is_guest));
                                //add this list to the database.
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
        is_user_list_ready = true;
        return m;
    }

    static void p(String str) {
        System.out.println("" + str);
    }

}
