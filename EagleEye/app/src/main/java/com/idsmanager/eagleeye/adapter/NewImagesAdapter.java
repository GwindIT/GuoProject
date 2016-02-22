package com.idsmanager.eagleeye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.idsmanager.eagleeye.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by 雅麟 on 2015/7/10.
 */
public class NewImagesAdapter extends RecyclerView.Adapter<NewImagesViewHolder> {
    public interface ImageItemClickListener {
        void onItemClicked(View view, String image, int position);

        void onAddImageClicked(View view);
    }

    private static final int ITEM_TYPE = 1;
    private static final int MORE_TYPE = 2;

    private Context mContext;
    private List<String> mImageUris;
    private ImageItemClickListener mListener;

    public NewImagesAdapter(Context context, List<String> newImages, ImageItemClickListener listener) {
        mContext = context;
        mImageUris = newImages;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 6) {
            if (mImageUris.size() >= getItemCount()) {
                return ITEM_TYPE;
            }
        }
        if (position == getItemCount() - 1) {
            return MORE_TYPE;
        }
        return ITEM_TYPE;
    }

    @Override
    public NewImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_image, parent, false);
        return new NewImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewImagesViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE:
                bindItem(holder, position);
                break;
            case MORE_TYPE:
                bindMore(holder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if (mImageUris == null) {
            count = 1;
        } else {
            if (mImageUris.size() >= 6) {
                count = 6;
            } else {
                count = mImageUris.size() + 1;
            }
        }
        return count;
    }

    public void addImage(String imageUri) {
        if (TextUtils.isEmpty(imageUri)) {
            return;
        }
        if (mImageUris == null) {
            mImageUris = new ArrayList<>();
        }
        mImageUris.add(imageUri);
        notifyItemInserted(getImageCount());
    }

    public List<String> getImages() {
        return mImageUris == null ? null : mImageUris.subList(0, mImageUris.size());
    }

    private void bindItem(final NewImagesViewHolder holder, final int position) {
        final String item = mImageUris.get(position);
        System.out.println(item);
        Glide.with(mContext)
                .load(item)
                .into(holder.ivImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClicked(holder.ivImage, item, position);
                }
            }
        });
    }

    private void bindMore(final NewImagesViewHolder holder) {
        Glide.with(mContext)
                .load(R.drawable.add_pic)
                .into(holder.ivImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAddImageClicked(holder.ivImage);
                }
            }
        });
    }

    private int getImageCount() {
        return mImageUris == null ? 0 : mImageUris.size();
    }
}

class NewImagesViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    public NewImagesViewHolder(View itemView) {
        super(itemView);
        ivImage = (ImageView) itemView.findViewById(R.id.item_iv_image);
    }
}
