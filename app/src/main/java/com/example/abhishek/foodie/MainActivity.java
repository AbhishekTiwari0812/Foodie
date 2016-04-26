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
        start_time = System.currentTimeMillis();
        final ProgressDialog progressDialogue = ProgressDialog.show(this, "Loading", "Trying to get the access token", true);
        progressDialogue.setCancelable(false);
        context = this;
        this.token_set = false;
        button_daily_user = (Button) findViewById(R.id.daily_user);
        button_guest = (Button) findViewById(R.id.guest_user);
        button_one_time_user = (Button) findViewById(R.id.one_time_user);
        manager_login_button = (Button) findViewById(R.id.manager);
        //TODO: Get the access token from the local machine, if not found, check web for the AccessToken
        clientAccessTokenFetcher = getSharedPreferences(ACCESS_TOKEN_FILE_NAME, Context.MODE_PRIVATE);
        ClientAccessToken = clientAccessTokenFetcher.getString("MyAccessToken", null);
        //TODO: edit this to support more than one client
        client_app_id = clientAccessTokenFetcher.getString("MyAppId", "abcd");
        client_app_password = clientAccessTokenFetcher.getString("MyAppPassword", "password");
        if (ClientAccessToken == null) {
            DataFromWeb.getAccessTokenFromWeb();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!MainActivity.token_set) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (ClientAccessToken == null) {
                        Toast.makeText(getApplicationContext(), "Failed to get the Access Token.\nRestart the app and make sure you're connected to the web", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        SharedPreferences.Editor editor = clientAccessTokenFetcher.edit();
                        editor.putString("MyAccessToken", ClientAccessToken);
                        editor.commit();
                    }
                    progressDialogue.cancel();
                }
            });
            t.start();

        } else {
            progressDialogue.cancel();
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
