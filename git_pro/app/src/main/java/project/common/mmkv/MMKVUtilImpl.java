package project.common.mmkv;

import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;

import java.util.Set;

/**
 * description：mmkv实现封装
 * ===============================
 * creator：zhenqiang
 * create time：2020-01-13  11:23
 * ===============================
 * reasons for modification：
 * Modifier：
 * Modify time：2020-01-13  11:23
 */
public class MMKVUtilImpl implements MMKVUtill {

    private MMKV mmkv;
    private final boolean isCanDel;
    private static final String MMKV_NOT_DEL = "mmkv_not_del";

    public MMKVUtilImpl(boolean isCanDel) {
        this.isCanDel = isCanDel;
    }

    private synchronized MMKV getMMKV() {

        if (isCanDel) {
            mmkv = MMKV.defaultMMKV();
        } else if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(MMKV_NOT_DEL);
        }
        return mmkv;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getMMKV().decodeBool(key, defaultValue);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        getMMKV().encode(key, value);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getMMKV().decodeString(key, defaultValue);
    }

    @Override
    public void setString(String key, String value) {
        getMMKV().removeValueForKey(key);
        getMMKV().encode(key, value);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return getMMKV().decodeLong(key, defaultValue);
    }

    @Override
    public void setLong(String key, Long value) {
        getMMKV().encode(key, value);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return getMMKV().decodeInt(key, defaultValue);
    }

    @Override
    public void setInt(String key, int value) {
        getMMKV().encode(key, value);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return getMMKV().decodeFloat(key, defaultValue);
    }

    @Override
    public void setFloat(String key, float value) {
        getMMKV().encode(key, value);
    }

    @Override
    public void setString(String key, Set<String> set) {
        getMMKV().encode(key, set);
    }

    @Override
    public void setParcelable(String key, Parcelable parcelable) {
        getMMKV().encode(key, parcelable);
    }

    @Override
    public <T extends Parcelable> T getParcelable(String key, Class<T> tClass) {
        return decodeParcelable(key, tClass, null);
    }

    @Override
    public <T extends Parcelable> T decodeParcelable(String key, Class<T> tClass, T defaultValue) {
        return getMMKV().decodeParcelable(key, tClass, defaultValue);
    }

    @Override
    public boolean containKey(String key) {
        return getMMKV().contains(key);
    }

    @Override
    public void deleteAll() {
        getMMKV().clearAll();
    }

    @Override
    public double getSize() {
        return getMMKV().totalSize();
    }

    @Override
    public MMKVUtill remove(String key) {
         getMMKV().remove(key);
        return this;
    }

    @Override
    public MMKVUtill removeForKeys(String[] keys) {
        getMMKV().removeValuesForKeys(keys);
        return this;
    }

    @Override
    public MMKVUtill putString(String key, @Nullable String value) {
        getMMKV().encode(key, value);
        return this;
    }

    @Override
    public MMKVUtill putInt(String key, int value) {
        getMMKV().encode(key, value);
        return this;
    }

    @Override
    public MMKVUtill putLong(String key, long value) {
        getMMKV().encode(key, value);
        return this;
    }

    @Override
    public MMKVUtill putFloat(String key, float value) {
        getMMKV().encode(key, value);
        return this;
    }

    @Override
    public MMKVUtill putBoolean(String key, boolean value) {
        getMMKV().encode(key, value);
        return this;
    }

}
