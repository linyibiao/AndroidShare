package com.lyb.besttimer.commonutil.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/4/25
 * 描    述：
 * =====================================
 */
public class FileUtil {

    public static File getDir(Context context, String dir) {
        File parentFile;
        if (isExternalStorageWritable()) {
            parentFile = new File(Environment.getExternalStorageDirectory(), dir);
        } else {
            parentFile = new File(context.getFilesDir(), dir);
        }
        return parentFile;
    }

    public static String saveBitmap(Context context, String dir, Bitmap b) {
        File parentFile = getDir(context, dir);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        long dataTake = System.currentTimeMillis();
        File jpegFile = new File(parentFile, "picture_" + dataTake + ".jpg");
        try {
            FileOutputStream fout = new FileOutputStream(jpegFile);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
