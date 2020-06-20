package com.example.common.image;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 图片辅助
 */
public class ImageHelper {

    /**
     * 获得一个打开相机的Intent intent.getdata为bitmap
     * @return null if this device not support
     */
    public static Intent getOpenCamera(Context context){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (checkIntentCanAccess(intent,context)) {
            return intent;
        }
        return null;
    }

    /**
     *
     * @param outFile 打开相机 图片存储位置
     * @return null if this device not support
     */
    public static Intent getOpenCamera(Context context, Uri outFile){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outFile);
        if (checkIntentCanAccess(intent,context)) {
            return intent;
        }
        return null;
    }

    /**
     *
     * @return 获取相册图片的路径
     */
    public static String getAlbumImagePath(Intent intent,Context context){
        if (intent==null) {
            return "";
        }
        Uri uri = intent.getData();
        Cursor cursor = null;
        String result;
        try {
            String[] fileColumn = {MediaStore.Images.Media.DATA};
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), uri, fileColumn);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(fileColumn[0]);
            result = cursor.getString(columnIndex);

        }finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return result;
    }
    /**
     *
     * @return the intent can be resolve
     */
    private static boolean checkIntentCanAccess(Intent intent, Context context){
        return intent.resolveActivity(context.getPackageManager())!=null;
    }
}
