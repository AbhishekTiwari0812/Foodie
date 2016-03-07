package com.example.abhishek.foodie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Abhishek on 07-03-2016.
 */
public class ManagerPage extends AppCompatActivity {
    private Button login_button;
    LinearLayout ll_login;
    LinearLayout ll_home_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.manager_page);
        ll_login = (LinearLayout) findViewById(R.id.manager_login_page);
        ll_home_page = (LinearLayout) findViewById(R.id.manger_page);
        login_button = (Button) findViewById(R.id.manager_log_in);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:::::: check whether the password is correct or not
                boolean correct = true;
                //if password is correct:
                if (correct) {
                    ll_login.setVisibility(View.GONE);
                    ll_home_page.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Password is incorrect.\nTry Again!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
