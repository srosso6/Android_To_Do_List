package com.codeclan.example.rememberme;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by user on 09/08/2016.
 */
public class NoteDisplay {

    private String mId;
    private TextView mHeading;
    private String mHeadingText;
    private String mContentText;
    private CheckBox mCheckBox;
    private Button mDelButton;
    private int mTaskComplete;
    protected MainActivity mContext;

    public NoteDisplay(MainActivity context, String headingText, String contentText, int taskComplete, String id) {
//        this.context = context.getApplicationContext();
        mContext = context;
        mHeadingText = headingText;
        mContentText = contentText;
        mId = id;
        mTaskComplete = taskComplete;
        mHeading = new TextView(mContext);
    }

    public ViewGroup buildNote() {
        ViewGroup hLayout = createHorizontalLinearLayout();
        hLayout.addView(createHeading());
        hLayout.addView(createCheckBox());
        ViewGroup vLayout = createVerticalLinearLayout();
        vLayout.addView(hLayout);
        vLayout.addView(createDelButton());
        setListeners();
        return vLayout;
    }

    public ViewGroup createVerticalLinearLayout() {
        int idAsInt = Integer.parseInt(mId);
        LinearLayout vLayout = new LinearLayout(mContext);
        vLayout.setOrientation(LinearLayout.VERTICAL);
        vLayout.setBackgroundResource(R.drawable.shape_notes);
        vLayout.setId(idAsInt);
        return vLayout;
    }

    public ViewGroup createHorizontalLinearLayout() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }

    public TextView createHeading() {
        mHeading.setTextSize(40);
        mHeading.setText(mHeadingText);
        return mHeading;
    }

    public CheckBox createCheckBox() {
        mCheckBox = new CheckBox(mContext);
        mCheckBox.setText("Done");
        return mCheckBox;
    }

    public Button createDelButton() {
        mDelButton = new Button(mContext);
        mDelButton.setText("Delete");
        mDelButton.setBackgroundResource(R.drawable.del_button);
        mDelButton.setWidth(50);
        mDelButton.setHeight(70);
        return mDelButton;
    }

    public EditText createEditText() {
        EditText editText = new EditText(mContext);
        editText.setBackgroundResource(R.drawable.edit_text_notes);
        editText.setText(mHeadingText);
        return editText;
    }

    public Bundle createBundle() {
        Bundle extras = new Bundle();
        extras.putString("heading", mHeadingText);
        extras.putString("content", mContentText);
        return extras;
    }

    public void startNewIntent(Bundle extras) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

    public void setListeners() {

        final EditText editText = createEditText();

        mHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = createBundle();
                startNewIntent(extras);
            }
        });

        mHeading.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mContext.enableHeaderEdit(editText, mId);
                return true;
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mHeadingText = editText.getText().toString();
                    mHeading = createHeading();
                    mContext.updateHeader(editText, mHeading, mHeadingText, mTaskComplete,  mId);
                }
                return false;
            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mHeading.setPaintFlags(mHeading.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    mTaskComplete = 1;
                } else {
                    mHeading.setPaintFlags(mHeading.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    mTaskComplete = 0;
                }
                mContext.updateDb(mId, mHeadingText, mTaskComplete);
            }
        });

        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup vLayout = mContext.getNote(mId);
                mContext.removeNote(vLayout, mId);
            }
        });
    }
}