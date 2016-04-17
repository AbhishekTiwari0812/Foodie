package com.example.abhishek.foodie;

import java.util.ArrayList;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UserList extends ListActivity {
    private ArrayList<User> listItems = new ArrayList<User>();
    private boolean is_guest = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);
        is_guest = getIntent().getBooleanExtra("is_guest", false);
        ListView expandableList = (ListView) findViewById(android.R.id.list);
        try {
            setListItems();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CustomAdapter adapter = new CustomAdapter(this, listItems, getResources());
        expandableList.setAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
            }
        });
    }

    public void setListItems() throws InterruptedException {
        //TODO: add items here
        if (!DataFromWeb.is_user_list_ready) {
            Thread.sleep(1000l);
            //busy waiting for the data to be fetched from the internet.
        }
        ArrayList<User> usersList = MainActivity.list_of_all_users;
        //TODO: first sort the name in lexicographical order.
        //TODO: Assign the index of each user in the list.
        for (int i = 0; i < usersList.size(); i++) {
            User user = usersList.get(i);
            p("User:" + user.user_name);
            if (is_guest == user.is_guest) {     //if only guests are requested, add only the guests
                user.position_in_display_list = i;
                listItems.add(user);

                p("Adding :" + user.user_name);
            }
        }
    }

    void p(Object str) {
        Log.d("Error", "" + str);
    }
}