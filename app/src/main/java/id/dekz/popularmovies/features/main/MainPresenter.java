package id.dekz.popularmovies.features.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;

import id.dekz.popularmovies.App;
import id.dekz.popularmovies.Constant;
import id.dekz.popularmovies.basemvp.BasePresenter;
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

    @Override
    public void onAttach(MainView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        if(responseCall!=null) responseCall.cancel();
        view = null;
    }

    void resetPage(){
        currentPage = 1;
    }

    void checkWhenScrolled(GridLayoutManager layoutManager, int dy){
        if(dy > 0){
            final int visibleThreshold = 2;
            int lastItem  = layoutManager.findLastCompletelyVisibleItemPosition();
            int currentTotalCount = layoutManager.getItemCount();

            if(currentTotalCount <= lastItem + visibleThreshold){
                currentPage++;
                loadData(categorySelected);
            }
        }
    }

    void loadData(String category){
        categorySelected = category;
        view.onLoadingData();

        if(category.equals(Constant.CATEGORY_MOST_POPULAR)){
            responseCall = App.getRestClient()
                    .getService()
                    .getPopularMovie(currentPage);
        }else{
            responseCall = App.getRestClient()
                    .getService()
                    .getHighRatedMovie(currentPage);
        }

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
