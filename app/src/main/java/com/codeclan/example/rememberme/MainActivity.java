package com.codeclan.example.rememberme;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by user on 04/08/2016.
 */
public class MainActivity extends AppCompatActivity {

    NoteDisplay mNoteDisplay;
    DatabaseHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this.deleteDatabase("reminders.db");
        mDb = new DatabaseHelper(this);
        getNotes();
    }

    public String getNoteText(int id) {
        EditText editText = (EditText) findViewById(id);
        String text = editText.getText().toString();
        editText.setText("");
        return text;
    }

    public void addNote(View view) {
        String headingText = getNoteText(R.id.edit_note_heading);
        String contentText = getNoteText(R.id.edit_note_content);
        String id = addToDb(headingText, contentText, 0);
        mNoteDisplay = new NoteDisplay(this, headingText, contentText, 0, id);
        ViewGroup note = mNoteDisplay.buildNote();
        displayNote(note);
    }

    public ViewGroup getNoteList() {
        return (ViewGroup) findViewById(R.id.note_list);
    }

    public ViewGroup getNote(String id) {
        return (ViewGroup) findViewById(Integer.parseInt(id));
    }

    public ViewGroup getNoteInnerLayout(String id) {
        ViewGroup layout = (ViewGroup) findViewById(Integer.parseInt(id));
        return (ViewGroup) layout.getChildAt(0);
    }

    public void displayNote(ViewGroup note) {
        ViewGroup noteList = getNoteList();
        noteList.addView(note);
    }

    public void enableHeaderEdit(EditText editText,String id) {
        ViewGroup note = getNoteInnerLayout(id);
        note.removeView(note.getChildAt(0));
        note.addView(editText, 0);
    }

    public void updateHeader(EditText editText, TextView heading, String headingText, int taskComplete, String id) {
        ViewGroup note = getNoteInnerLayout(id);
        note.removeView(editText);
        note.addView(heading, 0);
        updateDb(id, headingText, taskComplete);
    }

    public void removeNote(ViewGroup note, String id) {
        ViewGroup noteList = getNoteList();
        noteList.removeView(note);
        deleteFromDb(id);
    }

    public String addToDb(String headingText, String contentText, int taskComplete){
        long result = mDb.insertData(headingText, contentText, taskComplete);
        if(result == -1) {
            Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
        }
        return Long.toString(result);
    }

    public void getNotes() {
        Cursor cursor = mDb.getAllData();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String headingText = cursor.getString(cursor.getColumnIndex("heading"));
                String contentText = cursor.getString(cursor.getColumnIndex("content"));
                int taskComplete = Integer.parseInt(cursor.getString(cursor.getColumnIndex("done")));
                mNoteDisplay = new NoteDisplay(this, headingText, contentText, taskComplete, id);
                ViewGroup note = mNoteDisplay.buildNote();
                displayNote(note);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public void updateDb(String id, String headingText, int taskComplete) {
        mDb.updateData(id, headingText, taskComplete);
    }

    public void deleteFromDb(String id) {
        mDb.deleteData(id);
    }

}