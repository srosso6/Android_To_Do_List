package com.codeclan.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by user on 05/08/2016.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String headingText = extras.getString("heading");
        String contentText = extras.getString("content");

        setTitle(headingText);

        TextView textView = (TextView)findViewById(R.id.note_content);
        textView.setText(contentText);
    }



}
