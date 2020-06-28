package project.common.DbUtils;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class DaoHelper<T> {
    private static DaoHelper sDaoHelper;
    private final static String DEFAULT_PATH = "/data/data/com.test.commonporject/database/test.db";
    private SQLiteDatabase sqLiteDatabase;
    private String tabName;//数据库表名
    private Class<T> cls;
    private BaseDaoImpl dao;

    private DaoHelper() {
        File file = new File(DEFAULT_PATH);
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            if (!mkdirs) {
                //create error
                return;
            }
        }
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DEFAULT_PATH,null);

    }

    public static <T> DaoHelper<T> getInstance() {
        if (sDaoHelper == null) {
            synchronized (DaoHelper.class) {
                if (sDaoHelper == null) {
                    sDaoHelper = new DaoHelper<>();
                }
            }
        }
        return sDaoHelper;
    }

    public <T>BaseDaoImpl<T> getDao(Class<T> cls) {
        try {
//            if (dao == null) {
//                dao = BaseDaoImpl.class.newInstance();
//            }
//            DbFile annotation = cls.getAnnotation(DbFile.class);
//            if (annotation == null) {
//                throw new IllegalArgumentException("getDao has null DbFile");
//            }
//            String tmp = annotation.value();
//            if (!TextUtils.isEmpty(tmp) && TextUtils.equals(tmp, tabName) && sqLiteDatabase != null) {
//                //是同一个数据库
//                dao.init(sqLiteDatabase, cls);
//            } else {
//                //不是同一个数据库
//                if (sqLiteDatabase!=null) {
//                    sqLiteDatabase.close();
//                }
//                tabName = tmp;
//                this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DEFAULT_PATH + tabName + ".db", null);
//                dao.init(sqLiteDatabase, cls,true);
//            }

            dao = BaseDaoImpl.class.newInstance();
            dao.init(sqLiteDatabase,cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dao;
    }
}
