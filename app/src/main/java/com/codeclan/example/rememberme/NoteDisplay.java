package com.codeclan.example.rememberme;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
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
//    private TextView mContent;
    private String mHeadingText;
    private String mContentText;
    private CheckBox mCheckBox;
    private Button mDelButton;
    private int mTaskComplete;
    protected Activity mContext;

    public NoteDisplay(Activity context, String headingText, String contentText, int taskComplete, String id) {
//        mContext = (Activity) context.getApplicationContext();
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
        setListeners(vLayout);
        return vLayout;
    }

    public ViewGroup createVerticalLinearLayout() {
        int idAsInt = Integer.parseInt(mId);
        LinearLayout vLayout = new LinearLayout(mContext);
        vLayout.setOrientation(LinearLayout.VERTICAL);
        vLayout.setBackgroundResource(R.drawable.shape_notes);
        vLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        vLayout.setId(idAsInt);
        return vLayout;
    }

    public ViewGroup createHorizontalLinearLayout() {
        LinearLayout hLayout = new LinearLayout(mContext);
        hLayout.setOrientation(LinearLayout.HORIZONTAL);
        return hLayout;
    }

    public ViewGroup getVerticalLinearLayout() {
        return (ViewGroup) mContext.findViewById(Integer.parseInt(mId));

    }

    public ViewGroup getHorizontalLinearLayout() {
        ViewGroup vLayout = getVerticalLinearLayout();
        return (ViewGroup) vLayout.getChildAt(0);
    }

    public TextView createHeading() {
        mHeading.setTextSize(30);
        mHeading.setText(mHeadingText);
        mHeading.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.3f));
        return mHeading;
    }

    public CheckBox createCheckBox() {
        mCheckBox = new CheckBox(mContext);
        mCheckBox.setText("Done");
        mCheckBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.7f));
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

    public EditText createEditText(String text) {
        EditText editText = new EditText(mContext);
        editText.setBackgroundResource(R.drawable.edit_text_notes);
        editText.setText(text);
        return editText;
    }

    public Bundle createBundle() {
        Bundle extras = new Bundle();
        extras.putString("id", mId);
        extras.putString("headingText", mHeadingText);
        extras.putString("contentText", mContentText);
        extras.putInt("taskComplete", mTaskComplete);
        return extras;
    }

    public void startNewIntent(Bundle extras) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

    public void setListeners(ViewGroup vLayout) {

        final EditText editTextHeading = createEditText(mHeadingText);
        final ViewGroup noteList = mContext.getNoteList();

        vLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DragShadow dragShadow = new DragShadow(view);
                ClipData data = ClipData.newPlainText("", "");
                view.startDrag(data, dragShadow, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        vLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {

                ViewGroup target = (ViewGroup) view;
                ViewGroup dragged = (ViewGroup) event.getLocalState();

                final int targetIndex = noteList.indexOfChild(target);
                final int draggedIndex = noteList.indexOfChild(dragged);

                int dragEvent = event.getAction();

                switch(dragEvent) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d("drag", "entered");
                        noteList.removeView(target);
                        noteList.addView(target, draggedIndex);
                        noteList.removeView(dragged);
                        noteList.addView(dragged, targetIndex);
                        dragged.setVisibility(View.INVISIBLE);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d("drag", "exited");
                        dragged.setVisibility(View.VISIBLE);
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d("drag", "dropped");
                        dragged.setVisibility(View.VISIBLE);
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d("drag", "ended");
                        break;
                }
                return true;
            }
        });

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
                ViewGroup hLayout = getHorizontalLinearLayout();
                hLayout.removeView(hLayout.getChildAt(0));
                hLayout.addView(editTextHeading, 0);
                return true;
            }
        });

        editTextHeading.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mHeadingText = editTextHeading.getText().toString();
                    mHeading = createHeading();

                    ViewGroup hLayout = getHorizontalLinearLayout();
                    hLayout.removeView(editTextHeading);
                    hLayout.addView(mHeading, 0);
                    mContext.updateDb(mId, mHeadingText, mContentText, mTaskComplete);
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
                mContext.updateDb(mId, mHeadingText, mContentText, mTaskComplete);
            }
        });

        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup vLayout = getVerticalLinearLayout();
                mContext.removeNote(vLayout);
                mContext.deleteFromDb(mId);
            }
        });

    }
}