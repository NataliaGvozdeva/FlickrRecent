package com.antonkokoryshkin.flickrrecent.UI.PhotoGallery;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.antonkokoryshkin.flickrrecent.R;
import com.antonkokoryshkin.flickrrecent.model.FAnswersResponse;
import com.antonkokoryshkin.flickrrecent.model.Photo;
import com.antonkokoryshkin.flickrrecent.service.FlickrApi;
import com.antonkokoryshkin.flickrrecent.service.FlickrService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGalleryFragment extends Fragment {

    private static final String API_KEY = "87688d80fc15bb8094ed97c85541d333";
    private static final String DIALOG_ABOUT_APP = "AboutAppDialog";
    private static final int MAX_COUNT_IMG = 1000;

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mPhotoRecyclerView;
    private RelativeLayout relativeLayout;
    private DialogFragment aboutAppDialog;

    private List<Photo> photos;
    private boolean isLoading = false;
    private int uploadingPage = 1;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        init(v);
        uploadImage(uploadingPage);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_app:
                if (getFragmentManager() != null) {
                    aboutAppDialog.show(getFragmentManager(), DIALOG_ABOUT_APP);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Инициализация элементов разметки пользовательского интерфейса
    public void init(View v) {
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.srl);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.rv);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.GONE);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        aboutAppDialog = new AboutAppDialogFragment();

        photos = new ArrayList<>();
        RVAdapter mAdapter = new RVAdapter(photos);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        mPhotoRecyclerView.setLayoutManager(gridLayoutManager);
        mPhotoRecyclerView.setAdapter(mAdapter);

        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ( (visibleItemCount+firstVisibleItems) >= totalItemCount){
                        if (photos.size() < MAX_COUNT_IMG) {
                            isLoading = true;
                            uploadingPage++;
                            uploadImage(uploadingPage);
                        }
                    }
                }

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoading = false;
                uploadingPage = 1;
                photos.clear();
                mPhotoRecyclerView.getAdapter().notifyDataSetChanged();

                uploadImage(1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

    }

    // Загрузка фотографий с помощью Retrofit 2 в POJO модель данных.
    public void uploadImage(int uploadingPage){
        FlickrApi flickrApi = FlickrService.getFlickrApi();

        Call<FAnswersResponse> call = flickrApi.getData("flickr.photos.getRecent", API_KEY, uploadingPage, "json", "1", "url_s");

        call.enqueue(new Callback<FAnswersResponse>() {
            @Override
            public void onResponse(@NonNull Call<FAnswersResponse> call, @NonNull Response<FAnswersResponse> response) {
                mPhotoRecyclerView.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);

                List<Photo> newPhotos = response.body().getPhotos().getPhoto();
                Collections.reverse(newPhotos);
                photos.addAll(newPhotos);

                mPhotoRecyclerView.getAdapter().notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<FAnswersResponse> call, @NonNull Throwable t) {
                mPhotoRecyclerView.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);

            }
        });
    }

}
