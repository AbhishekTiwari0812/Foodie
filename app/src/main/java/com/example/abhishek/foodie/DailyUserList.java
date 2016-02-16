package com.example.abhishek.foodie;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DailyUserList extends ListActivity {
    private ArrayList<UserListItem> listItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_user_page1);
        ListView expandableList = (ListView) findViewById(android.R.id.list);
        setListItems();
        CustomAdapter adapter = new CustomAdapter(this, listItems, getResources());
        expandableList.setAdapter(adapter);
        expandableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DailyUserList.this, DailyUserProfile.class);
                startActivity(intent);
            }
        });
    }

    public void setListItems() {
        //TODO: add items here
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
        listItems.add(new UserListItem());
    }

    void _(Object str) {
        Log.d("Error", "" + str);
    }
}