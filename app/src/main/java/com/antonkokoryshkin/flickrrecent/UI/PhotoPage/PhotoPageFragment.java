package com.antonkokoryshkin.flickrrecent.UI.PhotoPage;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonkokoryshkin.flickrrecent.R;
import com.squareup.picasso.Picasso;

public class PhotoPageFragment extends Fragment {
    private static final String ARG_URI = "com.antonkokoryshkin.flickrrecent.photo_url";

    private Uri mUri;
    private ImageView imageView;

    public static PhotoPageFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        PhotoPageFragment fragment = new PhotoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(ARG_URI);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_page, container, false);

        imageView = (ImageView) v.findViewById(R.id.item_image_view);

        Picasso.with(getContext())
                .load(mUri)
                .into(imageView);
        return v;
    }

}
