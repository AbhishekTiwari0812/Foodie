package com.example.abhishek.foodie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Abhishek on 07-03-2016.
 */
public class ManagerPage extends AppCompatActivity {
    private Button login_button;
    private Button price_reset_button;
    private Button reset_password_button;
    LinearLayout ll_login;
    LinearLayout ll_home_page;
    LinearLayout ll_edit_password_page;
    EditText new_item_price;
    int layout_type;        // ==1 for login page, ==2 for set prices page,  ==3 for edit password page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.manager_page);
        layout_type = 1;
        //TODO: manage onBackPressed Button
        //TODO: set the prices in the edit price boxes
        ll_login = (LinearLayout) findViewById(R.id.manager_login_page);
        ll_home_page = (LinearLayout) findViewById(R.id.manger_page);
        ll_edit_password_page = (LinearLayout) findViewById(R.id.layout_edit_password);
        login_button = (Button) findViewById(R.id.manager_log_in);
        price_reset_button = (Button) findViewById(R.id.button_reset_password);
        reset_password_button = (Button) findViewById(R.id.button_reset_password);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:::::: check whether the password is correct or not
                boolean correct = true;
                //if password is correct:
                if (correct) {
                    layout_type = 2;
                    ll_login.setVisibility(View.GONE);
                    ll_home_page.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Password is incorrect.\nTry Again!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //resetting the price::
        price_reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_item_price = (EditText) findViewById(R.id.et_breakfast_price);
                Double new_breakfast_price = Double.valueOf(new_item_price.getText().toString());
                new_item_price = (EditText) findViewById(R.id.et_lunch_price);
                Double new_lunch_price = Double.valueOf(new_item_price.getText().toString());
                new_item_price = (EditText) findViewById(R.id.et_dinner_price);
                Double new_dinner_price = Double.valueOf(new_item_price.getText().toString());
                new_item_price = (EditText) findViewById(R.id.et_special_item_price);
                Double new_special_item_price = Double.valueOf(new_item_price.getText().toString());
                //TODO: change preferences values.

            }
        });
        reset_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_type = 3;
                ll_home_page.setVisibility(View.GONE);
                ll_edit_password_page.setVisibility(View.VISIBLE);
                //TODO: verify and reste password
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (layout_type) {
            case 1:
                super.onBackPressed();
                break;
            case 2:
                layout_type -= 1;
                ll_home_page.setVisibility(View.VISIBLE);
                ll_login.setVisibility(View.GONE);
                break;
            case 3:
                layout_type -= 1;
                ll_home_page.setVisibility(View.VISIBLE);
                ll_edit_password_page.setVisibility(View.GONE);
                break;
        }
    }
}
