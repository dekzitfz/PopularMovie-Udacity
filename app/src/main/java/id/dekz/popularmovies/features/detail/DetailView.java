package id.dekz.popularmovies.features.detail;

import id.dekz.popularmovies.basemvp.BaseView;
import id.dekz.popularmovies.model.apiresponse.MovieItem;

/**
 * Created by DEKZ on 6/17/2017.
 */

public interface DetailView extends BaseView {
    void onDataReceived(MovieItem movie);
    void onFailedReceiveData();
    void onStatusReceived(boolean isFavorite);
}
