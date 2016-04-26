package com.example.abhishek.foodie;

import android.content.SharedPreferences;
import android.widget.Toast;

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
    static final String URL_TO_SEND_TRANSACTION = "http://iems-demo.herokuapp.com/api/v1/transaction";

    public static void sendTransaction(final Transaction t) {
        final String json_transaction = "{\"transaction\": {\"guest_transaction\": " + Boolean.toString(t.is_guest) + ", \"regular_user_id\": " + Long.toString(t.user_id) + ", \"food_type\": \"" + t.food_type + "\", \"price\": " + Float.toString(t.price) + ", \"date\": \"" + (t.time_stamp) + "\" ,\"image\":\"" + "data:image/png;base64," + t.user_img + "\"},\"access_token\":\"" + MainActivity.ClientAccessToken + "\"}";
        p(json_transaction);
        JSONObject json_object = null;
        try {
            json_object = new JSONObject(json_transaction);
        } catch (JSONException e) {
            t.addToFailedTransactionList(json_transaction, t.time_stamp.toString());
            //Toast.makeText(UserProfile.context, "Fatal error:Some transactions failed, inform the manager!!", Toast.LENGTH_SHORT).show();
            return;
        }
        p("Transaction being sent:" + json_transaction);
        final RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_TO_SEND_TRANSACTION, json_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                p("This is the response we got" + response.toString());
                try {
                    String temp_object = response.getString("errors");
                    p("This is the temp object");
                    if (temp_object != null) {
                        if (temp_object.equals("Not Authenticated")) {
                            t.addToFailedTransactionList(json_transaction, t.time_stamp.toString());
                            if (MainActivity.clientAccessTokenFetcher != null) {
                                try {
                                    SharedPreferences.Editor editor = MainActivity.clientAccessTokenFetcher.edit();
                                    editor.putString("MyAccessToken", null);
                                    editor.putString("MyAppId", null);
                                    editor.putString("MyAppPassword", null);
                                    editor.commit();
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(UserProfile.context, "Transaction is successful", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p("Some error occurred with the transaction");
                t.addToFailedTransactionList(json_transaction, t.time_stamp.toString());
                //TODO: if error caused is because of some other reason, inform the manager!!!
            }
        });
        requestQueue.add(request);
    }

    static void p(String str) {
        System.out.println("" + str);
    }
}
