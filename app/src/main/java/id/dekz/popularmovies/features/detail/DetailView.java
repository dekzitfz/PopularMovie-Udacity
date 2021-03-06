package id.dekz.popularmovies.features.detail;

import java.util.List;

import id.dekz.popularmovies.basemvp.BaseView;
import id.dekz.popularmovies.model.apiresponse.MovieItem;
import id.dekz.popularmovies.model.apiresponse.ReviewItem;
import id.dekz.popularmovies.model.apiresponse.TrailerItem;

/**
 * Created by DEKZ on 6/17/2017.
 */

public interface DetailView extends BaseView {
    void onDataReceived(MovieItem movie);
    void onFailedReceiveData();
    void onStatusReceived(boolean isFavorite);
    void onRefreshLoader();
    void onTrailerDataReceived(List<TrailerItem> data);
    void onReviewDataReceived(List<ReviewItem> data);
}
