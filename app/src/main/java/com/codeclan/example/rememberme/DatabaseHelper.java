package com.codeclan.example.rememberme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 06/08/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "reminders.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TaskReaderContract.TaskEntry.TABLE_NAME + " (" + TaskReaderContract.TaskEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TaskReaderContract.TaskEntry.COLUMN_NAME_HEADING + " TEXT, " + TaskReaderContract.TaskEntry.COLUMN_NAME_CONTENT + " TEXT, " + TaskReaderContract.TaskEntry.COLUMN_NAME_DONE + " INTEGER)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TaskReaderContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }

    public long insertData(String heading, String content, int done) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskReaderContract.TaskEntry.COLUMN_NAME_HEADING, heading);
        contentValues.put(TaskReaderContract.TaskEntry.COLUMN_NAME_CONTENT, content);
        contentValues.put(TaskReaderContract.TaskEntry.COLUMN_NAME_DONE, done);
        return db.insert(TaskReaderContract.TaskEntry.TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TaskReaderContract.TaskEntry.TABLE_NAME, null);
    }

    public boolean updateData(String id, String heading, int done) {
        //need to add content in here too once refactored
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskReaderContract.TaskEntry.COLUMN_NAME_ID, id);
        contentValues.put(TaskReaderContract.TaskEntry.COLUMN_NAME_HEADING, heading);
//        contentValues.put(TaskReaderContract.TaskEntry.COLUMN_NAME_CONTENT, content);
        contentValues.put(TaskReaderContract.TaskEntry.COLUMN_NAME_DONE, done);
        db.update(TaskReaderContract.TaskEntry.TABLE_NAME, contentValues, "id = ?", new String[]{id});
        return true;
    }

    public int deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TaskReaderContract.TaskEntry.TABLE_NAME, "id = ?", new String[] {id});
    }

}
