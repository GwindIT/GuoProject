package com.idsmanager.eagleeye.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by wind on 2016/2/22.
 */
public class ImagesAdapter extends PagerAdapter {

    protected List<Bitmap> list;
    protected Context context;

    public ImagesAdapter(Context context, List<Bitmap> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list.size() != 1) {
            return list.size() * 1000 * 100;
        } else {
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % list.size();
        ImageView imageView = new ImageView(context);
        container.addView(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(list.get(position));
        return imageView;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
