package com.acadgild.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }



    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // Method to insert the task data
    public void insert(String name, String desc, String date, String status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.DESC, desc);
        contentValue.put(DatabaseHelper.DATE, date);
        contentValue.put(DatabaseHelper.STATUS, status);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }
    // Method to fetch all the tasks in ascending order of date
    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC, DatabaseHelper.DATE, DatabaseHelper.STATUS };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, DatabaseHelper.DATE + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Method to fetch completed tasks in ascending order of date
    public Cursor fetch_complete() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC, DatabaseHelper.DATE, DatabaseHelper.STATUS };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, DatabaseHelper.STATUS + "=" + 1, null, null, null, DatabaseHelper.DATE + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    // Method to update the task
    public int update(String _id, String title, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, title);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return 0;
    }
    // Method to delete the task
    public void delete(String _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }
    // Method to change the status of the task
    public int status(String task_status, String hidden_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.STATUS, task_status);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + hidden_id, null);
        return 0;
    }

}
