package project.common.mmkv;

import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Set;

/**
 * description：mmkv操作
 * ===============================
 * creator：zhenqiang
 * create time：2020-01-13  11:23
 * ===============================
 * reasons for modification：
 * Modifier：
 * Modify time：2020-01-13  11:23
 */

public interface MMKVUtill {
    /**
     * 获取mmkv中存储的Boolean值
     *
     * @param key          指定的key
     * @param defaultValue 默认值
     *
     * @return mmkv中存储的key对应的boolean
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 获取mmkv中存储的Boolean值，如果key已经存在，会覆盖旧的值
     *
     * @param key   指定的key
     * @param value 指定的值
     */
    void setBoolean(String key, boolean value);

    /**
     * 获取mmkv中存储的String值
     *
     * @param key          指定的key
     * @param defaultValue 默认值
     *
     * @return mmkv中存储的key对应的String
     */
    String getString(String key, String defaultValue);

    /**
     * 获取mmkv中存储的String值，如果key已经存在，会覆盖旧的值
     *
     * @param key   指定的key
     * @param value 指定的值
     */
    void setString(String key, String value);

    /**
     * 获取mmkv中存储的Long值
     *
     * @param key          指定的key
     * @param defaultValue 默认值
     *
     * @return mmkv中存储的key对应的Long
     */
    long getLong(String key, long defaultValue);

    /**
     * 获取mmkv中存储的Boolean值
     *
     * @param key   指定的key
     * @param value 指定的值
     */
    void setLong(String key, Long value);

    /**
     * 获取mmkv中存储的int值
     *
     * @param key          指定的key
     * @param defaultValue 默认值
     *
     * @return mmkv中存储的key对应的int
     */
    int getInt(String key, int defaultValue);

    /**
     * 获取mmkv中存储的int值
     *
     * @param key   指定的key
     * @param value 默认值
     *
     * @return mmkv中存储的key对应的int
     */
    void setInt(String key, int value);


    /**
     * 获取mmkv中存储的float值
     *
     * @param key
     * @param defaultValue
     *
     * @return
     */
    float getFloat(String key, float defaultValue);

    /**
     * 保存float
     *
     * @param key
     * @param value
     */
    void setFloat(String key, float value);

    /**
     * 保存Set<String>
     * @param key
     * @param set
     */
    void setString(String key, Set<String> set);

    /**
     * 保存实现Parcelable接口的序列化数据
     * @param key
     * @param parcelable
     */
    void setParcelable(String key, Parcelable parcelable);


    /**
     * 获取序列化数据
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
     <T extends Parcelable> T getParcelable(String key, Class<T> tClass);

    /**
     * 获取序列化数据，允许设置默认值
     * @param key
     * @param tClass
     * @param defaultValue
     * @param <T>
     * @return
     */
    <T extends Parcelable> T decodeParcelable(String key, Class<T> tClass, T defaultValue);
    
    /**
     * 判断mmkv中是否包含某个key
     *
     * @return true包含这个key
     */
    boolean containKey(String key);

    /**
     * 删除所有本地化的数据
     */
    void deleteAll();

    /**
     * 获取存储的大小
     *
     * @return 大小
     */
    double getSize();

    /**
     * 删除mmkv中的一个key对应的键值对
     *
     * @param key 指定的key
     */
    MMKVUtill remove(String key);


    /**
     * 删除mmkv中的keys数组对应的键值对
     *
     * @param keys 指定的keys数组
     */
    MMKVUtill removeForKeys(String[] keys);

    /**
     * 往edit里写入String
     *
     * @param key
     * @param value
     *
     * @return
     */
    MMKVUtill putString(String key, @Nullable String value);

    /**
     * 往edit里写入int
     *
     * @param key
     * @param value
     *
     * @return
     */
    MMKVUtill putInt(String key, int value);

    /**
     * 往edit里写入long
     *
     * @param key
     * @param value
     *
     * @return
     */
    MMKVUtill putLong(String key, long value);

    /**
     * 往edit里写入float
     *
     * @param key
     * @param value
     *
     * @return
     */
    MMKVUtill putFloat(String key, float value);

    /**
     * 往edit里写入boolean
     *
     * @param key
     * @param value
     *
     * @return
     */
    MMKVUtill putBoolean(String key, boolean value);

}
