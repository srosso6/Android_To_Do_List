package com.codeclan.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by user on 05/08/2016.
 */
public class DetailActivity extends Activity {

    private TextView mContent;
    private String mContentText;
    private String mHeadingText;
    private String mId;
    private int mTaskComplete;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mId = extras.getString("id");
        mHeadingText = extras.getString("headingText");
        mContentText = extras.getString("contentText");
        mTaskComplete = extras.getInt("taskComplete");

        setTitle(mHeadingText);

        mContent = (TextView) findViewById(R.id.note_content);
        setContent();
        setListeners();

    }

    public void showCalendar() {
//        Calendar calendar = new GregorianCalendar();

    }

    public void setContent() {
        mContent.setText(mContentText);
    }

    public void setListeners() {
        final EditText editTextContent = createEditText(mContentText);

        mContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ViewGroup noteBoard = getNoteBoard();
                noteBoard.removeView(noteBoard.getChildAt(0));
                noteBoard.addView(editTextContent, 0);
                return true;
            }
        });

        editTextContent.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mContentText = editTextContent.getText().toString();
                    setContent();

                    ViewGroup noteBoard = getNoteBoard();
                    noteBoard.removeView(editTextContent);
                    noteBoard.addView(mContent, 0);
                    updateDb(mId, mHeadingText, mContentText, mTaskComplete);
                }
                return false;
            }
        });
    }

    public EditText createEditText(String text) {
        EditText editText = new EditText(this);
        editText.setBackgroundResource(R.drawable.edit_text_notes);
        editText.setText(text);
        return editText;
    }

}
