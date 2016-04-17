package com.example.abhishek.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    TextView breakfast_price, lunch_price, dinner_price, special_item_price;
    TextView user_display_name;
    static final String PREF_FILE_NAME = "my_price_file";
    User current_user;
    FoodMenuItem foodTaken;
    Button button_start_transaction;
    int breakfast_counter, lunch_counter, dinner_counter, special_item_counter;
    double current_breakfast_price;
    double current_lunch_price;
    double current_dinner_price;
    double current_special_item_price;
    TextView[] plus_button;         //0 for breakfast, 1 for lunch, 2 dinner ,3 for special item.
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
        breakfast_price = (TextView) findViewById(R.id.tv_breakfast_price);
        lunch_price = (TextView) findViewById(R.id.tv_lunch_price);
        dinner_price = (TextView) findViewById(R.id.tv_dinner_price);
        special_item_price = (TextView) findViewById(R.id.tv_special_item_price);
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
        counter = new int[4];
        for (int i = 0; i < 4; i++)
            counter[i] = 0;
        initPage();
        refresh_price_value();

    }

    void initPage() {
        breakfast_counter = lunch_counter = dinner_counter = special_item_counter = 0;
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        current_breakfast_price = Double.parseDouble(sharedPreferences.getString("breakfast", "0"));
        current_lunch_price = Double.parseDouble(sharedPreferences.getString("lunch", "0"));
        current_dinner_price = Double.parseDouble(sharedPreferences.getString("dinner", "0"));
        current_special_item_price = Double.parseDouble(sharedPreferences.getString("special_item", "0"));
        user_display_name.setText(current_user.user_name);
        //TODO: start taking photo activity
        //TODO: set the profile name and profile picture fetched from the database
        //TODO: remove the buttons and add a counter.
        button_start_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: wait till photo is taken
                Transaction.createTransaction(current_user, getFoodItem());


            }
        });
        //init item counters
        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            plus_button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public FoodMenuItem getFoodItem() {
        if (breakfast_counter != 0) {
            foodTaken = new FoodMenuItem(FOOD_TYPE.BREAKFAST, breakfast_counter);
        }
        if (lunch_counter != 0) {

        }
        if (dinner_counter != 0) {

        }
        if (special_item_counter != 0) {

        }
        return foodTaken;
    }

    void refresh_price_value() {
        breakfast_price.setText("Rs " + (current_breakfast_price * breakfast_counter));
        lunch_price.setText("Rs " + (current_lunch_price * lunch_counter));
        dinner_price.setText("Rs " + (current_dinner_price * dinner_counter));
        special_item_price.setText("Rs " + (special_item_counter * current_special_item_price));
    }
}
