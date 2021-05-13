package com.example.quixote_login;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quixote_login.Data.NotesContract;
import com.example.quixote_login.Data.NotesDatabaseHelper;
import com.example.quixote_login.Utilities.ImageSaver;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;

    private Cursor data;

    private NotesDatabaseHelper notesDatabaseHelper;

    public NotesAdapter(Context context, Cursor data){
        this.context = context;
        this.data = data;

        notesDatabaseHelper = new NotesDatabaseHelper(context, context.getApplicationContext());
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        data.moveToPosition(position);
        holder.getTitleText().setText(data.getString(data.getColumnIndex(NotesContract.NoteEntry.COLUMN_TITLE)));
        holder.getDescText().setText(data.getString(data.getColumnIndex(NotesContract.NoteEntry.COLUMN_DESCRIPTION)));

        Cursor photosCursor = notesDatabaseHelper.getAllPhotosForNote(data.getLong(data.getColumnIndex(NotesContract.NoteEntry._ID)));
        Log.e("Hello", ""+photosCursor.getCount());
        Log.e("Hello", "" + photosCursor.moveToFirst());
        if(photosCursor.getCount() > 0){
            photosCursor.moveToFirst();
            String path = photosCursor.getString(photosCursor.getColumnIndex(NotesContract.PhotoEntry.COLUMN_PHOTO));
            String name = photosCursor.getString(photosCursor.getColumnIndex(NotesContract.PhotoEntry.COLUMN_PHOTO_NAME));
            Bitmap bm = ImageSaver.loadImageFromStorage(path, name);
            holder.getNoteImage().setImageBitmap(bm);
        }
    }

    @Override
    public int getItemCount() {
        return data.getCount();
    }

    public void swapCursor(Cursor cursor){
        data = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView noteImage;
        private TextView titleText;
        private TextView descText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteImage = itemView.findViewById(R.id.iv_note);
            titleText = itemView.findViewById(R.id.tv_note_title);
            descText = itemView.findViewById(R.id.tv_note_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                    int position = getAdapterPosition();
                    data.moveToPosition(position);
                    intent.putExtra("noteid", data.getLong(data.getColumnIndex(NotesContract.NoteEntry._ID)));
                    v.getContext().startActivity(intent);
                }
            });
        }

        public ImageView getNoteImage() {
            return noteImage;
        }

        public TextView getTitleText() {
            return titleText;
        }

        public TextView getDescText() {
            return descText;
        }
    }
}
