package com.example.abhishek.foodie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button button_daily_user;
    private Button button_guest;
    private Button button_one_time_user;
    private Button manager_login_button;
    static Context context;
    static ArrayList<User> list_of_all_users;
    static final String ACCESS_TOKEN_FILE_NAME = "MY_ENCRYPTER";
    static SharedPreferences sharedPreferences;
    //TODO: API authentication
    //TODO: sending images
    //TODO: Find the AccessToken for API encryption
    static String ClientAccessToken;
    static SharedPreferences clientAccessTokenFetcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        button_daily_user = (Button) findViewById(R.id.daily_user);
        button_guest = (Button) findViewById(R.id.guest_user);
        button_one_time_user = (Button) findViewById(R.id.one_time_user);
        manager_login_button = (Button) findViewById(R.id.manager);
        //TODO: Get the access token from the local machine, if not found, check web for the AccessToken
        //TODO: generate one unique id to tell the server.
        clientAccessTokenFetcher = getSharedPreferences(ACCESS_TOKEN_FILE_NAME, Context.MODE_PRIVATE);
        ClientAccessToken = clientAccessTokenFetcher.getString("MyAccessToken", null);

        if (ClientAccessToken == null) {
            //TODO: handle the exceptions
            // ClientAccessToken = DataFromWeb.getAccessTokenFromWeb();
            //  SharedPreferences.Editor editor = clientAccessTokenFetcher.edit();
            // editor.putString("MyAccessToken", ClientAccessToken);
            //    editor.commit();
        }
        if (ClientAccessToken == null) {
            //failed to get the access token. Abort the app right now.
            Toast.makeText(getApplicationContext(), "Failed to get the Access Token.\nRestart the app and make sure you're connected to the web", Toast.LENGTH_LONG).show();
            //TODO: uncomment this after testing.
            //finish();
        }

        initPage();
        sharedPreferences = getSharedPreferences(Transaction.FAILED_TRANSACTION_FILE, Context.MODE_PRIVATE);
        startService(new Intent(getApplicationContext(), BackgroundServices.class));
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
