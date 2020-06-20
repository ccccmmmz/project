package com.example.common.DbUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库升级应该依赖一个外部数据 {@link SQLiteDatabase getversion()}
 */
public class BaseDaoImpl<T> implements IBaseDao<T> {

    private SQLiteDatabase sqLiteDatabase;//操作的数据库
    private Class<T> cls;//操作的数据
    private String tabName;//表名
    private String beanFlag;//记录bean中的主键 可以没有设置
    private boolean isInitFinish = false;
    private Map<String, Field> cacheMap;//缓存的注解map

    void init(SQLiteDatabase sqLiteDatabase, Class<T> cls) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.cls = cls;
        if (!isInitFinish) {
            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen() || cls == null) {
                throw new IllegalArgumentException("db or cls Illegal");
            }
            initCacheMap();
            isInitFinish = true;
        }
    }

//    public void init(SQLiteDatabase sqLiteDatabase, Class<T> cls,boolean initNew){
//        this.sqLiteDatabase = sqLiteDatabase;
//        this.cls = cls;
//        Log.i("ligen", "init: BaseDaoImpl---" + isInitFinish);
//        if (initNew) {
//            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen() || cls == null) {
//                throw new IllegalArgumentException("db or cls Illegal");
//            }
//            initCacheMap();
//            isInitFinish = true;
//        }
//    }

    /**
     * 缓存反射数据 减少反射
     */
    private void initCacheMap() {
        //创建数据库
        DbFile annotation = cls.getAnnotation(DbFile.class);
        if (annotation == null) {
            throw new IllegalArgumentException("the DbFile value is null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        tabName = annotation.value();
        sb.append(tabName);
        sb.append(" (");

        cacheMap = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            DbField annotationField = field.getAnnotation(DbField.class);
            if (annotationField != null) {
                String key = annotationField.value();
                if (TextUtils.isEmpty(key)) {
                    throw new IllegalArgumentException(annotationField.toString() + "has null DbField");
                }
                //查找主键
                if (TextUtils.isEmpty(beanFlag)) {
                    DbPrimary primary = field.getAnnotation(DbPrimary.class);
                    if (primary != null) {
                        beanFlag = key;
                    }
                }
                cacheMap.put(key, field);
                sb.append(key);//sql key
                field.setAccessible(true);
                try {
                    Class<?> type = field.getType();
                    String typeStr = type.toString();
                    if (typeStr.equals("int")) {
                        sb.append(" integer");
                    } else if (typeStr.equals(String.class.toString())) {
                        sb.append(" text");
                    } else if (typeStr.equals(Double.class.toString())) {
                        sb.append(" double");
                    } else if (typeStr.equals(Float.class.toString())) {
                        sb.append(" float");
                    } else if (typeStr.equals(Float.class.toString())) {
                        sb.append(" long");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //添加主键
                if (!TextUtils.isEmpty(beanFlag) && TextUtils.equals(key, beanFlag)) {
                    sb.append(" PRIMARY KEY AUTOINCREMENT,");
                } else {
                    sb.append(",");
                }
            }
        }
        if (',' == sb.charAt(sb.length() - 1)) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        sqLiteDatabase.execSQL(sb.toString());
        Log.i("ligen", "initCacheMap: execSQL" + sb.toString());
    }

    @Override
    public void release() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    @Override
    public void insert(Object obj) {
        if (sqLiteDatabase==null) {
            throw new IllegalArgumentException("数据库不存在,检查DaoHelper 的DEFAULT_PATH");
        }
        ContentValues contentValues = getContentValues(obj);
        sqLiteDatabase.beginTransaction();
        try {
            long insert = sqLiteDatabase.insert(tabName, beanFlag, contentValues);
            if (insert != -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }
            Log.i("ligen", "insert: " + contentValues.toString());
            Log.i("ligen", "insert: 插入回调" + insert);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }

    }

    @NonNull
    private ContentValues getContentValues(Object obj) {
        ContentValues contentValues = new ContentValues();
        if (obj==null) {
            return contentValues;
        }
        Set<String> keySet = cacheMap.keySet();
        for (String next : keySet) {
//            if (TextUtils.equals(next, beanFlag) && !TextUtils.isEmpty(beanFlag)) {
//                是主键不要加入cv中
//                continue;
//            }
            Field field = cacheMap.get(next);
            if (field == null) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                if (o!=null) {
                    contentValues.put(next, o.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }

    @Override
    public void delete(Object obj) {
        if (sqLiteDatabase==null) {
            throw new IllegalArgumentException("数据库不存在,检查DaoHelper 的DEFAULT_PATH");
        }
        if (TextUtils.isEmpty(beanFlag)) {
            //没有设置主键 匹配所有
            ContentValues contentValues = getContentValues(obj);
            if (contentValues.size()==0) {
                return;
            }
            try {
                Condition condition = new Condition(contentValues);
                sqLiteDatabase.beginTransaction();
                int delete;
                delete = sqLiteDatabase.delete(tabName, condition.getSelection(), condition.getSelectionArgs());
                if (delete != 0) {
                    sqLiteDatabase.setTransactionSuccessful();
                }
                Log.i("ligen", "delete: 删除回调" + delete);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqLiteDatabase.endTransaction();
            }
        } else {
            Field field = cacheMap.get(beanFlag);
            //主键的值
            if (field == null) {
                return;
            }
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                sqLiteDatabase.beginTransaction();
                int delete;
                delete = sqLiteDatabase.delete(tabName, beanFlag + " = ?", new String[]{o.toString()});
                if (delete != 0) {
                    sqLiteDatabase.setTransactionSuccessful();
                }
                Log.i("ligen", "delete: 删除回调" + delete);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqLiteDatabase.endTransaction();
            }
        }
    }

    @Override
    public void update(T obj) {
        if (sqLiteDatabase==null) {
            throw new IllegalArgumentException("数据库不存在,检查DaoHelper 的DEFAULT_PATH");
        }

        if (TextUtils.isEmpty(beanFlag)) {
            //s没有设置主键 匹配所有
            ContentValues contentValues = getContentValues(obj);
            if (contentValues.size()==0) {
                return;
            }
            try {
                Condition condition = new Condition(contentValues);
                sqLiteDatabase.beginTransaction();
                int delete;
                delete = sqLiteDatabase.update(tabName,contentValues, condition.getSelection(), condition.getSelectionArgs());
                if (delete != 0) {
                    sqLiteDatabase.setTransactionSuccessful();
                }
                Log.i("ligen", "update: 更新回调" + delete);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqLiteDatabase.endTransaction();
            }
        } else {
            Field field = cacheMap.get(beanFlag);
            //主键的值
            if (field == null) {
                return;
            }
            ContentValues contentValues = getContentValues(obj);//更新的cv
            if (contentValues.size()==0) {
                return;
            }
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                sqLiteDatabase.beginTransaction();
                int update;
                update = sqLiteDatabase.update(tabName, contentValues,beanFlag + " = ?",new String[]{o.toString()});
                if (update != 0) {
                    sqLiteDatabase.setTransactionSuccessful();
                }
                Log.i("ligen", "update: 更新回调" + update);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqLiteDatabase.endTransaction();
            }
        }
    }

    @Override
    public List<T> query(T obj) {
        if (sqLiteDatabase==null) {
            throw new IllegalArgumentException("数据库不存在,检查DaoHelper 的DEFAULT_PATH");
        }
        //查询
        return query(obj,"",null,null);
    }

    private List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        String limitStr = null;
        if (startIndex != null && limit != null) {
            limitStr = startIndex + "," + limit;
        }
        Cursor cursor = null;
        Condition condition = new Condition(getContentValues(where));
        List<T> result = new ArrayList<>();
        try {
            T t1 = null;
            cursor = sqLiteDatabase.query(tabName, null, condition.getSelection(),
                    condition.getSelectionArgs(), "", "", orderBy, limitStr);
            while (cursor.moveToNext()) {
                Set<String> keySet = cacheMap.keySet();//列名
                try {
                    t1 = cls.newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
                for (String s : keySet) {
                    //为了赋值给T
                    Field field = cacheMap.get(s);
                    if (field == null) {
                        continue;
                    }
                    //拿到set方法
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (t1==null) {
                        return result;
                    }
                    try {
                        if (type == String.class) {
                            String string = cursor.getString(cursor.getColumnIndex(s));//列中对应的值
                            string = string == null ? "" : string;
                            field.set(t1, string);
                        } else if (type == Integer.class||type==int.class) {
                            int string = cursor.getInt(cursor.getColumnIndex(s));
                            field.set(t1, string);
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                //添加到集合中
                result.add(t1);
            }
        } catch (IllegalAccessException| IllegalArgumentException | SecurityException |IllegalStateException |NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 查询所有
     */
    public List<T> query(){
        return query(null);
    }

    public List<T> query(Integer offSet,Integer limit){
        return query(null,"",offSet,limit);
    }
    public List<T> query(@IntRange(from = 1) int limit){
        return query(null,"",0,limit);
    }
}
