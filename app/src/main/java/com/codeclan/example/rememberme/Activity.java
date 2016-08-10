package com.codeclan.example.rememberme;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by user on 10/08/2016.
 */
public abstract class Activity extends AppCompatActivity {

    DatabaseHelper mDb;
    NoteDisplay mNoteDisplay;
    ArrayList<ViewGroup> mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.deleteDatabase("reminders.db");
        mDb = new DatabaseHelper(this);
    }

    public String addToDb(Activity context, String headingText, String contentText, int taskComplete){
        long result = mDb.insertData(headingText, contentText, taskComplete);
        if(result == -1) {
            Toast.makeText(context, "Data Not Inserted", Toast.LENGTH_LONG).show();
        }
        return Long.toString(result);
    }

    public ViewGroup createNote(String headingText, String contentText, int taskComplete, String id) {
        mNoteDisplay = new NoteDisplay(Activity.this, headingText, contentText, taskComplete, id);
        return mNoteDisplay.buildNote();
    }

    public ArrayList<ViewGroup> getNotes() {
        Cursor cursor = mDb.getAllData();
        mNotes = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String headingText = cursor.getString(cursor.getColumnIndex("heading"));
                String contentText = cursor.getString(cursor.getColumnIndex("content"));
                int taskComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex("done")));
                ViewGroup note = createNote(headingText, contentText, taskComplete, id);
                mNotes.add(note);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return mNotes;
    }

    public ViewGroup getNoteBoard() {
        return (ViewGroup) findViewById(R.id.note_board);
    }

    public ViewGroup getNoteList() {
        return (ViewGroup) findViewById(R.id.note_list);
    }

    public void updateDb(String id, String headingText, String contentText, int taskComplete) {
        mDb.updateData(id, headingText, contentText, taskComplete);
    }

    public void removeNote(ViewGroup note) {
        ViewGroup noteList = getNoteList();
        noteList.removeView(note);
    }

    public void deleteFromDb(String id) {
        mDb.deleteData(id);
    }

}
