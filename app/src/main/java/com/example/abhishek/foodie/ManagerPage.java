package com.example.abhishek.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    EditText et_breakfast_price;
    EditText et_lunch_price;
    EditText et_dinner_price;
    EditText et_special_item_price;
    EditText manager_password_field;
    EditText et_old_password;
    EditText et_new_password;
    EditText et_new_password_again;
    int layout_type;        // ==1 for login page, ==2 for set prices page,  ==3 for edit password page
    SharedPreferences sharedPreferences;
    String current_breakfast_price;
    String current_lunch_price;
    String current_dinner_price;
    String current_special_item_price;
    Button bt_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.manager_page);
        layout_type = 1;
        //TODO: set the prices in the edit price boxes
        ll_login = (LinearLayout) findViewById(R.id.manager_login_page);
        ll_home_page = (LinearLayout) findViewById(R.id.manger_page);
        ll_edit_password_page = (LinearLayout) findViewById(R.id.layout_edit_password);
        ll_login.setVisibility(View.VISIBLE);
        ll_home_page.setVisibility(View.GONE);
        ll_edit_password_page.setVisibility(View.GONE);
        login_button = (Button) findViewById(R.id.manager_log_in);
        price_reset_button = (Button) findViewById(R.id.button_reset_price);
        reset_password_button = (Button) findViewById(R.id.button_reset_password);
        bt_change_password = (Button) findViewById(R.id.button_change_password);
        manager_password_field = (EditText) findViewById(R.id.et_manager_password);
        et_breakfast_price = (EditText) findViewById(R.id.et_breakfast_price);
        et_lunch_price = (EditText) findViewById(R.id.et_lunch_price);
        et_dinner_price = (EditText) findViewById(R.id.et_dinner_price);
        et_special_item_price = (EditText) findViewById(R.id.et_special_item_price);
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_again = (EditText) findViewById(R.id.et_new_password_repeated);
        sharedPreferences = getSharedPreferences(UserProfile.PREF_FILE_NAME, Context.MODE_PRIVATE);
        current_breakfast_price = sharedPreferences.getString("breakfast", "0");
        current_lunch_price = sharedPreferences.getString("lunch", "0");
        current_dinner_price = sharedPreferences.getString("dinner", "0");
        current_special_item_price = sharedPreferences.getString("special_item", "0");
        et_breakfast_price.setText(current_breakfast_price);
        et_lunch_price.setText(current_lunch_price);
        et_dinner_price.setText(current_dinner_price);
        et_special_item_price.setText(current_special_item_price);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:::::: check whether the password is correct or not
                //String actual_password = DataFromWeb.get_manager_password();
                String actual_password = sharedPreferences.getString("manager_password", "1234");
                String entered_password = manager_password_field.getText().toString();
                boolean correct = entered_password.equals(actual_password);
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
                //TODO: make it failsafe by checking the format of the price entered by the manager
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("breakfast", et_breakfast_price.getText().toString());
                editor.putString("lunch", et_lunch_price.getText().toString());
                editor.putString("dinner", et_dinner_price.getText().toString());
                editor.putString("special_item", et_special_item_price.getText().toString());
                editor.commit();
                Toast.makeText(ManagerPage.this, "price of items havem been modified", Toast.LENGTH_LONG).show();
                _(sharedPreferences.getString("breakfast", "Not working correctly"));
            }
        });
        reset_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_type = 3;
                ll_home_page.setVisibility(View.GONE);
                ll_edit_password_page.setVisibility(View.VISIBLE);


            }
        });
        bt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: verify and reset password
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                String new_password_again = et_new_password_again.getText().toString();
                if (old_password.equals(sharedPreferences.getString("manager_password", "1234"))) {
                    if (new_password.equals(new_password_again)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("manager_password", new_password);
                        editor.commit();
                        Toast.makeText(getApplication().getApplicationContext(), "Password changed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication().getApplicationContext(), "New passwords didn't match.Try again ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Old Password is not correct.Try again", Toast.LENGTH_LONG).show();
                }
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

    void _(String str) {
        Log.d("Output....", str);
    }
}
