package com.codeclan.example.rememberme;

import android.provider.BaseColumns;

/**
 * Created by user on 08/08/2016.
 */
public final class TaskReaderContract {

    public TaskReaderContract() {}

    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_HEADING = "heading";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_DONE = "done";
    }

}
