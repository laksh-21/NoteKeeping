package com.example.quixote_login;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Note{
    public String title;
    public String description;
    public ArrayList<Bitmap> images = new ArrayList<>();

    public Note(String title, String description){
        this.title = title;
        this.description = description;
    }

    public Note(String title, String description, ArrayList<Bitmap> images){
        this.title = title;
        this.description = description;
        this.images = images;
    }
}
