package com.example.abhishek.foodie;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhishek on 08-03-2016.
 */
enum FOOD_TYPE {
    BREAKFAST, LUNCH, DINNER, SPECIAL_ITEM;
}

public class FoodMenuItem {
    FOOD_TYPE type;       //1 for breakfast, 2 for lunch, 3 for dinner, 4 for breakfast
    int count;
    double price;

    FoodMenuItem(FOOD_TYPE type, int count) {
        this.type = type;
        this.count = count;
    }

    //TODO: get the price of food from the shared preferences.
    double getPrice() {
        double food_price = 0;
        return food_price;
    }

    //get food type
    FOOD_TYPE getType() {
        return this.type;
    }


    public void update_value(String ItemName, double NewPrice) {
        //change preferences

    }
}
