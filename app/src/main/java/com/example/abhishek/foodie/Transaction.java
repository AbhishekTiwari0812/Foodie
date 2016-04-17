package com.example.abhishek.foodie;

import java.sql.Time;

/**
 * Created by Abhishek on 11-04-2016.
 */
public class Transaction {
    long transaction_id;        //unique transaction id, maybe timeStamp can be used?!
    long user_id;               //user who caused this transaction
    String user_img;            //image taken at the moment, for authentication records
    FOOD_TYPE food_type;              //type of food he/she took
    double price;                  //price of the food
    long time_label;             //time ?

    static Transaction createTransaction(User a, FoodMenuItem f) {
        Transaction t = new Transaction();
        t.transaction_id = System.currentTimeMillis();  //this must be a unique id
        t.user_id = a.user_id;
        t.food_type = f.getType();
        t.price = f.getPrice();
        return t;
    }

    void makeTransaction() {

    }
}
