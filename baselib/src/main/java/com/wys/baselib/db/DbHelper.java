package com.wys.baselib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yas on 2019/11/7
 * Describe:数据库Helper（用于文件的断点续下）
 */
public class DbHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "download.db";
    private static int VERSION = 1;
    public static String TABLE_VIDEO = "video_cache";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
