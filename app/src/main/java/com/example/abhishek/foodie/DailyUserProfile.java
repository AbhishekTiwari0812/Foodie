package com.example.abhishek.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Abhishek on 16-02-2016.
 */
public class DailyUserProfile extends AppCompatActivity {
    TextView breakfast_price, lunch_price, dinner_price, special_item_price;
    String user_name;
    String user_id;
    static final String PREF_FILE_NAME = "my_price_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_page);
        breakfast_price = (TextView) findViewById(R.id.tv_breakfast_price);
        lunch_price = (TextView) findViewById(R.id.tv_lunch_price);
        dinner_price = (TextView) findViewById(R.id.tv_dinner_price);
        special_item_price = (TextView) findViewById(R.id.tv_special_item_price);
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String current_breakfast_price = sharedPreferences.getString("breakfast", "0");
        String current_lunch_price = sharedPreferences.getString("lunch", "0");
        String current_dinner_price = sharedPreferences.getString("dinner", "0");
        String current_special_item_price = sharedPreferences.getString("special_item", "0");
        breakfast_price.setText("Rs " + current_breakfast_price);
        lunch_price.setText("Rs " + current_lunch_price);
        dinner_price.setText("Rs " + current_dinner_price);
        special_item_price.setText("Rs " + current_special_item_price);
        //TODO: start taking photo activity
        //TODO: set the profile name and profile picture fetched from the database
        //TODO: remove the buttons and add a counter.

    }
}
