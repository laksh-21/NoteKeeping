package com.example.quixote_login;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Objects;

class ViewPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    ArrayList<Bitmap> images;

    public ViewPagerAdapter(Context context, ArrayList<Bitmap> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void swapData(ArrayList<Bitmap> images){
        this.images = images;
        this.notifyDataSetChanged();
    }

    public void addImage(Bitmap bm){
        this.images.add(bm);
        this.notifyDataSetChanged();
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.detail_image_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageViewMain);

        imageView.setImageBitmap(images.get(position));
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
