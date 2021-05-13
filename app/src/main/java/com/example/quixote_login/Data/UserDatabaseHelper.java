package com.example.quixote_login.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.quixote_login.Model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "userbase.db";

    public class UserEntry{
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_USER_ID = "_id";
        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_PHONE = "phone";
        public static final String COLUMN_USER_EMAIL = "email";
        public static final String COLUMN_USER_PASSWORD = "password";

        public static final String CREATE_SQL_STATEMENT = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
                COLUMN_USER_NAME + " TEXT NOT NULL, " +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_USER_PHONE + " TEXT NOT NULL" + " );";

        public static final String DROP_SQL_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserEntry.CREATE_SQL_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserEntry.DROP_SQL_STATEMENT);
        onCreate(db);
    }

    public final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String mail = user.getEmail();
        String phone = user.getPhoneNumber();
        String password = user.getPassword();
        String name = user.getName();

        password = md5(password);

        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USER_EMAIL, mail);
        values.put(UserEntry.COLUMN_USER_NAME, name);
        values.put(UserEntry.COLUMN_USER_PASSWORD, password);
        values.put(UserEntry.COLUMN_USER_PHONE, phone);

        db.insert(UserEntry.TABLE_NAME, null, values);
        db.close();
    }

    public boolean userExists(String email){
        String projection[] = {UserEntry.COLUMN_USER_ID};

        String selection = UserEntry.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = new String[]{email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return (count > 0);
    }

    public boolean userRegistered(String email, String password){
        String projection[] = {UserEntry.COLUMN_USER_ID};

        String selection = UserEntry.COLUMN_USER_EMAIL + " = ?" + " AND " + UserEntry.COLUMN_USER_PASSWORD + " = ?";

        password = md5(password);
        String[] selectionArgs = new String[]{email, password};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return (count > 0);
    }
}
