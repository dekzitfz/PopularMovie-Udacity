package id.dekz.popularmovies.features.mostpopular;

import java.util.List;

import id.dekz.popularmovies.basemvp.BaseView;
import id.dekz.popularmovies.model.apiresponse.mostpopular.ResultsItem;

/**
 * Created by DEKZ on 6/16/2017.
 */

public interface MostPopularView extends BaseView {
    void onDataReceived(List<ResultsItem> data);
    void onLoadingData();
    void onFailedReceivedData();
    void showMessage(String msg);
}
