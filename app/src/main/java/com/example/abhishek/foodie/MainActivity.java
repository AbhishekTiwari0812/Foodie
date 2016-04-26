package com.example.abhishek.foodie;

import android.app.ProgressDialog;
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
    public static boolean token_set;
    private Button button_daily_user;
    private Button button_guest;
    static String client_app_id;
    static String client_app_password;
    private Button button_one_time_user;
    private Button manager_login_button;
    static Context context;
    static ArrayList<User> list_of_all_users;
    static final String ACCESS_TOKEN_FILE_NAME = "MY_ENCRYPTER";
    static SharedPreferences sharedPreferences;
    //TODO: sending images
    //TODO: Find the AccessToken for API encryption
    static String ClientAccessToken;
    static SharedPreferences clientAccessTokenFetcher;
    long start_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clientAccessTokenFetcher = getSharedPreferences(ACCESS_TOKEN_FILE_NAME, Context.MODE_PRIVATE);
        //get client app id
        client_app_id = clientAccessTokenFetcher.getString("MyAppId", null);
        //get client app password
        client_app_password = clientAccessTokenFetcher.getString("MyAppPassword", null);
        if (client_app_id == null) {
            //if client app id is null, fetch it from the user.
            Intent intent = new Intent(MainActivity.this, AccessTokenGetter.class);
            startActivity(intent);
        }

        //get the access from local machine
        //if the user opened the app for the first time, it will be null
        ClientAccessToken = clientAccessTokenFetcher.getString("MyAccessToken", null);

        //get the access token from the web
        final ProgressDialog progressDialogue = ProgressDialog.show(this, "Loading", "Trying to get the access token", true);
        progressDialogue.setCancelable(false);
        context = this;
        this.token_set = false;

        if (ClientAccessToken == null) {
            //trying to get the data from the server
            DataFromWeb.getAccessTokenFromWeb();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //waiting to get any response from the server.
                    while (!MainActivity.token_set) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //if the access token is not found.
                    if (ClientAccessToken == null) {
                       // Toast.makeText(getApplicationContext(), "Failed to get the Access Token.\nWrong credentials or no internet.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        //if we found the access token
                        SharedPreferences.Editor editor = clientAccessTokenFetcher.edit();
                        //store it in the shared-preferences for future transactions
                        editor.putString("MyAccessToken", ClientAccessToken);
                        editor.putString("MyAppId", client_app_id);
                        editor.putString("MyAppPassword", client_app_password);
                        editor.commit();
                    }
                    progressDialogue.cancel();
                }
            });
            t.start();
        } else {
            //if the token was already in the shared-preferences.
            progressDialogue.cancel();
        }

        button_daily_user = (Button) findViewById(R.id.daily_user);
        button_guest = (Button) findViewById(R.id.guest_user);
        button_one_time_user = (Button) findViewById(R.id.one_time_user);
        manager_login_button = (Button) findViewById(R.id.manager);
        //initialize the on click listeners for the home page.
        initPage();

        //this is for keeping track of failed transactions and fetching the failed transaction to the server
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
