package com.example.abhishek.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.abhishek.foodie.R.id.daily_user;
import static com.example.abhishek.foodie.R.id.one_time_user;

public class MainActivity extends AppCompatActivity {
    private Button button_daily_user;
    private Button button_guest;
    private Button button_one_time_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_daily_user = (Button) findViewById(R.id.daily_user);
        button_daily_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyUserList.class);
                startActivity(intent);
            }
        });
        button_guest = (Button) findViewById(R.id.guest_user);
        button_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open the guest user list
                Intent intent = new Intent(MainActivity.this, DailyUserList.class);
                startActivity(intent);
            }
        });
        button_one_time_user = (Button) findViewById(R.id.one_time_user);
        button_one_time_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open the one time user list
                Intent intent = new Intent(MainActivity.this, DailyUserList.class);
                startActivity(intent);
            }
        });
    }


}
