package com.example.quixote_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quixote_login.Data.NotesContract;
import com.example.quixote_login.Data.NotesDatabaseHelper;
import com.example.quixote_login.Utilities.ImageSaver;

import java.util.ArrayList;
import java.util.BitSet;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView title;
    private TextView description;

    private NotesDatabaseHelper notesDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        notesDatabaseHelper = new NotesDatabaseHelper(this, getApplicationContext());

        viewPager = findViewById(R.id.viewPager);
        title = findViewById(R.id.tv_detail_title);
        description = findViewById(R.id.tv_deatil_desc);

        Intent intent = getIntent();
        long noteId = (Long)intent.getSerializableExtra("noteid");
        populateEverything(noteId);
    }

    private void populateEverything(long noteId){
        Cursor photos = notesDatabaseHelper.getAllPhotosForNote(noteId);

        ArrayList<Bitmap> images = new ArrayList<>();

        while (photos.moveToNext()){
            String path = photos.getString(photos.getColumnIndex(NotesContract.PhotoEntry.COLUMN_PHOTO));
            String name = photos.getString(photos.getColumnIndex(NotesContract.PhotoEntry.COLUMN_PHOTO_NAME));
            Bitmap bm = ImageSaver.loadImageFromStorage(path, name);
            images.add(bm);
        }

        Cursor note = notesDatabaseHelper.getNoteById(noteId);
        note.moveToFirst();

        title.setText(note.getString(note.getColumnIndex(NotesContract.NoteEntry.COLUMN_TITLE)));
        description.setText(note.getString(note.getColumnIndex(NotesContract.NoteEntry.COLUMN_DESCRIPTION)));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, images);
        viewPager.setAdapter(viewPagerAdapter);
    }
}