package com.example.abhishek.foodie;

/**
 * Created by Abhishek on 15-02-2016.
 */
public class User {
    long user_id;       //unique id of the user.
    String user_name;   //name of the user
    Boolean is_guest;       //  is the current user a guest or not.
    int position_in_display_list;

    User(long id, String name, Boolean is_guest) {
        this.user_id = id;
        this.user_name = name;
        this.is_guest = is_guest;
    }

    //empty constructor
    User() {
        //do nothing.
    }
}
