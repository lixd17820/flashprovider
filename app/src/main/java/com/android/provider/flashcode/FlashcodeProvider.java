package com.android.provider.flashcode;

import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class FlashcodeProvider extends ContentProvider {

	private static final String TAG = "FixcodeProvider";
	// private static final int ROAD_ID = 3;
	// private static final int ROAD_XZQH = 4;
	// private static final int USER_XZQH = 5;
	// private static final int ADD_XZQH = 6;
	// private static final int ADD_ROAD = 7;
	// private static final int ADD_SEG = 8;
	private static final int ADD_WFDD = 9;
	private static final int DEL_WFDD = 13;
	// private static final int DEL_ALL = 15;
	// private static final int USER_ROAD = 16;
	// private static final int USER_SEG = 10;
	private static final int QUERY_FAVOR_WFDD = 11;
	private static final int DEL_DEPT = 101;
	private static final int DEL_VEH = 102;
	private static final int ADD_DEPT = 103;
	private static final int ADD_VEH = 104;
	private static final int DEPARTMENT = 105;
	private static final int VEHICLE = 106;
	private static final int QUERYVIOLATION = 202;
	private static final int UPDATEVIOLATION = 203;
	private static final int INSERTVIOLATION = 204;
	private static final int DELVIOLATION = 205;
	private static final int RAWDELSQL = 206;

	private static final int INSERT_GZXX = 300;
	private static final int QUERY_GZXX_MAXID = 301;
	private static final int QUERY_GZXX_INFO = 302;
	private static final int UPDATE_GZXX = 303;
	private static final int DELETE_GZXX = 304;

	private static final int INSERT_WPXX = 310;
	private static final int QUERY_WPXX_MAXID = 311;
	private static final int QUERY_WPXX_INFO = 312;
	private static final int UPDATE_WPXX = 313;

	private static final int INSERT_PCRYXX = 320;
	private static final int QUERY_PCRYXX_MAXID = 321;
	private static final int QUERY_PCRYXX_INFO = 322;
	private static final int UPDATE_PCRYXX = 323;

	private static final int INSERT_JBRYXX = 330;
	private static final int QUERY_JBRYXX_MAXID = 331;
	private static final int QUERY_JBRYXX_INFO = 332;
	private static final int UPDATE_JBRYXX = 333;

	private static final int RAWQUERY = 400;

	private static final int EXPORT_DB = 1000;

	public FlashcodeDatabaseHelper dbAdapter;

	public static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// uriMatcher.addURI(Flashcode.AUTHORITY, "road/#/#", ROAD_ID);
		// uriMatcher.addURI(Flashcode.AUTHORITY, "xzqhroads/#", ROAD_XZQH);
		// uriMatcher.addURI(Flashcode.AUTHORITY, "userxzqh", USER_XZQH);
		// uriMatcher.addURI(Flashcode.AUTHORITY, "userroad/#", USER_ROAD);
		// uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.USER_SEG, USER_SEG);
		// uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.ADD_XZQH, ADD_XZQH);
		// uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.ADD_ROAD, ADD_ROAD);
		// uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.ADD_SEG, ADD_SEG);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.ADD_WFDD, ADD_WFDD);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.DEL_WFDD, DEL_WFDD);
		// uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.DEL_ALL, DEL_ALL);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_FAVOR_WFDD,
				QUERY_FAVOR_WFDD);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.DEL_DEPT, DEL_DEPT);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.DEL_VEH, DEL_VEH);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.ADD_DEPT, ADD_DEPT);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.ADD_VEH, ADD_VEH);
		uriMatcher
				.addURI(Flashcode.AUTHORITY, Flashcode.DEPARTMENT, DEPARTMENT);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.VEHICLE, VEHICLE);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERYVIOLATION,
				QUERYVIOLATION);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.UPDATEVIOLATION,
				UPDATEVIOLATION);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.INSERTVIOLATION,
				INSERTVIOLATION);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.DELVIOLATION,
				DELVIOLATION);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.RAWDELSQL, RAWDELSQL);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.INSERT_GZXX,
				INSERT_GZXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_GZXX_MAXID,
				QUERY_GZXX_MAXID);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_GZXX_INFO,
				QUERY_GZXX_INFO);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.UPDATE_GZXX,
				UPDATE_GZXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.DELETE_GZXX,
				DELETE_GZXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.INSERT_WPXX,
				INSERT_WPXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_WPXX_MAXID,
				QUERY_WPXX_MAXID);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_WPXX_INFO,
				QUERY_WPXX_INFO);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.UPDATE_WPXX,
				UPDATE_WPXX);

		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.INSERT_PCRYXX,
				INSERT_PCRYXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_PCRYXX_MAXID,
				QUERY_PCRYXX_MAXID);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_PCRYXX_INFO,
				QUERY_PCRYXX_INFO);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.UPDATE_PCRYXX,
				UPDATE_PCRYXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.RAWQUERY, RAWQUERY);

		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.INSERT_JBRYXX,
				INSERT_JBRYXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_JBRYXX_MAXID,
				QUERY_JBRYXX_MAXID);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.QUERY_JBRYXX_INFO,
				QUERY_JBRYXX_INFO);
		uriMatcher.addURI(Flashcode.AUTHORITY, Flashcode.UPDATE_JBRYXX,
				UPDATE_JBRYXX);
		uriMatcher.addURI(Flashcode.AUTHORITY, "export_db", EXPORT_DB);
	}

	// 创建数据库
	@Override
	public boolean onCreate() {
		int currentCode = 0;
		try {
			PackageManager pm = getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(
					"com.android.provider.flashcode", 0);
			currentCode = pi.versionCode;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

		Log.e(TAG, "on create method");
		dbAdapter = FlashcodeDatabaseHelper.getDBAdapterInstance(getContext(),
				currentCode);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		dbAdapter.openDataBase();
		// Log.e(TAG,"on fixcode provider create data");
		// databasehelper =
		// FixcodeDatabaseHelper.getDBAdapterInstance(getContext());
		// try {
		// databasehelper.createDataBase();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowId = 0;
		switch (uriMatcher.match(uri)) {
		// 删除所有用户路段对照表
		// case DEL_ALL:
		// rowId += dbAdapter.deleteRecordInDB(Flashcode.UserXzqh.TABLE_NAME,
		// selection, selectionArgs);
		// rowId += dbAdapter.deleteRecordInDB(Flashcode.UserRoads.TABLE_NAME,
		// selection, selectionArgs);
		// rowId += dbAdapter.deleteRecordInDB(
		// Flashcode.UserRoadSeg.TABLE_NAME, selection, selectionArgs);
		// break;
		// 删除自定义违法地点表的内容
		case DEL_WFDD:
			rowId = dbAdapter.deleteRecordInDB(Flashcode.FavorWfdd.TABLE_NAME,
					selection, selectionArgs);
			break;
		case DEL_DEPT:
			rowId = dbAdapter.deleteRecordInDB(Flashcode.Department.TABLE_NAME,
					selection, selectionArgs);
			break;
		case DEL_VEH:
			rowId = dbAdapter.deleteRecordInDB(Flashcode.Vehicle.TABLE_NAME,
					selection, selectionArgs);
			break;
		// case Flashcode.DEL_HMB:
		// rowId = dbAdapter.deleteRecordInDB(Flashcode.Hmb.TABLE_NAME,
		// selection, selectionArgs);
		// break;
		case DELVIOLATION:
			rowId = dbAdapter
					.deleteRecordInDB(Flashcode.VioViolation.TABLE_NAME,
							selection, selectionArgs);
			break;
		case RAWDELSQL:
			dbAdapter.executeRawSql(selection);
			break;
		case DELETE_GZXX:
			rowId = dbAdapter.deleteRecordInDB(Flashcode.ZapcGzxx.TABLE_NAME,
					selection, selectionArgs);
			break;
		default:
			break;
		}
		return rowId;
	}

	@Override
	public String getType(Uri uri) {
		int code = uriMatcher.match(uri);
		switch (code) {
		// case ADD_XZQH:
		// case ADD_ROAD:
		// case ADD_SEG:
		case ADD_WFDD:
			return Flashcode.CONTENT_TYPE;
			// case USER_ROAD:
			// case USER_SEG:
		case QUERYVIOLATION:
			return Flashcode.CONTENT_ITEM_TYPE;
			// case ROAD_ID:
			// return Flashcode.CONTENT_ITEM_TYPE;
			// case ROAD_XZQH:
			// case USER_XZQH:
			// return Flashcode.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// SQLiteDatabase db = databasehelper.getWritableDatabase();
		long rowId = 0;
		switch (uriMatcher.match(uri)) {
		// case ADD_XZQH:
		// rowId = dbAdapter.insertRecordsInDB(Flashcode.UserXzqh.TABLE_NAME,
		// values);
		// break;
		// case ADD_ROAD:
		// rowId = dbAdapter.insertRecordsInDB(Flashcode.UserRoads.TABLE_NAME,
		// values);
		// break;
		// case ADD_SEG:
		// rowId = dbAdapter.insertRecordsInDB(
		// Flashcode.UserRoadSeg.TABLE_NAME, values);
		// break;
		// 加入一条自定义违法地点表
		case ADD_WFDD:
			rowId = dbAdapter.insertRecordsInDB(Flashcode.FavorWfdd.TABLE_NAME,
					values);
			break;
		// 加入单位信息
		case ADD_DEPT:
			rowId = dbAdapter.insertRecordsInDB(
					Flashcode.Department.TABLE_NAME, values);
			break;
		// 加入车辆信息
		case ADD_VEH:
			rowId = dbAdapter.insertRecordsInDB(Flashcode.Vehicle.TABLE_NAME,
					values);
			break;
		// 加入一条简易程序
		case INSERTVIOLATION:
			rowId = dbAdapter.insertRecordsInDB(
					Flashcode.VioViolation.TABLE_NAME, values);
			break;
		// 加入治安盘查工作信息
		case INSERT_GZXX:
			dbAdapter.copyFlashDbToSdCard();
			rowId = dbAdapter.insertRecordsInDB(Flashcode.ZapcGzxx.TABLE_NAME,
					values);
			break;
		case INSERT_WPXX:
			rowId = dbAdapter.insertRecordsInDB(Flashcode.ZapcWpxx.TABLE_NAME,
					values);
			break;
		case INSERT_PCRYXX:
			rowId = dbAdapter.insertRecordsInDB(
					Flashcode.ZapcPcryxx.TABLE_NAME, values);
			break;
		case INSERT_JBRYXX:
			rowId = dbAdapter.insertRecordsInDB(
					Flashcode.ZapcJbryxx.TABLE_NAME, values);
			break;

		default:
			break;
		}

		// rowId = db.insert(Fixcode.FrmCode.CODE_TABLE_NAME,
		// Fixcode.FrmCode._ID,
		// values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(Flashcode.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Cursor c = null;

		switch (uriMatcher.match(uri)) {
		// case USER_XZQH:
		// c = dbAdapter
		// .selectRecordsFromDB(Flashcode.UserXzqh.TABLE_NAME,
		// projection, selection, selectionArgs, null, null,
		// sortOrder);
		// break;
		// // 根据行政区划查询用户区域内的道路列表
		// case USER_ROAD:
		// String userXzqh = uri.getPathSegments().get(1);
		// c = dbAdapter.selectRecordsFromDB(Flashcode.UserRoads.TABLE_NAME,
		// projection, Flashcode.UserRoads.XZQH + "='" + userXzqh
		// + "'", selectionArgs, null, null, sortOrder);
		// break;
		// // 从用户路段表中查询数据，条件由selection指定
		// case USER_SEG:
		// c = dbAdapter
		// .selectRecordsFromDB(Flashcode.UserRoadSeg.TABLE_NAME,
		// projection, selection, selectionArgs, null, null,
		// sortOrder);
		// break;
		case QUERY_FAVOR_WFDD:
			c = dbAdapter
					.selectRecordsFromDB(Flashcode.FavorWfdd.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case DEPARTMENT:
			c = dbAdapter
					.selectRecordsFromDB(Flashcode.Department.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case VEHICLE:
			c = dbAdapter
					.selectRecordsFromDB(Flashcode.Vehicle.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		// 查询简易程序
		case QUERYVIOLATION:
			c = dbAdapter.selectRecordsFromDB(
					Flashcode.VioViolation.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		// 查询工作信息最大编号
		case QUERY_GZXX_MAXID:
			c = dbAdapter
					.selectRecordsFromDB(
							"select case WHEN (max(id) ISNULL) THEN 1 else max(id)+1 END id from zapc_gzxx ",
							null);
			break;
		// 查询工作信息
		case QUERY_GZXX_INFO:
			c = dbAdapter
					.selectRecordsFromDB(Flashcode.ZapcGzxx.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		// 查询物品信息最大编号
		case QUERY_WPXX_MAXID:
			c = dbAdapter
					.selectRecordsFromDB(
							"select case WHEN (max(id) ISNULL) THEN 1 else max(id)+1 END id from zapc_wpxx ",
							null);
			break;
		// 查询物品信息
		case QUERY_WPXX_INFO:
			c = dbAdapter
					.selectRecordsFromDB(Flashcode.ZapcWpxx.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		// 查询盘查人员信息最大编号
		case QUERY_PCRYXX_MAXID:
			c = dbAdapter
					.selectRecordsFromDB(
							"select case WHEN (max(pcrybh) ISNULL) THEN 1 else max(pcrybh)+1 END id from zapc_pcryxx ",
							null);
			break;
		// 查询盘查人员信息
		case QUERY_PCRYXX_INFO:
			c = dbAdapter
					.selectRecordsFromDB(Flashcode.ZapcPcryxx.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		// 查询人员基本信息最大编号
		case QUERY_JBRYXX_MAXID:
			c = dbAdapter
					.selectRecordsFromDB(
							"select case WHEN (max(id) ISNULL) THEN 1 else max(id)+1 END id from zapc_jbryxx ",
							null);
			break;
		// 查询人员基本信息
		case QUERY_JBRYXX_INFO:
			c = dbAdapter
					.selectRecordsFromDB(Flashcode.ZapcJbryxx.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case RAWQUERY:
			c = dbAdapter.selectRecordsFromDB(selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int row = 0;
		switch (uriMatcher.match(uri)) {
		// 更新简易程序
		case UPDATEVIOLATION:
			row = dbAdapter.updateRecordsInDB(
					Flashcode.VioViolation.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case UPDATE_GZXX:
			row = dbAdapter.updateRecordsInDB(Flashcode.ZapcGzxx.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		case UPDATE_WPXX:
			row = dbAdapter.updateRecordsInDB(Flashcode.ZapcWpxx.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		case UPDATE_PCRYXX:
			row = dbAdapter.updateRecordsInDB(Flashcode.ZapcPcryxx.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		case UPDATE_JBRYXX:
			row = dbAdapter.updateRecordsInDB(Flashcode.ZapcJbryxx.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		case EXPORT_DB:
			Log.e("Flashcode db",""+EXPORT_DB);
			//row = dbAdapter.copyFlashDbToSdCard();
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return row;
	}

}
