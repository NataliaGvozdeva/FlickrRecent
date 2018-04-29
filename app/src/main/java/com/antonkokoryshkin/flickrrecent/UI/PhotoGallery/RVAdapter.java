package com.antonkokoryshkin.flickrrecent.UI.PhotoGallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonkokoryshkin.flickrrecent.R;
import com.antonkokoryshkin.flickrrecent.UI.PhotoPage.PhotoPageActivity;
import com.antonkokoryshkin.flickrrecent.model.Photo;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PhotoViewHolder> {

    private static final String EXTRA_PHOTO_LIST = "com.antonkokoryshkin.flickrrecent.photo_list";
    private static final String EXTRA_PHOTO_POSITION = "com.antonkokoryshkin.flickrrecent.photo_position";

    private List<Photo> photos;

    public RVAdapter(List<Photo> photos){
        this.photos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumblail_photo_item, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Photo photo = photos.get(position);
        holder.bindPhotoItem(photo);
        holder.mItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = PhotoPageActivity.newIntent(v.getContext(), photo.getPhotoUri());
                i.putExtra(EXTRA_PHOTO_LIST, (Serializable) photos);
                i.putExtra(EXTRA_PHOTO_POSITION, position);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImageView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.item_image_view);
        }

        public void bindPhotoItem(Photo photo) {
            Picasso.with(mItemImageView.getContext())
                    .load(photo.getUrlS())
                    .placeholder(R.drawable.placeholder)
                    .into(mItemImageView);
        }
    }

}
