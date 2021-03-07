package com.example.studentsdirectory.database;

import android.provider.BaseColumns;

public final class DetailsContract {
        private DetailsContract(){}
        public static class DetailsEntry implements BaseColumns {
            public static final String DB_NAME = "studentDb";
            public static final String TABLE_NAME = "studentDetails";
            public static final String COLUMN_NAME_STUDENT = "Name";
            public static final String COLUMN_NAME_DOB = "DOB";
            public static final String COLUMN_NAME_DEPARTMENT = "Department";
        }
    }

