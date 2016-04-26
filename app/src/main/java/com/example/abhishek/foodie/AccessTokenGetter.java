package com.example.abhishek.foodie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Abhishek on 26-04-2016.
 */
public class AccessTokenGetter extends AppCompatActivity {
    private EditText app_id_fetcher;
    private EditText app_password_fetcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_token_getter);
        app_id_fetcher = (EditText) findViewById(R.id.et_app_id);
        app_password_fetcher = (EditText) findViewById(R.id.et_app_password);
        Button verifier_button = (Button) findViewById(R.id.bt_access_token_fetcher);
        verifier_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.client_app_id = app_id_fetcher.toString();

                MainActivity.client_app_password = app_password_fetcher.toString();
                finish();
            }
        });
    }
}
