package com.example.abhishek.foodie;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Abhishek on 18-04-2016.
 */
public class BackgroundServices extends Service {
    static final String URL_TO_SEND_TRANSACTION = "http://iems-demo.herokuapp.com/api/v1/transaction";
    Thread transaction_sender;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        sharedPreferences = getSharedPreferences(Transaction.FAILED_TRANSACTION_FILE, Context.MODE_PRIVATE);
        transaction_sender = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 1;
                while (count < 10000) {
                    retryFailedTransactions();
                    try {
                        Thread.sleep(1000 * 60 * 5);        //retrying every 5 minutes.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        transaction_sender.start();


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
            final RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.context);
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
                    Toast.makeText(UserProfile.context, "Transaction failed in service.Fatal error", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Background Service started.", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Fatal error.Please restart the Foodie app", Toast.LENGTH_LONG).show();
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
}
