package id.dekz.popularmovies.features.main;

import id.dekz.popularmovies.basemvp.BasePresenter;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class MainPresenter implements BasePresenter<MainView> {

    private MainView view;

    @Override
    public void onAttach(MainView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
