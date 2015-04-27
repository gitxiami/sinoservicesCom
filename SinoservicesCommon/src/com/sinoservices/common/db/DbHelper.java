package com.sinoservices.common.db;

import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sinoservices.common.Global;
import com.sinoservices.common.entity.ModuleEntity;
import com.sinoservices.common.util.LogUtil;
/**
 * @ClassName: DbHelper 
 * @Description: ���ݿ������
 * @date 2015��4��27�� ����8:43:14 
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

	private static final String TAG = "DbHelper";

	public DbHelper(Context context) {
		super(context, Global.DATABASE_NAME, null, Global.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			// �½�Ӧ��ģ�����ݱ�
			TableUtils.createTableIfNotExists(connectionSource,
					ModuleEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.d(TAG, e.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			// ɾ����
			TableUtils.dropTable(connectionSource, ModuleEntity.class, true);

			/** ���½��� **/
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.d(TAG, e.toString());
		}
	}
    
	private Dao<ModuleEntity, Long> moduleEntityDao;
    /**��ȡmoduleEntityDao**/
	public Dao<ModuleEntity, Long> getModuleEntityDao() throws SQLException {
		if (moduleEntityDao == null)
			moduleEntityDao = super.getDao(ModuleEntity.class);
		return moduleEntityDao;
	}
		
}
