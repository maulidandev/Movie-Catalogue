package com.dicoding.picodiploma.moviecatalogueapi.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.api.Constants;
import com.dicoding.picodiploma.moviecatalogueapi.db.AppDatabase;
import com.dicoding.picodiploma.moviecatalogueapi.db.DatabaseContract;
import com.dicoding.picodiploma.moviecatalogueapi.model.Favorite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Bitmap> mWidgetItems;
    private final Context mContext;
    private ArrayList<Favorite> favorites;

    StackRemoteViewsFactory(Context context) {
        mContext = context;

        favorites = new ArrayList<>();
        mWidgetItems = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        AppDatabase db = Room.databaseBuilder(mContext, AppDatabase.class, DatabaseContract.FAVORITE_TABLE_NAME).allowMainThreadQueries().build();
        favorites = (ArrayList<Favorite>) db.favoriteDao().getAllMovie();

        mWidgetItems.clear();

        for (int i = 0; i< favorites.size(); i++){
            try {
                mWidgetItems.add(Glide.with(mContext)
                        .asBitmap()
                        .load(Constants.IMAGE_SOURCE + favorites.get(i).getPoster_path())
                        .submit()
                        .get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
        Bundle extras = new Bundle();
        extras.putInt(MovieFavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
