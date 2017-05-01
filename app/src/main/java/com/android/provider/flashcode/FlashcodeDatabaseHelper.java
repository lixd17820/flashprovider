package com.android.provider.flashcode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class FlashcodeDatabaseHelper extends SQLiteOpenHelper {

	private static String TAG = "FlashcodeDatabaseHelper";

	private static String DB_PATH = "";
	private SQLiteDatabase myDataBase;

	private final Context myContext;
	private int version;

	private static FlashcodeDatabaseHelper mDBConnection;

	/**
	 * 构造方法,由上下文和版本号两个参数
	 * 
	 * @param context
	 * @param version
	 */
	private FlashcodeDatabaseHelper(Context context, int version) {
		super(context, Flashcode.DATABASE_NAME, null, version);
		this.myContext = context;
		this.version = version;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
		File f = new File(DB_PATH);
		if (!f.exists())
			f.mkdirs();
	}

	/**
	 * 单例方法
	 * 
	 * @param context
	 * @return DBAdapter
	 */
	public static synchronized FlashcodeDatabaseHelper getDBAdapterInstance(
			Context context, int version) {
		if (mDBConnection == null) {
			mDBConnection = new FlashcodeDatabaseHelper(context, version);
		}
		return mDBConnection;
	}

	/**
	 * 验证数据库是否存在或版本号,拷贝文件
	 * 
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			// do nothing - database already exist
		} else {
			try {
				copyDataBase();
				Log.e(TAG, "version is updated");
				String myPath = DB_PATH + Flashcode.DATABASE_NAME;
				SQLiteDatabase db = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READWRITE);
				Log.e(TAG, "version " + version);
				if (db != null && db.getVersion() < version) {
					db.setVersion(version);
					Log.e(TAG, "db.setVersion(" + version + ")");
					db.close();
				}
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * 验证文件是否存在,并且版本和目前版本的关系,如果不存在或版本低于目前版本则返回真
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		boolean result = false;
		int oldversion = 0;
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + Flashcode.DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		if (checkDB != null) {
			oldversion = checkDB.getVersion();
			Log.e(TAG, "old database's version is " + oldversion);
			checkDB.close();
			if (oldversion >= version) {
				result = true;
			}
		}
		return result;
	}

	public int copyFlashDbToSdCard() {
		int count = 0;
		String myPath = DB_PATH + Flashcode.DATABASE_NAME;
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(new File(myPath)));
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(new File(
							Environment.getExternalStorageDirectory(),
							"falshcode.db")));
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) > 0) {
				out.write(b, 0, len);
				count += len;
			}
			out.close();
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			count = 0;
		}
		return count;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(
				Flashcode.DATABASE_NAME);
		File path = new File(DB_PATH);
		if (!path.exists())
			path.mkdirs();
		File outFileName = new File(path, Flashcode.DATABASE_NAME);
		if (outFileName.exists()) {
			Log.e(TAG, "Delete file.");
			outFileName.delete();
		}
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Open the database
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + Flashcode.DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * Close the database if exist
	 */
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	/**
	 * Call on creating data base for example for creating tables at run time
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	/**
	 * can used for drop tables then call onCreate(db) function to create tables
	 * again - upgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("onUpgrade", "db old " + oldVersion + " newVersion " + newVersion);

	}

	// ----------------------- CRUD Functions ------------------------------

	/**
	 * This function used to select the records from DB.
	 * 
	 * @param tableName
	 * @param tableColumns
	 * @param whereClase
	 * @param whereArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return A Cursor object, which is positioned before the first entry.
	 */
	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {
		return myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
				groupBy, having, orderBy);
	}

	/**
	 * This function used to update the Record in DB.
	 * 
	 * @param tableName
	 * @param initialValues
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are updated
	 */
	public int updateRecordsInDB(String tableName, ContentValues initialValues,
			String whereClause, String whereArgs[]) {
		return myDataBase.update(tableName, initialValues, whereClause,
				whereArgs);
	}

	/**
	 * 加入或更新记录
	 * 
	 * @param tableName
	 * @param initialValues
	 * @return
	 */
	public long replaceRecordsInDB(String tableName, ContentValues initialValues) {
		return myDataBase.replace(tableName, null, initialValues);
	}

	/**
	 * This function used to delete the Record in DB.
	 * 
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are deleted.
	 */
	public int deleteRecordInDB(String tableName, String whereClause,
			String[] whereArgs) {
		return myDataBase.delete(tableName, whereClause, whereArgs);
	}

	// --------------------- Select Raw Query Functions ---------------------

	/**
	 * apply raw Query
	 * 
	 * @param query
	 * @param selectionArgs
	 * @return Cursor
	 */
	public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
		return myDataBase.rawQuery(query, selectionArgs);
	}

	/**
	 * 执行指定的SQL语句,用于操作数据,非查询
	 * 
	 * @param sql
	 */
	public void executeRawSql(String sql) {
		myDataBase.execSQL(sql);
	}

	// -------------------------------------------------------------------------

	/**
	 * 加入数据
	 * 
	 * 
	 */
	public long insertRecordsInDB(String tableName, ContentValues values) {
		return myDataBase.insert(tableName, null, values);
	}

}

// 以上是FixcodeDatabaseHelper类,用于创建和更新数据库
