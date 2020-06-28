package project.common.mmkv;

/**
 * description：获取MMKV实例，
 * instant：设置-清理缓存 会清除
 * instantNotDel: 设置-清理缓存 不会清除
 * ===============================
 * creator：zhenqiang
 * create time：2020-01-13  11:16
 * ===============================
 * reasons for modification：
 * Modifier：
 * Modify time：2020-01-13  11:16
 */
public class MMKVGetter {

    private static final MMKVUtill instant = new MMKVUtilImpl(true);
    private static final MMKVUtill instantNotDel = new MMKVUtilImpl(false);

    /**
     * 设置-清理缓存 会被清除
     * @return
     */
    public static MMKVUtill MMKV_IMPL() {
        return instant;
    }

    /**
     * 设置-清理缓存 不会被清除
     * @return
     */
    public static MMKVUtill MMKV_IMPL_NOT_DEL() {
        return instantNotDel;
    }

}
