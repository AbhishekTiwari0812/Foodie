package com.example.abhishek.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
            tv.setText(tv.getText() + "\n\n" + i + ",)" + entry.getValue().toString());
            i++;
        }
    }
}
