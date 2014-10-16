package com.example.remindit;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PnrDatabase {
	public static final String KEY_PNR = "ticket_pnr";
	public static final String KEY_DOJ = "ticket_doj";
	public static final String KEY_WAIT = "ticket_wait";
	public static final String KEY_CNF = "ticket_cnf";
	public static final String KEY_STATUS = "ticket_status";
	public static final String KEY_CHART = "ticket_chart_prepared";
	public static final String KEY_COUNTER = "ticket_counter";

	private static final String DATABASE_NAME = "pnr1.db";
	private static final String DATABASE_TABLE = "pnr_detail";
	private static final int DATABASE_VERSION = 2;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	/**********************************************************************
	 * INSERTING DATA TO THE DATABASE TABLE
	 */
	public void createEntry(String pnr, String doj, int wait, int confirm,
			String status, String chart, int counter) {
		try {
			Log.d("Insert: ", "Inserting ..");
			ContentValues cv = new ContentValues();
			cv.put(KEY_PNR, pnr);
			cv.put(KEY_DOJ, doj);
			cv.put(KEY_WAIT, wait);
			cv.put(KEY_CNF, confirm);
			cv.put(KEY_STATUS, status);
			cv.put(KEY_CHART, chart);
			cv.put(KEY_COUNTER, counter);

			Log.d("Insert: ", "Inserted");
			ourDatabase.insert(DATABASE_TABLE, null, cv);
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}

	}

	/*
	 * public String getData() { // TODO Auto-generated method stub
	 * Log.d("get data function: ", "called"); String[] columns = new String[] {
	 * KEY_ROWID, KEY_STATUS, KEY_PNR, KEY_DOJ }; Cursor c =
	 * ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
	 * 
	 * String result = ""; int iRow = c.getColumnIndex(KEY_ROWID); int iStatus =
	 * c.getColumnIndex(KEY_STATUS); int iPnr = c.getColumnIndex(KEY_PNR); int
	 * iDoj = c.getColumnIndex(KEY_DOJ); for (c.moveToFirst(); !c.isAfterLast();
	 * c.moveToNext()) { result = result + c.getString(iRow) + " " +
	 * c.getString(iStatus) + " PNR :" + c.getString(iPnr) + " " +
	 * c.getString(iDoj) + "\n";
	 * 
	 * } return result;
	 * 
	 * }
	 */

	/**********************************************************************
	 * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
	 */

	public ArrayList<ArrayList<Object>> getAllRowsAsArrays() {
		// create an ArrayList that will hold all of the data collected from the
		// database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor c;

		try {
			// ask the database object to create the cursor.
			String[] columns = new String[] { KEY_PNR, KEY_DOJ, KEY_WAIT,
					KEY_CNF, KEY_STATUS, KEY_CHART, KEY_COUNTER };
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
					dataList.add(c.getInt(2));
					dataList.add(c.getInt(3));
					dataList.add(c.getString(4));
					dataList.add(c.getString(5));
					dataList.add(c.getInt(6));
                    //Log.d(dataList.get(0).toString()+dataList.get(1).toString()+dataList.get(2).toString()+dataList.get(3).toString()+dataList.get(4).toString()+dataList.get(5).toString()+dataList.get(6).toString(),"data");    
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
	 * RETRIEVING ALL PNR FROM THE DATABASE TABLE
	 */

	public ArrayList<ArrayList<Object>> getAllPNR() {
		Log.d("getAllPNR():", "FETCHING PNRS...");
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor c;

		try {
			// ask the database object to create the cursor.
			String[] columns = new String[] { KEY_PNR };
			c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
					null, null);

			// move the cursor's pointer to position zero.
			c.moveToFirst();

			if (!c.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();
					Log.d("PNR:", c.getString(0));
					dataList.add(c.getString(0));
					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (c.moveToNext());

			}
			Log.d("getAllPNR():", "FETCHING DONE");
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		return dataArrays;

	}

	/**********************************************************************
	 * RETRIEVING DOJ FROM THE DATABASE TABLE
	 */

	public String getDoj(String pnr) {
		// TODO Auto-generated method stub
		Cursor cursor = ourDatabase.rawQuery("SELECT " + KEY_DOJ + " FROM "
				+ DATABASE_TABLE + " where " + KEY_PNR + "=" + pnr, null);
		cursor.moveToFirst();
		return cursor.getString(0);

	}

	/**********************************************************************
	 * RETRIEVING WAIT FROM THE DATABASE TABLE
	 */
	public int getWait(String pnr) {
		Cursor cursor = null;
		int count;

		try {
			cursor = ourDatabase.rawQuery("SELECT " + KEY_WAIT + " FROM "
					+ DATABASE_TABLE + " where " + KEY_PNR + "=" + pnr, null);
			cursor.moveToFirst();

		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		count = cursor.getInt(0);

		return count;
	}

	/**********************************************************************
	 * RETRIEVING CONFIRM FROM THE DATABASE TABLE
	 */
	public int getCnf(String pnr) {
		Cursor cursor = null;
		int count;

		try {
			cursor = ourDatabase.rawQuery("SELECT " + KEY_CNF + " FROM "
					+ DATABASE_TABLE + " where " + KEY_PNR + "=" + pnr, null);
			cursor.moveToFirst();

		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		count = cursor.getInt(0);

		return count;
	}

	/**********************************************************************
	 * RETRIEVING STATUS FROM THE DATABASE TABLE
	 */
	public String getStatus(String pnr) {
		Cursor cursor = null;
		try {
			cursor = ourDatabase.rawQuery("SELECT " + KEY_STATUS + " FROM "
					+ DATABASE_TABLE + " where " + KEY_PNR + "=" + pnr, null);
			cursor.moveToFirst();

		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		return cursor.getString(0);
	}

	/**********************************************************************
	 * RETRIEVING COUNTER FROM THE DATABASE TABLE
	 */
	public int getCounter(String pnr) {
		Cursor cursor = null;
		int count;

		try {
			cursor = ourDatabase.rawQuery("SELECT " + KEY_COUNTER + " FROM "
					+ DATABASE_TABLE + " where " + KEY_PNR + "=" + pnr, null);
			cursor.moveToFirst();

		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		count = cursor.getInt(0);

		return count;
	}

	/**********************************************************************
	 * RETRIEVING A ROW FROM THE DATABASE TABLE
	 */
	public ArrayList<Object> getRowAsArray(String rowID) {

		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;

		try {
			String[] columns = new String[] { KEY_PNR, KEY_DOJ, KEY_WAIT,
					KEY_CNF, KEY_STATUS, KEY_CHART, KEY_COUNTER };

			cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_PNR + "="
					+ rowID, null, null, null, null, null);

			// move the pointer to position zero in the cursor.
			cursor.moveToFirst();

			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (!cursor.isAfterLast()) {
				do {
					rowArray.add(cursor.getString(0));
					rowArray.add(cursor.getString(1));
					rowArray.add(cursor.getString(2));
					rowArray.add(cursor.getString(3));
					rowArray.add(cursor.getInt(4));

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
	public void updateRow(String lPnr, String mDoj, int mwait, int mcnf,
			String mStatus, String mChart, int mCounter) {
		Log.d("update:", "updating....");
		// this is a key value pair holder used by android's SQLite functions
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_DOJ, mDoj);
		cvUpdate.put(KEY_WAIT, mwait);
		cvUpdate.put(KEY_CNF, mcnf);
		cvUpdate.put(KEY_STATUS, mStatus);
		cvUpdate.put(KEY_CHART, mChart);
		cvUpdate.put(KEY_COUNTER, mCounter);

		// ask the database object to update the database row of given rowID
		try {
			ourDatabase.update(DATABASE_TABLE, cvUpdate, "ticket_pnr="+lPnr,
					null);
			Log.d("update:", "updated");
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	/**********************************************************************
	 * UPDATING A WAIT ROW IN THE DATABASE TABLE
	 */
	public void updateWait(String lPnr, int mWait) {
		Log.d("update:", "updating wait....");
		// this is a key value pair holder used by android's SQLite functions
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_WAIT, mWait);

		// ask the database object to update the database row of given rowID
		try {
			ourDatabase.update(DATABASE_TABLE, cvUpdate, "ticket_pnr="+lPnr,
					null);
			Log.d("update:", "updated");
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	/**********************************************************************
	 * UPDATING CONFIRM ROW IN THE DATABASE TABLE
	 */
	public void updateCnf(String lPnr, int mConfirm) {
		Log.d("update:", "updating confirm....");
		// this is a key value pair holder used by android's SQLite functions
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_CNF, mConfirm);

		// ask the database object to update the database row of given rowID
		try {
			ourDatabase.update(DATABASE_TABLE, cvUpdate, "ticket_pnr="+lPnr + lPnr,
					null);
			Log.d("update:", "updated");
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	/**********************************************************************
	 * UPDATING STATUS IN THE DATABASE TABLE
	 */
	public void updateStatus(String lPnr, String mStatus) {
		Log.d("update:", "updating status...."+mStatus+lPnr);
		// this is a key value pair holder used by android's SQLite functions
		

		// ask the database object to update the database row of given rowID
		try {
			ContentValues cvUpdate = new ContentValues();

			cvUpdate.put(KEY_STATUS, mStatus);
			ourDatabase.update(DATABASE_TABLE, cvUpdate,"ticket_pnr="+lPnr,null);
			Log.d("update:", "updated");
			String s=getStatus(lPnr);
			Log.d("status check",s);
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			
			e.printStackTrace();
		}
		
	}

	/**********************************************************************
	 * UPDATING COUNTER IN THE DATABASE TABLE
	 */
	public void updateCounter(String lPnr, int mCounter) {
		Log.d("update:", "updating counter...."+lPnr+mCounter);
		// this is a key value pair holder used by android's SQLite functions
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_COUNTER, mCounter);

		// ask the database object to update the database row of given rowID
		try {
			ourDatabase.update(DATABASE_TABLE, cvUpdate,"ticket_pnr="+lPnr,
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
	public void deleteEntry(String lPnr) {
		try {
			Log.d("delete:", "deleting....");
			ourDatabase.delete(DATABASE_TABLE, KEY_PNR + "=" + lPnr, null);
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
			db.execSQL("create table " + DATABASE_TABLE + " ( " + KEY_PNR
					+ " text primary key, " + KEY_DOJ + " text, "
					+ KEY_WAIT + " integer, " + KEY_CNF + " integer, "
					+ KEY_STATUS + " text, " + KEY_CHART + " text, "
					+ KEY_COUNTER + " integer" + " )");
			Log.d("sqlite database ", "Created");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists " + DATABASE_TABLE);
			onCreate(db);

		}

	}

	// constructor
	public PnrDatabase(Context c) {
		Log.d("PnrDatabase: ", "CTOR called");
		ourContext = c;
	}

	public PnrDatabase open() throws SQLException {

		ourHelper = new DbHelper(ourContext);
		Log.d("PnrDatabase:", "OPEN FUNCTION called");

		ourDatabase = ourHelper.getWritableDatabase();

	Log.d("PnrDatabase:", "database created");
		return this;
	}

	public void close() {
		ourHelper.close();
		//Log.d("PnrDatabase:", "CLOSE FUNCTION called");
	}

}
