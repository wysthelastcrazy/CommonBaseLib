package com.wys.downloader.db;

/**
 * Created by yas on 2020-03-02
 * Describe:数据库常量管理
 */
public class DBConstant {
    //数据库名称
    public static final String DB_NAME = "download.db";
    //数据库版本
    public static final int DB_VERSION = 1;
    //数据库表名称
    public static final String TABLE_NAME = "download_file_info";

    /**数据库表字段**/
    public static final String TASK_ID = "task_id";
    public static final String DOWNLOAD_URL = "download_url";
    public static final String DOWNLOAD_DIR = "download_dir";
    public static final String DOWNLOAD_NAME = "download_name";
    public static final String FILE_LENGTH = "file_length";
    public static final String CURR_POSITION = "curr_position";


}
