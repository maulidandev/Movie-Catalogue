package com.dicoding.picodiploma.moviecatalogueapi.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.dicoding.picodiploma.moviecatalogueapi.db.AppDatabase;
import com.dicoding.picodiploma.moviecatalogueapi.db.DatabaseContract;
import com.dicoding.picodiploma.moviecatalogueapi.db.FavoriteDao;
import com.dicoding.picodiploma.moviecatalogueapi.model.Favorite;

import static com.dicoding.picodiploma.moviecatalogueapi.db.DatabaseContract.AUTHORITY;
import static com.dicoding.picodiploma.moviecatalogueapi.db.DatabaseContract.CONTENT_URI;
import static com.dicoding.picodiploma.moviecatalogueapi.db.DatabaseContract.FAVORITE_TABLE_NAME;

public class MovieFavoriteContentProvider extends ContentProvider {
    private AppDatabase appDatabase;
    private FavoriteDao favoriteDao;

    private static final int FAVORITE = 1;
    private static final int FAVORITE_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // content://com.dicoding.picodiploma.moviecatalogueapi/note
        sUriMatcher.addURI(AUTHORITY, FAVORITE_TABLE_NAME, FAVORITE);
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY,
                FAVORITE_TABLE_NAME + "/#",
                FAVORITE_ID);
    }

    @Override
    public boolean onCreate() {
        // Creates a new database object.
        appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, DatabaseContract.FAVORITE_TABLE_NAME).allowMainThreadQueries().build();

        // Gets a Data Access Object to perform the database operations
        favoriteDao = appDatabase.favoriteDao();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                cursor = favoriteDao.getAllMovieCursor();
                break;
            case FAVORITE_ID:
                cursor = favoriteDao.findByIdCursor(Integer.parseInt(uri.getLastPathSegment()));
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                added = favoriteDao.insert(Favorite.fromContentValues(values));
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                Favorite favorite = new Favorite();
                favorite.setId(Integer.parseInt(uri.getLastPathSegment()));

                deleted = favoriteDao.delete(favorite);
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                Favorite favorite = Favorite.fromContentValues(values);
                favorite.setId(Integer.parseInt(uri.getLastPathSegment()));

                updated = favoriteDao.update(favorite);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }
}
