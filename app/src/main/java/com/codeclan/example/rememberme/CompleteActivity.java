package com.codeclan.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by user on 06/08/2016.
 */
public class CompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String headingText = extras.getString("heading");
        String contentText = extras.getString("content");

        TextView heading = new TextView(this);
        heading.setTextSize(40);
        heading.setText(headingText);

        TextView content = new TextView(this);
        content.setTextSize(30);
        content.setText(contentText);

        ViewGroup layout = (ViewGroup) findViewById(R.id.completed);
        layout.addView(heading);
        layout.addView(content);

    }
}
