package id.dekz.popularmovies.features.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.dekz.popularmovies.App;
import id.dekz.popularmovies.Constant;
import id.dekz.popularmovies.basemvp.BasePresenter;
import id.dekz.popularmovies.database.FavoriteContract;
import id.dekz.popularmovies.model.apiresponse.MovieItem;
import id.dekz.popularmovies.model.apiresponse.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class MainPresenter implements BasePresenter<MainView> {

    private MainView view;
    private Call<MovieResponse> responseCall;
    private int currentPage = 1;
    private String categorySelected;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private Cursor favoriteData = null;

    @Override
    public void onAttach(MainView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        if(responseCall!=null) responseCall.cancel();
        view = null;
    }

    void setCategory(String category){
        categorySelected = category;
    }

    void resetPage(){
        currentPage = 1;
    }

    void loadData(String category){
        categorySelected = category;

        if(category.equals(Constant.CATEGORY_MOST_POPULAR)){
            view.onLoadingData();
            responseCall = App.getRestClient()
                    .getService()
                    .getPopularMovie(currentPage);
        }else if(category.equals(Constant.CATEGORY_HIGH_RATED)){
            view.onLoadingData();
            responseCall = App.getRestClient()
                    .getService()
                    .getHighRatedMovie(currentPage);
        }

        if(responseCall != null){
            responseCall.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                    if(response.isSuccessful()){
                        view.onDataReceived(response.body().getResults(), currentPage);
                    }else{
                        view.onFailedReceivedData();
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    view.onFailedReceivedData();
                }
            });
        }
    }

    void setupLoader(final Context context, final ContentResolver contentResolver){
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Cursor>(context) {
                    @Override
                    public Cursor loadInBackground() {
                        try {
                            return contentResolver.query(
                                    FavoriteContract.FavoriteEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null
                            );
                        }catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onStartLoading() {
                        if(categorySelected.equals(Constant.CATEGORY_FAVORITES)) forceLoad();
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.d("favorites found ", ""+data.getCount());
                favoriteData = data;
                generateFromCursor(favoriteData);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }

    void initLoader(LoaderManager loaderManager){
        loaderManager.initLoader(Constant.LOADER_MAIN_ID, null, loaderCallbacks);
    }

    void restartLoader(LoaderManager loaderManager){
        loaderManager.restartLoader(Constant.LOADER_MAIN_ID, null, loaderCallbacks);
    }

    private void generateFromCursor(Cursor cursor){
        List<MovieItem> movies = new ArrayList<>();
        cursor.moveToPosition(-1);
        try {
            while (cursor.moveToNext()) {
                MovieItem movieItem = new MovieItem();
                movieItem.setPosterPath(
                        cursor.getString(
                                cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER)
                        )
                );
                movieItem.setOriginalTitle(
                        cursor.getString(
                                cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)
                        )
                );
                movieItem.setBackdropPath(
                        cursor.getString(
                                cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP)
                        )
                );
                movieItem.setReleaseDate(
                        cursor.getString(
                                cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE)
                        )
                );
                movieItem.setOverview(
                        cursor.getString(
                                cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_SYNOPSIS)
                        )
                );
                movieItem.setVoteAverage(
                        cursor.getDouble(
                                cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RATING)
                        )
                );
                movieItem.setId(
                        cursor.getLong(
                                cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID)
                        )
                );

                movies.add(movieItem);
            }
        } finally {
            cursor.close();
        }

        view.onDataReceived(movies, 1);
    }
}
