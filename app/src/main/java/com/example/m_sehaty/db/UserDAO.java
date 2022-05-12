package com.abdsoft.med_dose.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDAO {

    public static final String TAG = "UserDAO";

    private Context mContext;

    private SQLiteDatabase mDatabase;
    private com.abdsoft.med_dose.db.DBHelper mDbHelper;

    public UserDAO(Context context) {
        mDbHelper = new com.abdsoft.med_dose.db.DBHelper(context);
        this.mContext = context;

        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }

    }


    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public boolean checkEmailAndPassword(String email, String password){
        Cursor cursor = this.mDatabase.query("User", new String[]{com.abdsoft.med_dose.db.DBHelper.USER_KEY_EMAIL, com.abdsoft.med_dose.db.DBHelper.USER_PASSWORD}, com.abdsoft.med_dose.db.DBHelper.USER_KEY_EMAIL+"= ? AND " + com.abdsoft.med_dose.db.DBHelper.USER_PASSWORD+"= ?",new String[]{email,password},null,null,null);
        return cursor.getCount() > 0;
    }


    public Boolean checkEmail(String email) {
        Cursor cursor = this.mDatabase.rawQuery("Select * from " + com.abdsoft.med_dose.db.DBHelper.USER_TABLE + " where " + com.abdsoft.med_dose.db.DBHelper.USER_KEY_EMAIL+ " = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public Boolean registerNewUser(String first_input, String last_input, String email_input,String pass_input, String phone_input){

        ContentValues contentValues= new ContentValues();
        contentValues.put(com.abdsoft.med_dose.db.DBHelper.USER_KEY_FNAME, first_input);
        contentValues.put(com.abdsoft.med_dose.db.DBHelper.USER_KEY_LNAME, last_input);
        contentValues.put(com.abdsoft.med_dose.db.DBHelper.USER_KEY_EMAIL, email_input);
        contentValues.put(com.abdsoft.med_dose.db.DBHelper.USER_PASSWORD, pass_input);
        contentValues.put(com.abdsoft.med_dose.db.DBHelper.USER_KEY_Phone, phone_input);
        contentValues.put(com.abdsoft.med_dose.db.DBHelper.USER_ISCURRENT, "FALSE");

        long result = this.mDatabase.insert(com.abdsoft.med_dose.db.DBHelper.USER_TABLE, null, contentValues);
        return result != -1;
    }
}
