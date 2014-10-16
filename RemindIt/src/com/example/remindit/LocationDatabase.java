package com.example.remindit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class LocationDatabase

{
	public static final String KEY_ID = "location_id";
	public static final String KEY_PLACE = "location_place";
	public static final String KEY_lat = "location_lat";
	public static final String KEY_lng = "location_lng";
	public static final String KEY_NOTE = "location_note";

	private static final String DATABASE_NAME = "location.db";
	private static final String DATABASE_TABLE = "location_detail";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	/**********************************************************************
	 * INSERTING DATA TO THE DATABASE TABLE
	 */
	public void createEntry(String place, String note, String lat, String lng) {
		try {
			Log.d("Insert: ", "Inserting ..");
			ContentValues cv = new ContentValues();
			cv.put(KEY_PLACE, place);
			cv.put(KEY_NOTE, note);
			cv.put(KEY_lat, lat);
			cv.put(KEY_lng, lng);

			Log.d("Insert: ", "Inserted");
			ourDatabase.insert(DATABASE_TABLE, null, cv);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}

	}

	/**********************************************************************
	 * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
	 */

	public ArrayList<ArrayList<Object>> getAllRowsAsArrays() {
		// create an ArrayList that will hold all of the data collected from the
		// database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		Cursor c;

		try {
			// ask the database object to create the cursor.
			String[] columns = new String[] { KEY_PLACE,KEY_lat,KEY_lng, KEY_NOTE,KEY_ID, };
			c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
					null, null);

			// move the cursor's pointer to position zero.
			c.moveToFirst();
           
			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!c.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(c.getString(0));
					dataList.add(c.getString(1));
					dataList.add(c.getString(2));
					dataList.add(c.getString(3));
					dataList.add(c.getString(4));
					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (c.moveToNext());
				Log.d("get all rows", " RETRIEVING ALL ROWS ");
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		
	
		
		return dataArrays;
		
	}

	/**********************************************************************
	 * RETRIEVING A ROW FROM THE DATABASE TABLE
	 */
	public ArrayList<Object> getRowAsArray(int rowID) {

		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;

		try {
			String[] columns = new String[] { KEY_PLACE, KEY_NOTE, };

			cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_ID + "="
					+ rowID, null, null, null, null, null);

			// move the pointer to position zero in the cursor.
			cursor.moveToFirst();

			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (!cursor.isAfterLast()) {
				do {
					rowArray.add(cursor.getString(0));
					rowArray.add(cursor.getString(1));

				} while (cursor.moveToNext());
			}

			// let java know that you are through with the cursor.
			cursor.close();
		} catch (SQLException e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList containing the given row from the database.
		return rowArray;
	}

	/**********************************************************************
	 * UPDATING A ROW IN THE DATABASE TABLE
	 */
	public void updateRow(int lId, String mPlace, String mNote) {
		Log.d("update:", "updating....");
		// this is a key value pair holder used by android's SQLite functions
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_PLACE, mPlace);
		cvUpdate.put(KEY_NOTE, mNote);

		// ask the database object to update the database row of given rowID
		try {
			ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ID + "=" + lId,
					null);
			Log.d("update:", "updated");
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	/**********************************************************************
	 * DELETE ROW FROM THE DATABASE TABLE
	 */
	public void deleteEntry(int lId) {
		try {
			Log.d("delete:", "deleting....");
			ourDatabase.delete(DATABASE_TABLE, KEY_ID + "=" + lId, null);
			Log.d("delete:", "deleted");
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
		public void deleteall() {
		try {
			Log.d("delete:", "deleting....");
			String deleteSQL = "DELETE FROM " + DATABASE_TABLE;

			ourDatabase.execSQL(deleteSQL);

			Log.d("delete:", "deleted");
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
	/**********************************************************************
	 * PRIVATE CLASS DBHELPER
	 */

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.d("oncreate: ", "called");
			try{db.execSQL("create table " + DATABASE_TABLE + " ( " + KEY_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_PLACE
					+ " text, " +KEY_lat +" text, " + KEY_lng + " text, "
					+ KEY_NOTE + " text " + " )");
			
			Log.d("sqlite database ", "Created");}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.d("Error in sql lite create ", "Error in sqllite create");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists " + DATABASE_TABLE);
			onCreate(db);

		}

	}

	// constructor
	public LocationDatabase(Context c) {

		Log.d("LocationDatabase: ", "CTOR called");
		ourContext = c;
	}

	public LocationDatabase open() throws SQLException {

		ourHelper = new DbHelper(ourContext);
		// Log.d("LocationDatabase:", "OPEN FUNCTION called");

		ourDatabase = ourHelper.getWritableDatabase();

		// Log.d("LocationDatabase:", "database created");
		return this;
	}

	public void close() {
		ourHelper.close();
		// Log.d("LocationDatabase:", "CLOSE FUNCTION called");
	}

}
