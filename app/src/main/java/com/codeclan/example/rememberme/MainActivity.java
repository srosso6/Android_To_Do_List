package com.codeclan.example.rememberme;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import java.util.ArrayList;

/**
 * Created by user on 04/08/2016.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ViewGroup> notes = getNotes();
        for(ViewGroup note : notes) {
            displayNote(note);
        }
    }

    public ViewGroup getNoteList() {
        return (ViewGroup) findViewById(R.id.note_list);
    }

    public void displayNote(ViewGroup note) {
        ViewGroup noteList = getNoteList();
        noteList.addView(note);
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
        String id = addToDb(this, headingText, contentText, 0);
        ViewGroup note = createNote(headingText, contentText, 0, id) ;
        displayNote(note);
    }

}