package com.example.abhishek.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Abhishek on 18-04-2016.
 */
public class PendingTransactionList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_transaction);
        TextView tv = (TextView) findViewById(R.id.pending_transactions);
        SharedPreferences sharedPreferences = getSharedPreferences(Transaction.FAILED_TRANSACTION_FILE, Context.MODE_PRIVATE);
        Map<String, String> keys = (Map<String, String>) sharedPreferences.getAll();
        int i = 1;
        for (final Map.Entry<String, ?> entry : keys.entrySet()) {
            JSONObject obj;
            long user_id = 0;
            float price_of_transaction = 0;
            String time_of_transaction = "";
            boolean is_guest = false;
            try {
                obj = new JSONObject((String) entry.getValue());
                user_id = (long) obj.getLong("transaction");
                price_of_transaction = (float) obj.getDouble("price");
                is_guest = obj.getBoolean("guest_transaction");
                time_of_transaction = obj.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv.setText(tv.getText() + "\n\n" + i + ".)" + "Time:" + time_of_transaction + "\nUser Id:" + user_id + "\nPrice:" + price_of_transaction + "\nIs " + (is_guest ? "" : "not ") + "a guest.");
            i++;
        }
    }
}
