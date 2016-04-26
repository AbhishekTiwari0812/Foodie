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


    /*	This is the constructor initialization for the DatabaseHandler Class which is used for handling database queries.
    *	@param Context context
    */

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*	returns void
    *	Creates a table in the database
    *	@param db SQLiteDatabase Method is used to create Tables.
    *	@return void
    */

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS
                + "(" + ID + " INTEGER," + NAME
                + " TEXT," + GUEST + " BOOLEAN"+ ");";

        db.execSQL(CREATE_USERS_TABLE);
    }

    /*	returns void
    *	This method is invoked when the database upgrade is detected.
    *	@param SQLiteDatabase db Database instance
    *	@param int oldVersion The previous version of the database
    *	@param int newVersion The New version of the database
    *	@return void
    */


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        //Create new Tables
        onCreate(db);
    }



    /*	returns void
    *	This method is used to add user to the database
    *	@param User user The User Object
    *	@return void
    */

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


    /*	returns ArrayList of Users
    *	This method is returns all the Users which are stored in the local database
    *	@return ArrayList<User>
    */


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

    /*	returns void
    *	This method Updates the Users List in the database
    *	@param ArrayList<User> m The List contains the User Objects which needs to be updated to the database
    *	@return void
    */
    public void updateUsers(ArrayList<User> m) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_USERS);

            for (int i = 0; i < m.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(ID, m.get(i).user_id);
                values.put(NAME, m.get(i).user_name);
                values.put(GUEST, m.get(i).is_guest);

                db.insert(TABLE_USERS, null, values);
            }
            db.close();

        } catch (Exception e) {

        }
    }
}
