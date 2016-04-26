package com.example.abhishek.foodie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Nitin on 17-04-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "app_details.db";

    //Table name
    public static final String TABLE_USERS = "users";

    //Table Fields
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GUEST = "guest";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS
                + "(" + ID + " INTEGER," + NAME
                + " TEXT," + GUEST + " BOOLEAN"+ ");";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        //Create new Tables
        onCreate(db);
    }


    //Add Users
    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ID,user.user_id);
        values.put(NAME,user.user_name);
        values.put(GUEST,user.is_guest);

        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }


    //Get All Users
    public ArrayList<User> getAllUsers()
    {
        ArrayList<User> m = new ArrayList<User>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        while (cursor.moveToNext())
        {
                int id=cursor.getInt(cursor.getColumnIndex(ID));
                String name=cursor.getString(cursor.getColumnIndex(NAME));
                Boolean guest=cursor.getInt(cursor.getColumnIndex(GUEST))!=0;

                User user=new User(id,name,guest);
                m.add(user);
        }
        db.close();

       return m;
    }


    public void updateUsers(ArrayList<User> m) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS);

        for(int i=0;i<m.size();i++)
        {
            ContentValues values = new ContentValues();
            values.put(ID, m.get(i).user_id);
            values.put(NAME,m.get(i).user_name);
            values.put(GUEST,m.get(i).is_guest);

            db.insert(TABLE_USERS,null,values);
        }
        db.close();
    }
}
