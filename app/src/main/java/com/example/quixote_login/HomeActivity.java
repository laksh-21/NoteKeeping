package com.example.quixote_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.example.quixote_login.Data.NotesDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private FloatingActionButton floatingActionButton;

    private NotesDatabaseHelper notesDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Notes");
        notesDatabaseHelper = new NotesDatabaseHelper(this, getApplicationContext());

        floatingActionButton = findViewById(R.id.add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddNotesActivity.class);
                startActivity(intent);
//                sendDummyData();
            }
        });

        notesAdapter = new NotesAdapter(this, notesDatabaseHelper.getAllNotes());

        recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor cursor = notesDatabaseHelper.getAllNotes();
        notesAdapter.swapCursor(cursor);
    }

    private void sendDummyData(){
        Note note = new Note("Flowers", getString(R.string.lorem_ipsum));
        note.images.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.flower));
        note.images.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.flower1));
        notesDatabaseHelper.addNote(note);
    }
}