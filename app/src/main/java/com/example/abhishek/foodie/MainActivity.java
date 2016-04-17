package com.example.abhishek.foodie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button button_daily_user;
    private Button button_guest;
    private Button button_one_time_user;
    private Button manager_login_button;
    static Context context;
    static ArrayList<User> list_of_all_users;

    //TODO; check internet connection
    //TODO: get the list of users from the web if connected
    //TODO: if not, fetch from temp DB, show toast to the User and keep trying to refresh
    //TODO: send transaction if connected to web otherwise store it to send later.
    //TODO: API authentication
    //TODO: sending images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        button_daily_user = (Button) findViewById(R.id.daily_user);
        button_guest = (Button) findViewById(R.id.guest_user);
        button_one_time_user = (Button) findViewById(R.id.one_time_user);
        manager_login_button = (Button) findViewById(R.id.manager);
        initPage();
        list_of_all_users = DataFromWeb.GetUsersList();
    }

    void initPage() {
        button_daily_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserList.class);
                intent.putExtra("is_guest", false);
                startActivity(intent);
            }
        });
        button_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open the guest user list
                Intent intent = new Intent(MainActivity.this, UserList.class);
                intent.putExtra("is_guest", true);
                startActivity(intent);
            }
        });
        button_one_time_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open the one time user list
                Intent intent = new Intent(MainActivity.this, UserList.class);
                startActivity(intent);
            }
        });

        manager_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open login page
                Intent intent = new Intent(MainActivity.this, ManagerPage.class);
                startActivity(intent);

            }
        });


    }


}
