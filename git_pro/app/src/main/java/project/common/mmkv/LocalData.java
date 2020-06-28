package project.common.mmkv;

import com.tencent.mmkv.MMKV;

/**
 * mmkv 帮助
 */
public class LocalData {

    //String rootDir = MMKV.initialize(this);
    //System.out.println("mmkv root: " + rootDir);

    private MMKV mmkv;

    private LocalData() {
        mmkv = MMKV.defaultMMKV();
    }

    public static LocalData getInstance() {

        return LocalDataInner.instance;
    }

    private static class LocalDataInner {
        private static LocalData instance = new LocalData();
    }


}
