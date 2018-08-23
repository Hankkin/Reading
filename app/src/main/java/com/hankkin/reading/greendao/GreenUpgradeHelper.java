package com.hankkin.reading.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CY on 2016/10/24.
 * Email: tmxdyf@163.com
 */

public class GreenUpgradeHelper {


    /**
     * 升级入口
     *
     * @param db db
     * @param openHelper 重新建表时需要用上
     */
    public void upgrade(SQLiteDatabase db, SQLiteOpenHelper openHelper) {
        List<String> tables = queryAllTables(db);

        for (String tableName : tables) {
            if ("android_metadata".equals(tableName)||"sqlite_sequence".equals(tableName)) {//表的元数据，过滤
                continue;
            }
            String tempTableName = tableName + "_Temp";
            createTempTables(db, tableName, tempTableName);//创建临时表
            dropTable(db, tableName);//删除原表
            openHelper.onCreate(db);//创建新表
            restoreData(db, tableName, tempTableName);//恢复原表数据
            dropTable(db, tempTableName);//删除临时表
        }
    }


    /**
     * 创建零时表
     */
    private void createTempTables(SQLiteDatabase db, String tableName, String tempTableName) {
        copyTable(db, tableName, tempTableName);
    }


    /**
     * 删除所有表
     *
     * @param db
     */
    private void dropTable(SQLiteDatabase db, String tableName) {

        String sql = "DROP TABLE IF EXISTS " + tableName;

        db.execSQL(sql);

    }


    /**
     * 查询数据库中所有表名
     *
     * @param db
     * @return
     */
    private List<String> queryAllTables(SQLiteDatabase db) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM SQLITE_MASTER WHERE type='table' ORDER BY name";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;
    }


    /**
     * 复制表及内容
     *
     * @param db
     * @param oldTableName
     * @param newTableName
     */
    private void copyTable(SQLiteDatabase db, String oldTableName, String newTableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + newTableName + " AS SELECT * FROM " + oldTableName;
        db.execSQL(sql);
    }


    /**
     * 从临时表中恢复数据
     *
     * @param db
     * @param tableName     需要恢复的表
     * @param tableNameTemp 临时表
     */
    private void restoreData(SQLiteDatabase db, String tableName, String tableNameTemp) {


        String columns = TextUtils.join(",", queryColumns(db, tableNameTemp));

        String sql = "INSERT INTO " + tableName + "(" + columns + ") SELECT " + columns + " FROM " + tableNameTemp;

        db.execSQL(sql);

    }

    /**
     * 获取表中所有字段名
     *
     * @return
     */
    private String[] queryColumns(SQLiteDatabase db, String tableName) {

        String sql = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        cursor.close();

        return columnNames;
    }
}
