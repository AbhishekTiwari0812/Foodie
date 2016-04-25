package com.example.abhishek.foodie;

import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by Abhishek on 11-04-2016.
 */
public class Transaction {
    static final String FAILED_TRANSACTION_FILE = "failed_transactions";
    static long UniqueId = 0;
    long transaction_id;        //unique transaction id, maybe timeStamp can be used?!
    long user_id;               //user who caused this transaction
    boolean is_guest;
    char food_type;              //type of food he/she took
    float price;                  //price of the food
    String user_img;            //image taken at the moment, for authentication records
    public Date time_stamp;
    SharedPreferences sharedPreferences;
    void setFoodType(int i) {
        switch (i) {
            case 0:
                this.food_type = 'B';
                break;
            case 1:
                this.food_type = 'L';
                break;
            case 2:
                this.food_type = 'D';
                break;
            case 3:
                this.food_type = 'S';
                break;
        }
    }

    //add failed transaction to the buffer.
    void addToFailedTransactionList(String s, String id) {
        //checking if transaction already exists otherwise add to the transaction  buffer.
        String check_existence = MainActivity.sharedPreferences.getString(id, null);
        sharedPreferences = BackgroundServices.sharedPreferences;
        if (check_existence == null) {
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString(id, s);
            e.commit();
        }
    }
}
