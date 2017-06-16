package id.dekz.popularmovies.features.highrated;

import id.dekz.popularmovies.basemvp.BasePresenter;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class HighRatedPresenter implements BasePresenter<HighRatedView> {

    HighRatedView view;

    @Override
    public void onAttach(HighRatedView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
