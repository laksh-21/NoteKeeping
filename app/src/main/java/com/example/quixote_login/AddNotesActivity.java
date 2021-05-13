package com.example.quixote_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quixote_login.Data.NotesDatabaseHelper;
import com.example.quixote_login.Utilities.Validator;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddNotesActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private EditText title;
    private EditText description;
    private TextView titleCount;
    private TextView descCount;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private NotesDatabaseHelper notesDatabaseHelper;

    private final int GALLERY_REQUEST_CODE = 100;
    private final int CAMERA_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setTitle("Add a Note");

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPagerAdapter.images.size() <= 10) {
                    cameraOrGallery();
                } else{
                    Toast.makeText(AddNotesActivity.this, "Can only add upto 10 images",Toast.LENGTH_LONG).show();
                }
            }
        });
        title = findViewById(R.id.et_title_edit);
        description = findViewById(R.id.et_description_edit);

        titleCount = findViewById(R.id.tv_title_count);
        descCount = findViewById(R.id.tv_desc_count);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                titleCount.setText(String.valueOf(s.length()));
                if (s.length() > 100 || s.length() < 5) {
                    titleCount.setTextColor(Color.RED);
                } else {
                    titleCount.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descCount.setText(String.valueOf(s.length()));
                if (s.length() > 1000 || s.length() < 100) {
                    descCount.setTextColor(Color.RED);
                } else {
                    descCount.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewPager = findViewById(R.id.edit_pager);
        viewPagerAdapter = new ViewPagerAdapter(this, new ArrayList<Bitmap>());
        viewPager.setAdapter(viewPagerAdapter);

        notesDatabaseHelper = new NotesDatabaseHelper(this, getApplicationContext());
    }

    private void cameraOrGallery() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose what happens");
        builder1.setItems(new CharSequence[]{"Camera", "Gallery", "Cancel"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                camera();
                                dialog.dismiss();
                                break;
                            case 1:
                                gallery();
                                dialog.dismiss();
                                break;
                            default:
                                dialog.cancel();
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private void gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                viewPagerAdapter.addImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            viewPagerAdapter.addImage(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case R.id.done_btn:
                validateInputs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void validateInputs(){
        String titleString = title.getText().toString().trim();
        String descString = description.getText().toString().trim();

        if(!Validator.validateTitle(titleString)){
            title.setError("Minimum 5 and maximum 100 characters allowed");
            return;
        }

        if(!Validator.validateDescription(descString)){
            description.setError("Minimum 100 and maximum 1000 characters allowed");
            return;
        }

        Note note = new Note(titleString, descString, viewPagerAdapter.images);
        notesDatabaseHelper.addNote(note);
        finish();
    }
}