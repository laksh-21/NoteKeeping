package com.example.quixote_login.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.quixote_login.Note;
import com.example.quixote_login.Utilities.ImageSaver;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 3;

    public static final String NOTE_TABLE_CREATE = "CREATE TABLE " +
            NotesContract.NoteEntry.TABLE_NAME + " ( " +
            NotesContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NotesContract.NoteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            NotesContract.NoteEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL " +
            ");";

    public static final String PHOTO_TABLE_CREATE = "CREATE TABLE " +
            NotesContract.PhotoEntry.TABLE_NAME + " ( " +
            NotesContract.PhotoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NotesContract.PhotoEntry.COLUMN_NOTE_ID + " INTEGER NOT NULL, " +
            NotesContract.PhotoEntry.COLUMN_PHOTO + " TEXT NOT NULL, " +
            NotesContract.PhotoEntry.COLUMN_PHOTO_NAME + " TEXT NOT NULL" +
            ");";

    public static final String NOTE_TABLE_DROP = "DROP TABLE IF EXISTS " + NotesContract.NoteEntry.TABLE_NAME;
    public static final String PHOTO_TABLE_DROP = "DROP TABLE IF EXISTS " + NotesContract.PhotoEntry.TABLE_NAME;

    Context applicationContext;
    public NotesDatabaseHelper(@Nullable Context context, Context applicationContext) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.applicationContext = applicationContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTE_TABLE_CREATE);
        db.execSQL(PHOTO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(NOTE_TABLE_DROP);
        db.execSQL(PHOTO_TABLE_DROP);
        onCreate(db);
    }

    public Cursor getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[]{
                NotesContract.NoteEntry._ID,
                NotesContract.NoteEntry.COLUMN_TITLE,
                NotesContract.NoteEntry.COLUMN_DESCRIPTION
        };

        return db.query(NotesContract.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getAllPhotosForNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[]{
                NotesContract.PhotoEntry._ID,
                NotesContract.PhotoEntry.COLUMN_PHOTO,
                NotesContract.PhotoEntry.COLUMN_PHOTO_NAME
        };

        String selection = NotesContract.PhotoEntry.COLUMN_NOTE_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        return db.query(NotesContract.PhotoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public Cursor getNoteById(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[]{
                NotesContract.NoteEntry.COLUMN_TITLE,
                NotesContract.NoteEntry.COLUMN_DESCRIPTION
        };

        String selection = NotesContract.NoteEntry._ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        return db.query(NotesContract.NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public int addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Bitmap> images = note.images;
        String title = note.title;
        String description = note.description;

//        Inserting into note table
        ContentValues noteValues = new ContentValues();
        noteValues.put(NotesContract.NoteEntry.COLUMN_TITLE, title);
        noteValues.put(NotesContract.NoteEntry.COLUMN_DESCRIPTION, description);
        long noteId = db.insert(NotesContract.NoteEntry.TABLE_NAME, null, noteValues);

        if(noteId == -1) {
            return -1;
        }

//        Inserting into photos table
        int count = 0;
        for(Bitmap img: images){
            ContentValues photoValues = new ContentValues();
            photoValues.put(NotesContract.PhotoEntry.COLUMN_NOTE_ID, noteId);

            String name = generateName(count++, noteId);
            String path = ImageSaver.saveToInternalStorage(applicationContext, img, name);

            photoValues.put(NotesContract.PhotoEntry.COLUMN_PHOTO, path);
            photoValues.put(NotesContract.PhotoEntry.COLUMN_PHOTO_NAME, name);
            long photoId = db.insert(NotesContract.PhotoEntry.TABLE_NAME, null, photoValues);
            if(photoId == -1){
                return -1;
            }
        }

        return 1;
    }

    private String generateName(int count, long noteId){
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(count) + String.valueOf(noteId);
    }

//    private ArrayList<byte[]> createByteArray(ArrayList<Bitmap> images){
//        ArrayList<byte[]> imagesBytes = new ArrayList<>();
//        for(Bitmap bmp: images){
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 10, stream);
//            byte[] byteArray = stream.toByteArray();
//            imagesBytes.add(byteArray);
//            bmp.recycle();
//        }
//
//        return imagesBytes;
//    }
}
