package com.sinoservices.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class DbHelper extends OrmLiteSqliteOpenHelper {

	private static final String TAG = "DbHelper";

	public DbHelper(Context context) {
		super(context, null, null, 1);
//		super(context, Constants.DATABASE_NAME, null,
//				Constants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			
//			TableUtils.createTableIfNotExists(connectionSource, User.class);
			
		} catch (Exception e) {
			e.printStackTrace();
//			Loger.getLogger(TAG).e(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		
	}
	
//	private Dao<User, Long> userDao;
//	public Dao<User, Long> getUserDao() throws SQLException {
//		if(userDao==null)
//			userDao = super.getDao(User.class); 
//		return userDao;
//	}


}
