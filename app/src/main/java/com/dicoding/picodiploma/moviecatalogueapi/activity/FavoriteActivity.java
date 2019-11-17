package com.dicoding.picodiploma.moviecatalogueapi.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.adapter.SectionsFavoritePagerAdapter;
import com.dicoding.picodiploma.moviecatalogueapi.fragment.MovieFavoriteFragment;
import com.dicoding.picodiploma.moviecatalogueapi.fragment.TVShowFavoriteFragment;
import com.google.android.material.tabs.TabLayout;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        setTitle(getResources().getString(R.string.favorite));

        SectionsFavoritePagerAdapter sectionsFavoritePagerAdapter = new SectionsFavoritePagerAdapter(this, getSupportFragmentManager());
        sectionsFavoritePagerAdapter.addFragment(new MovieFavoriteFragment());
        sectionsFavoritePagerAdapter.addFragment(new TVShowFavoriteFragment());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsFavoritePagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}