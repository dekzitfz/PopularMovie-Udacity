package id.dekz.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import id.dekz.popularmovies.database.FavoriteContract.*;

/**
 * Created by DEKZ on 6/18/2017.
 */

public class FavoriteContentProvider extends ContentProvider {

    private FavoriteDBHelper dbHelper;

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteDBHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri result = null;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        switch (match){
            case FAVORITES:
                long id = db.insert(FavoriteEntry.TABLE_NAME, null, values);
                if(id > 0){
                    result = ContentUris.withAppendedId(FavoriteEntry.CONTENT_URI, id);

                    //noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);
                }else{
                    throw new SQLException("Insert data failed to "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: "+uri);
        }


        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITES, FAVORITES);
        matcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);

        return matcher;
    }
}
