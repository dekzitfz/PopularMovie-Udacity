package id.dekz.popularmovies.features.detail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.google.gson.Gson;

import id.dekz.popularmovies.basemvp.BasePresenter;
import id.dekz.popularmovies.database.FavoriteContract;
import id.dekz.popularmovies.model.apiresponse.MovieItem;

/**
 * Created by DEKZ on 6/17/2017.
 */

public class DetailPresenter implements BasePresenter<DetailView> {

    DetailView view;
    private Gson gson = new Gson();

    @Override
    public void onAttach(DetailView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        view = null;
    }

    void getData(String json){
        view.onDataReceived(gson.fromJson(json, MovieItem.class));
    }

    MovieItem getMovie(String json){
        return gson.fromJson(json, MovieItem.class);
    }

    void saveAsFavorite(ContentResolver resolver, MovieItem item){
        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, item.getId());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, item.getOriginalTitle());
        resolver.insert(FavoriteContract.FavoriteEntry.CONTENT_URI, cv);
    }
}
