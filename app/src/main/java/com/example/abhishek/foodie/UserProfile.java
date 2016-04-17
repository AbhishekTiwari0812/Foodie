package com.example.abhishek.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    //0 for breakfast, 1 for lunch, 2 dinner ,3 for special item.
    TextView[] price_textview;
    TextView user_display_name;
    static final String PREF_FILE_NAME = "my_price_file";
    User current_user;
    FoodMenuItem foodTaken;

    Button button_start_transaction;

    float[] prices;
    TextView[] plus_button;
    TextView[] minus_button;
    int[] counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: start taking photo activity in the background
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_page);
        int user_index = getIntent().getIntExtra("Position", 0);
        current_user = MainActivity.list_of_all_users.get(user_index);

        user_display_name = (TextView) findViewById(R.id.user_name);

        price_textview = new TextView[4];
        price_textview[0] = (TextView) findViewById(R.id.tv_0);
        price_textview[1] = (TextView) findViewById(R.id.tv_1);
        price_textview[2] = (TextView) findViewById(R.id.tv_2);
        price_textview[3] = (TextView) findViewById(R.id.tv_3);
        button_start_transaction = (Button) findViewById(R.id.bt_confirm_transaction);
        //initializing the counters
        plus_button = new TextView[4];
        plus_button[0] = (TextView) findViewById(R.id.plus0);
        plus_button[1] = (TextView) findViewById(R.id.plus1);
        plus_button[2] = (TextView) findViewById(R.id.plus2);
        plus_button[3] = (TextView) findViewById(R.id.plus3);
        minus_button = new TextView[4];
        minus_button[0] = (TextView) findViewById(R.id.minus0);
        minus_button[1] = (TextView) findViewById(R.id.minus1);
        minus_button[2] = (TextView) findViewById(R.id.minus2);
        minus_button[3] = (TextView) findViewById(R.id.minus3);
        initPage();
        refresh_price_value();
    }

    void initPage() {

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        prices = new float[4];

        prices[0] = sharedPreferences.getFloat("breakfast", 0.0f);
        prices[1] = sharedPreferences.getFloat("lunch", 0.0f);
        prices[2] = sharedPreferences.getFloat("dinner", 0.0f);
        prices[3] = sharedPreferences.getFloat("special_item", 0.0f);
        user_display_name.setText(current_user.user_name);
        //TODO: start taking photo activity
        //TODO: set the profile picture fetched from the database
        button_start_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: wait till photo is taken

            }
        });
        //init item counters
        counter = new int[4];
        //TODO: extension::
        //TODO: based on the time of the day, increase one counter and leave others ZERO.
        for (int i = 0; i < 4; i++)
            counter[i] = 0;

        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            plus_button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p("changing the price");
                    counter[finalI]++;
                    refresh_price_value();
                }
            });
        }
        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            minus_button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (counter[finalI] > 0)
                        counter[finalI]--;
                    refresh_price_value();
                }
            });
        }

    }

    void refresh_price_value() {
        for (int i = 0; i < 4; i++) {
            p("Item:" + counter[i]);
            p("New price:" + Float.toString(prices[i] * counter[i]));
            price_textview[i].setText("Rs " + Float.toString(prices[i] * counter[i]));
        }
    }

    void p(String str) {
        System.out.println("" + str);
    }
}
