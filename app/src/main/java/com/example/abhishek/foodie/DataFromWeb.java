package com.example.abhishek.foodie;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Abhishek on 14-03-2016.
 */

public class DataFromWeb {
    TextView output;
    static final String LOGIN_URL = "http://iems-demo.herokuapp.com/api/v1/users";
    static final String ACCESS_TOKEN_URL = "http://iems-demo.herokuapp.com/api/v1/authenticate";
    static boolean is_user_list_ready = false;
    static RequestQueue requestQueue;

    static ArrayList<User> GetUsersList() {
        //if not connected, fetch from the database.
        final ArrayList<User> m = new ArrayList<User>();
        final ProgressDialog progress;
        if (!isConnectingToInternet(MainActivity.context)) {
            progress = ProgressDialog.show(MainActivity.context, "Loading",
                    "Loading From the local database", true);
            progress.setCancelable(true);
            Toast.makeText(MainActivity.context, "Loading From the local database", Toast.LENGTH_SHORT).show();
            if (GetUsersListLocal(MainActivity.context).size() != 0) {
                progress.cancel();
                return GetUsersListLocal(MainActivity.context);
            } else {
                progress.cancel();
                Toast.makeText(MainActivity.context, "Please Connect to the Internet", Toast.LENGTH_LONG).show();
                return m;
            }
        }

        p("started looking for data");
        progress = ProgressDialog.show(MainActivity.context, "Loading",
                "fetching user list from the web", true);
        progress.setCancelable(true);
        requestQueue = Volley.newRequestQueue(MainActivity.context);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, LOGIN_URL, null,
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
                            }
                            //Since User List is ready,Updating the database
                            updateDatabase(m);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            progress.cancel();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        progress.cancel();
                    }
                }
        );
        requestQueue.add(jor);
        is_user_list_ready = true;

        return m;
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    static void getAccessTokenFromWeb() {
        final String[] AccessTokenForClient = {null};
        p("Trying to get the access token");
        requestQueue = Volley.newRequestQueue(MainActivity.context);
        //TODO: edit the json_object
        String t = "{\"client_id\": \"" + MainActivity.client_app_id + "\", \"manager_password\": \"" + MainActivity.client_app_password + "\"}";
        JSONObject json_object = null;
        try {
            json_object = new JSONObject(t);
        } catch (JSONException e) {
            p("Failed while creating the json object : " + t);
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, ACCESS_TOKEN_URL, json_object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String error_flag = null;
                        try {
                            JSONObject ja = response.getJSONObject("client");
                            AccessTokenForClient[0] = (String) ja.get("auth_token");
                            error_flag = (String) ja.get("errors");
                            if (error_flag != null)
                                MainActivity.ClientAccessToken = AccessTokenForClient[0];
                            MainActivity.token_set = true;
                        } catch (JSONException e) {
                            MainActivity.token_set = true;

                            MainActivity.ClientAccessToken = null;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.token_set = true;
                        MainActivity.ClientAccessToken = null;
                    }
                }
        );
        requestQueue.add(jor);
    }

    static void updateDatabase(final ArrayList<User> m) {

        Runnable updateThread = new Runnable() {
            @Override
            public void run() {

                DatabaseHandler dbHandler = new DatabaseHandler(MainActivity.context);
                dbHandler.updateUsers(m);
            }
        };

        updateThread.run();

    }
    static void p(String str) {
        System.out.println("" + str);
    }

    //Method to get the UserList from the database

    static ArrayList<User> GetUsersListLocal(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        ArrayList<User> m;
        m = dbHandler.getAllUsers();
        is_user_list_ready = true;
        return m;
    }

}
