package com.abdsoft.med_dose.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TAG = "DBHelper";
	private static final String DATABASE_NAME = "Med-Dose.db";
	private static final int DATABASE_VERSION = 1;

	// columns of the Medecines table
	public static final String MED_TABLE = "Medecines";
	public static final String MED_KEY_ID = "_id";
	public static final String MED_KEY_NAME = "Name";
	public static final String MED_KEY_DAY = "Day";
	public static final String MED_KEY_MONTH = "Month";
	public static final String MED_KEY_YEAR = "Year";
	public static final String MED_KEY_TIMES_PER_DAY = "TimesPerDay";
	public static final String MED_KEY_TOTAL_DOSES = "TotalDoses";
	public static final String MED_KEY_TIMINGS = "Timings";
	public static final String MED_KEY_ALERT_TYPE = "AlertType";

	// columns of the User table
	public static final String USER_TABLE = "User";
	public static final String USER_KEY_ID = "_id";
	public static final String USER_KEY_FNAME = "First_Name";
	public static final String USER_KEY_LNAME = "Last_Name";
	public static final String USER_KEY_EMAIL = "Email";
	public static final String USER_KEY_Phone = "Phone";
	public static final String USER_PASSWORD = "Password";
	public static final String USER_ISCURRENT = "IsCurrent";

	// SQL statement of the employees table creation
	private static final String SQL_CREATE_TABLE_MEDECINES = "CREATE TABLE " + MED_TABLE + "("
			+ MED_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ MED_KEY_NAME + " TEXT NOT NULL, "
			+ MED_KEY_DAY + " INTEGER NOT NULL, "
			+ MED_KEY_MONTH + " INTEGER NOT NULL, "
			+ MED_KEY_YEAR + " INTEGER NOT NULL, "
			+ MED_KEY_TIMES_PER_DAY + " INTEGER NOT NULL, "
			+ MED_KEY_TOTAL_DOSES + " INTEGER NOT NULL, "
			+ MED_KEY_TIMINGS + " TEXT NOT NULL, "
			+ MED_KEY_ALERT_TYPE + " TEXT NOT NULL "
			+");";



	// SQL statement of the employees table creation
	private static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE + "("
			+ USER_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ USER_KEY_FNAME + " TEXT NOT NULL, "
			+ USER_KEY_LNAME + " TEXT NOT NULL, "
			+ USER_KEY_EMAIL + " TEXT NOT NULL, "
			+ USER_KEY_Phone + " TEXT NOT NULL, "
			+ USER_PASSWORD  + " TEXT NOT NULL, "
			+ USER_ISCURRENT + " TEXT NOT NULL  "
			+");";


	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SQL_CREATE_TABLE_MEDECINES);
		database.execSQL(SQL_CREATE_TABLE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading the database from version " + oldVersion + " to "+ newVersion);
		// clear all data
		db.execSQL("DROP TABLE IF EXISTS " + MED_TABLE);
		
		// recreate the tables
		onCreate(db);
	}

	public DBHelper(Context context,  CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
}
