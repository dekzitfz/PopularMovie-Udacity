package id.dekz.popularmovies.features.mostpopular;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import id.dekz.popularmovies.App;
import id.dekz.popularmovies.basemvp.BasePresenter;
import id.dekz.popularmovies.model.apiresponse.mostpopular.MostPopularResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class MostPopularPresenter implements BasePresenter<MostPopularView> {

    MostPopularView view;
    private Call<MostPopularResponse> getMostPopular;
    private int currentPage = 1;

    @Override
    public void onAttach(MostPopularView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        if(getMostPopular!=null) getMostPopular.cancel();
        view = null;
    }

    void checkWhenScrolled(GridLayoutManager layoutManager, int dy){
        if(dy > 0){
            final int visibleThreshold = 2;
            int lastItem  = layoutManager.findLastCompletelyVisibleItemPosition();
            int currentTotalCount = layoutManager.getItemCount();

            if(currentTotalCount <= lastItem + visibleThreshold){
                Log.d("scroll", "bottom!");
                currentPage++;
                loadData();
            }
        }
    }

    void loadData(){
        getMostPopular = App.getRestClient()
                .getService()
                .getPopularMovie(currentPage);

        getMostPopular.enqueue(new Callback<MostPopularResponse>() {
            @Override
            public void onResponse(@NonNull Call<MostPopularResponse> call, @NonNull Response<MostPopularResponse> response) {
                if(response.isSuccessful()){
                    view.onDataReceived(response.body().getResults(), currentPage);
                }else{
                    Log.d("code ", ""+response.code());
                    //failed
                }
            }

            @Override
            public void onFailure(Call<MostPopularResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
