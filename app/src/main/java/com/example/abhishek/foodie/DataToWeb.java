package com.example.abhishek.foodie;

import android.provider.Settings;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abhishek on 10-04-2016.
 */
public class DataToWeb {
    static final String URL_TO_SEND_TRANSACTION = "";

    public static void sendTransaction(Transaction t) {
        //TODO: create a string to make it parsable as JSON object

        String json_transaction = "{\"transaction\": {\"guest_transaction\": " + Boolean.toString(t.is_guest) + ", \"regular_user_id\": " + Long.toString(t.user_id) + ", \"food_type\": \" " + Integer.toString(t.food_type) + "\", \"price\": " + Float.toString(t.price) + ", \"date\": \"" + (t.time_stamp) + "\"}}";
        JSONObject json_object = null;
        try {
            json_object = new JSONObject(json_transaction);
        } catch (JSONException e) {
            p("Some error in the json string format");
            p("Transaction failed.\n Try again");
            return;
        }
        p("Transaction being sent:" + json_transaction);
        final RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_TO_SEND_TRANSACTION, json_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                p("This is the response we got" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p("Some error occurred with the transaction");
            }
        });
        requestQueue.add(request);
    }

    static void p(String str) {
        System.out.println("" + str);
    }
}
