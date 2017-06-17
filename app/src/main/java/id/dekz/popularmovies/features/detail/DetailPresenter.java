package id.dekz.popularmovies.features.detail;

import com.google.gson.Gson;

import id.dekz.popularmovies.basemvp.BasePresenter;
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
}
