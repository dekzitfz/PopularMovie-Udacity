package id.dekz.popularmovies.features.mostpopular;

import id.dekz.popularmovies.basemvp.BasePresenter;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class MostPopularPresenter implements BasePresenter<MostPopularView> {

    MostPopularView view;

    @Override
    public void onAttach(MostPopularView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
