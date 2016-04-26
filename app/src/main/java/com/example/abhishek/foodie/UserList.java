package com.example.abhishek.foodie;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class UserList extends ListActivity {
    private ArrayList<User> listItems = new ArrayList<User>();
    private boolean is_guest = false;
    SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);
        is_guest = getIntent().getBooleanExtra("is_guest", false);
        final ListView expandableList = (ListView) findViewById(android.R.id.list);
        try {
            setListItems();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Adding Search feature
        search=(SearchView) findViewById(R.id.searchName);
        search.setQueryHint("Type Here!");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try{
                    setListItems(newText);
                    expandableList.setAdapter(new CustomAdapter(UserList.this, listItems, getResources()));
                    expandableList.deferNotifyDataSetChanged();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return false;
            }
        });


        CustomAdapter adapter = new CustomAdapter(this, listItems, getResources());
        expandableList.setAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Clearing the Search Box

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

    //Methods required for searching

    public void setListItems(String token) throws InterruptedException {
        //TODO: add items here
        if (!DataFromWeb.is_user_list_ready) {
            Thread.sleep(1000l);
            //busy waiting for the data to be fetched from the internet.
        }
        ArrayList<User> usersList = MainActivity.list_of_all_users;
        //TODO: first sort the name in lexicographical order.
        //TODO: Assign the index of each user in the list.
        listItems.clear();
        for (int i = 0; i < usersList.size(); i++) {
            User user = usersList.get(i);
            p("User:" + user.user_name);
            if (is_guest == user.is_guest  && checkUserInitialName(user,token)) {     //if only guests are requested, add only the guests
                user.position_in_display_list = i;
                listItems.add(user);

                p("Adding :" + user.user_name);
            }
        }
    }

    Boolean checkUserInitialName(User u,String text)
    {
        return u.user_name.toLowerCase().startsWith(text.toLowerCase());
    }

    @Override
    protected void onResume() {
        super.onResume();
        search.setQuery("",false); //clear the text
    }
}