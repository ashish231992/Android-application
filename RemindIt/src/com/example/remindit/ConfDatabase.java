package com.example.remindit;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
public class ConfDatabase {
	public static final String KEY_PNR = "ticket_pnr";
	
	public static final String KEY_CHART = "ticket_chart_prepared";
	public static final String KEY_FLAG = "ticket_flag";

	private static final String DATABASE_NAME = "confdb.db";
	private static final String DATABASE_TABLE = "conf_detail";
	private static final int DATABASE_VERSION = 2;

	private DbHelper ourHelper;
	private  Context ourContext;
	private SQLiteDatabase ourDatabase;

	/**********************************************************************
	 * INSERTING DATA TO THE DATABASE TABLE
	 */
	public void createEntry(String pnr, String chart, String flag) {
		try {
			Log.d("Insert: ", "Inserting ..");
			ContentValues cv = new ContentValues();
			cv.put(KEY_PNR, pnr);
			
			cv.put(KEY_CHART, chart);
			cv.put(KEY_FLAG, flag);

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

		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor c;

		try {
			// ask the database object to create the cursor.
			String[] columns = new String[] { KEY_PNR, KEY_CHART, KEY_FLAG };
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

	
	

	
	


	/**********************************************************************
	 * UPDATING A ROW IN THE DATABASE TABLE
	 */
	public void updateRow(String lPnr,String flag) {
		Log.d("update:", "updating....");
		// this is a key value pair holder used by android's SQLite functions
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_FLAG, flag);
		;

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
	
	public void updatechart(String lPnr,String chart) {
		Log.d("update:", "updating....");
		// this is a key value pair holder used by android's SQLite functions
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_CHART, chart);
		;

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


	
	public String getflag(String pnr) {
		Cursor cursor = null;
		try {
			cursor = ourDatabase.rawQuery("SELECT " + KEY_FLAG+ " FROM "
					+ DATABASE_TABLE + " where " + KEY_PNR + "=" + pnr, null);
			cursor.moveToFirst();

		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		return cursor.getString(0);
	}
	public String getchart(String pnr) {
		Cursor cursor = null;
		try {
			cursor = ourDatabase.rawQuery("SELECT " + KEY_CHART+ " FROM "
					+ DATABASE_TABLE + " where " + KEY_PNR + "=" + pnr, null);
			cursor.moveToFirst();

		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		return cursor.getString(0);
	}

	
	
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
					+ " text primary key, " 
					+KEY_CHART + " text, "
					+ KEY_FLAG + " integer" + " )");
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
	public ConfDatabase(Context c) {
		Log.d("PnrDatabase: ", "CTOR called");
		ourContext = c;
	}

	public ConfDatabase open() throws SQLException {

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
