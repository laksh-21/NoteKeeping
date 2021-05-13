package com.example.quixote_login.Data;

import android.provider.BaseColumns;

public class NotesContract {
    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notestable";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
    }

    public static class PhotoEntry implements BaseColumns{
        public static final String TABLE_NAME = "photostable";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NOTE_ID = "note_id";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_PHOTO_NAME = "name";
    }
}
