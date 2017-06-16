package id.dekz.popularmovies.basemvp;

/**
 * Created by DEKZ on 6/16/2017.
 */

public interface BasePresenter<T extends BaseView> {
    void onAttach(T BaseView);
    void onDetach();
}
