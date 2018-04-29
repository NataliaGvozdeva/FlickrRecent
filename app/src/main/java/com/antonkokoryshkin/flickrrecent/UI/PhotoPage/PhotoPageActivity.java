package com.antonkokoryshkin.flickrrecent.UI.PhotoPage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.antonkokoryshkin.flickrrecent.R;
import com.antonkokoryshkin.flickrrecent.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoPageActivity extends AppCompatActivity {

    private static final String EXTRA_PHOTO_LIST = "com.antonkokoryshkin.flickrrecent.photo_list";
    private static final String EXTRA_PHOTO_POSITION = "com.antonkokoryshkin.flickrrecent.photo_position";

    private Toolbar mToolbar;
    private ViewPager mViewPager;

    private List<Photo> photos;
    private Uri photoPageUri;
    private Uri photoUri;
    private int photoPosition;

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent i = new Intent(context, PhotoPageActivity.class);
        i.setData(photoPageUri);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_browser:
                menuBrowser(photoPageUri);
                return true;
            case R.id.share:
                menuShare(photoUri);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Инициализация элементов разметки пользовательского интерфейса
    private void init(){
        photos = (ArrayList<Photo>) getIntent().getSerializableExtra(EXTRA_PHOTO_LIST);
        photoPosition = getIntent().getIntExtra(EXTRA_PHOTO_POSITION, 1);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_menu_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.photo_view_pager);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateTitle(position, photos.size());
                photoPageUri = photos.get(position).getPhotoPageUri();
                photoUri = photos.get(position).getPhotoUri();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Photo photo = photos.get(position);
                return PhotoPageFragment.newInstance(photo.getPhotoUri());
            }

            @Override
            public int getCount() {
                return photos.size();
            }

        });


        mViewPager.setCurrentItem(photoPosition);
        updateTitle(photoPosition, photos.size());
        photoPageUri = photos.get(photoPosition).getPhotoPageUri();
        photoUri = photos.get(photoPosition).getPhotoUri();
    }

    // Обновление загаловка панели
    public void updateTitle(int currentPosition, int photoCount) {
        String subtitle = getString(R.string.photo_title, currentPosition+1, photoCount);
        getSupportActionBar().setTitle(subtitle);
    }

    // Запуск браузера с ссылкой на изображение
    public void menuBrowser(Uri photoPageUri){
        Intent i = new Intent(Intent.ACTION_VIEW, photoPageUri);
        startActivity(i);
    }

    // Поделиться изображением через другое приложение
    public void menuShare(Uri photoUri){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, photoUri.toString());
        i = Intent.createChooser(i, getString(R.string.share_via));
        startActivity(i);
    }

}
