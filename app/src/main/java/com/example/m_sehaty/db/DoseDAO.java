package com.abdsoft.med_dose.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

import com.abdsoft.med_dose.ui.dashboard.HistoryItem;
import com.abdsoft.med_dose.ui.home.HomeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DoseDAO {

    public static final String TAG = "DoseDAO";

    private Context mContext;

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;




    public DoseDAO(Context context) {
        mDbHelper = new DBHelper(context);
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

    public void insertNewMedicine(String medicineName, int day, int month, int year, int noOfTimesPerDay, int totalDoses, String timings, String alertType) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.MED_KEY_NAME, medicineName);
        values.put(DBHelper.MED_KEY_DAY, day);
        values.put(DBHelper.MED_KEY_MONTH, month);
        values.put(DBHelper.MED_KEY_YEAR, year);
        values.put(DBHelper.MED_KEY_TIMES_PER_DAY, noOfTimesPerDay);
        values.put(DBHelper.MED_KEY_TOTAL_DOSES, totalDoses);
        values.put(DBHelper.MED_KEY_TIMINGS, timings);
        values.put(DBHelper.MED_KEY_ALERT_TYPE, alertType);
        Log.i("Med-Dose DB Helper",  medicineName + day + month + year + noOfTimesPerDay + totalDoses + timings + alertType);
        this.mDatabase.insertWithOnConflict(DBHelper.MED_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.mDatabase.close();
    }



    public void deleteMedicine(String medicineName) {
        this.mDatabase.delete(DBHelper.MED_TABLE, DBHelper.MED_KEY_NAME + " = ?", new String[]{medicineName});
        this.mDatabase.close();
    }


    public List<HomeItem> getMedicineList() {
        List<HomeItem> medicineList = new ArrayList<>();
        Cursor cursor = this.mDatabase.query(DBHelper.MED_TABLE, new String[]{DBHelper.MED_KEY_NAME, DBHelper.MED_KEY_TIMES_PER_DAY}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            HomeItem homeItem = new HomeItem(cursor.getString(0)  , cursor.getString(1) + " times a day");
            medicineList.add(homeItem);
        }
        cursor.close();
        this.mDatabase.close();
        return medicineList;
       /* JSONObject json = new JSONObject(stringreadfromsqlite);
        ArrayList items = json.optJSONArray("uniqueArrays");*/
    }

    public int getId(String name) {
        int id = 0;
        Cursor cursor = this.mDatabase.query(DBHelper.MED_TABLE, new String[]{DBHelper.MED_KEY_NAME, DBHelper.MED_KEY_TIMES_PER_DAY}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        this.mDatabase.close();
        return id;
    }

    public List<HistoryItem> getMedicineHistory() {
        List<HistoryItem> historyList = new ArrayList<>();
        Cursor cursor = this.mDatabase.query(DBHelper.MED_TABLE, new String[]{DBHelper.MED_KEY_NAME, DBHelper.MED_KEY_DAY, DBHelper.MED_KEY_MONTH, DBHelper.MED_KEY_YEAR, DBHelper.MED_KEY_TIMES_PER_DAY, DBHelper.MED_KEY_TOTAL_DOSES, DBHelper.MED_KEY_TIMINGS}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(cursor.getInt(3), cursor.getInt(2), cursor.getInt(1));
            SimpleDateFormat format1 = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            String date = format1.format(calendar.getTime());
            HistoryItem historyItem= new HistoryItem(cursor.getString(0)  , date, cursor.getInt(4), cursor.getInt(5), cursor.getString(6));
            historyList.add(historyItem);
        }
        cursor.close();
        this.mDatabase.close();
        return historyList;
    }

    public List<String> getTimings(String medicineName) {
        List<String> timingList = new ArrayList<>();
        StringBuffer timingsString = new StringBuffer();
        Cursor cursor = this.mDatabase.query(DBHelper.MED_TABLE, new String[]{DBHelper.MED_KEY_TIMINGS}, DBHelper.MED_KEY_NAME + " = ?", new String[]{medicineName}, null, null, null);
        while (cursor.moveToNext()) {
            timingsString.append(cursor.getString(0));
            Log.i("Timings", timingsString.toString());
        }
        JSONObject json = null;
        try {
            json = new JSONObject(new String(timingsString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray items = json.optJSONArray("timingArrays");
        for (int i = 0; i < items.length(); i++) {
            try {
                timingList.add(items.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return timingList;
    }

    public int noOfDaysLeft(String medicineName, Calendar mNextAlarmDate) {
        int mPerDay = 0, mTotalDodes = 0;
        int day = 0, month = 0, year = 0;
        Cursor cursor = this.mDatabase.query(DBHelper.MED_TABLE, new String[]{DBHelper.MED_KEY_DAY, DBHelper.MED_KEY_MONTH, DBHelper.MED_KEY_YEAR, DBHelper.MED_KEY_TIMES_PER_DAY, DBHelper.MED_KEY_TOTAL_DOSES}, DBHelper.MED_KEY_NAME + " = ?", new String[]{medicineName}, null, null, null);
        while (cursor.moveToNext()) {
            day = cursor.getInt(0);
            month = cursor.getInt(1);
            year = cursor.getInt(2);
            mPerDay = cursor.getInt(3);
            mTotalDodes = cursor.getInt(4);
        }
        int totalDays = mTotalDodes/mPerDay;
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.DAY_OF_MONTH, day);
        startDate.set(Calendar.MONTH, month);
        startDate.set(Calendar.YEAR, year);
        long daysBetween = Math.round((float) (mNextAlarmDate.getTimeInMillis() - startDate.getTimeInMillis()) / (24 * 60 * 60 * 1000));
        int daysLeft = (int) (totalDays - daysBetween);
        return daysLeft;
    }



}
