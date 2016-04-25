package com.example.abhishek.foodie;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
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
import java.util.Map;

/**
 * Created by Abhishek on 18-04-2016.
 */
public class BackgroundServices extends Service {
    static final String URL_TO_SEND_TRANSACTION = "http://iems-demo.herokuapp.com/api/v1/transaction";
    Thread transaction_sender;
    static SharedPreferences sharedPreferences;
    static String loginURL = "https://iems-demo.herokuapp.com/api/v1/users";
    static RequestQueue requestQueue;
    Thread data_fetch;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    void retryFailedTransactions() {
        Map<String, String> keys = (Map<String, String>) sharedPreferences.getAll();
        for (final Map.Entry<String, ?> entry : keys.entrySet()) {
            String json_transaction = entry.getValue().toString();
            JSONObject json_object = null;
            try {
                json_object = new JSONObject(json_transaction);
            } catch (JSONException e) {
                Toast.makeText(UserProfile.context, "Fatal error:Some transactions failed, inform the manager!!", Toast.LENGTH_SHORT).show();
            }
            final RequestQueue requestQueue = Volley.newRequestQueue(BackgroundServices.this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_TO_SEND_TRANSACTION, json_object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(UserProfile.context, "Transaction is successful", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.remove(entry.getKey());
                    e.apply();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // Toast.makeText(UserProfile.context, "Transaction failed in service.Fatal error", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Background Service started.", Toast.LENGTH_SHORT).show();
        sharedPreferences = getSharedPreferences(Transaction.FAILED_TRANSACTION_FILE, Context.MODE_PRIVATE);
        System.out.print("Hello");
        transaction_sender = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 1;
                while (count < 10000) {
                    retryFailedTransactions();
                    try {
//                        Toast.makeText(MainActivity.context, "There", Toast.LENGTH_SHORT).show();
                        Thread.sleep(1000 * 60 * 1);        //retrying every 5 minutes.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        transaction_sender.start();

        data_fetch = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //Toast.makeText(MainActivity.context,"thread running",Toast.LENGTH_SHORT).show();
                    updateDatabaseOnService();
                    try {
                        Thread.sleep(1000 * 60 * 5);        //Updating every 5 minutes.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        data_fetch.start();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Fatal action.Transactions are not being recorded anymore.Please restart the Foodie app", Toast.LENGTH_LONG).show();
        /*
        boolean success = false;
        while (!success) {
            try {
                transaction_sender.join();
                success = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
        super.onDestroy();
    }

    void updateDatabaseOnService() {
        final ArrayList<User> m = new ArrayList<User>();
        // p("started looking for data");
        requestQueue = Volley.newRequestQueue(BackgroundServices.this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, loginURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ja = response.getJSONArray("users");
                            //p("Data from web" + ja.toString());
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jsonObject = ja.getJSONObject(i);
                                long user_id = jsonObject.getLong("id");
                                String user_name = jsonObject.getString("name");
                                Boolean is_guest = jsonObject.getBoolean("guest");
                                //    p("Adding:" + user_name);
                                m.add(new User(user_id, user_name, is_guest));
                            }
                            //Since User List is ready,Updating the database
                            DataFromWeb.updateDatabase(m);

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
        //    is_user_list_ready = true;
    }
}
