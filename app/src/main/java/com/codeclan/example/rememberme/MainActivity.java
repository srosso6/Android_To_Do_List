package com.codeclan.example.rememberme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by user on 04/08/2016.
 */
public class MainActivity extends AppCompatActivity {

    private TextView mHeading;
    private CheckBox mCheckBox;
    private Button mDelButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this.deleteDatabase("reminders.db");
        db = new DatabaseHelper(this);
        getNotes();
    }

    public String getNote(int id) {
        EditText editText = (EditText) findViewById(id);
        String note = editText.getText().toString();
        editText.setText("");
        return note;
    }

    public void displayNote(String headingText, String contentText, String id) {
        ViewGroup layout = createLinearLayout(id);
        createTextView(headingText, contentText, layout, id);
        createCheckBox();
        createDelButton(id, layout);
        add(layout);
    }

    public void addNote(View view) {
        final String headingText = getNote(R.id.edit_note_heading);
        final String contentText = getNote(R.id.edit_note_content);
        String id = addToDb(headingText, contentText);
        displayNote(headingText, contentText, id);
    }

    public ViewGroup createLinearLayout(String id) {
        int idAsInt = Integer.parseInt(id);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(idAsInt);
        return layout;
    }

    public Bundle createBundle(String headingText, String contentText) {
        Bundle extras = new Bundle();
        extras.putString("heading", headingText);
        extras.putString("content", contentText);
        return extras;
    }

    public void startNewIntent(Bundle extras, Class activity) {
        Intent intent = new Intent(MainActivity.this, activity);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void createTextView(final String headingText, final String contentText, final ViewGroup layout, final String id) {
        mHeading = new TextView(this);
        mHeading.setTextSize(40);
        mHeading.setText(headingText);

        final EditText editText = new EditText(this);
        final TextView newHeading = new TextView(this);

        mHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = createBundle(headingText, contentText);
                startNewIntent(extras, DetailActivity.class);
            }
        });

        mHeading.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                layout.removeView(layout.getChildAt(0));
                layout.addView(editText, 0);
                return true;
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String newHeadingText = editText.getText().toString();
                    newHeading.setTextSize(40);
                    newHeading.setText(newHeadingText);
                    layout.removeView(editText);
                    layout.addView(newHeading, 0);
                    updateDb(id, newHeadingText);
                }
                return false;
            }
        });
    }

    public void createCheckBox() {
        mCheckBox = new CheckBox(this);
        mCheckBox.setText("Done");
        final TextView heading = mHeading;

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    heading.setPaintFlags(heading.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    heading.setPaintFlags(heading.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
    }

    public void createDelButton(final String id, final ViewGroup layout) {
        mDelButton = new Button(this);
        mDelButton.setText("Delete");

        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFromDb(id);
                remove(layout);
            }
        });
    }

    public void add(final ViewGroup layout) {
        layout.addView(mHeading);
        layout.addView(mCheckBox);
        layout.addView(mDelButton);
        ViewGroup noteList = (ViewGroup) findViewById(R.id.note_list);
        noteList.addView(layout);
    }

    public void remove(ViewGroup layout) {
        ViewGroup noteList = (LinearLayout)findViewById(R.id.note_list);
        noteList.removeView(layout);
    }

    public String addToDb(String headingText, String contentText){
        long result = db.insertData(headingText, contentText);
        if(result == -1) {
            Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
        }
        return Long.toString(result);
    }

    public void getNotes() {
        Cursor cursor = db.getAllData();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String heading = cursor.getString(cursor.getColumnIndex("heading"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                displayNote(heading, content, id);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public void updateDb(String id, String headingText) {
        db.updateData(id, headingText);
    }

    public void deleteFromDb(String id) {
        db.deleteData(id);
    }

}