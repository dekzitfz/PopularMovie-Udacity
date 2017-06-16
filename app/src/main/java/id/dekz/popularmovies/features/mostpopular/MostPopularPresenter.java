package id.dekz.popularmovies.features.mostpopular;

import android.support.annotation.NonNull;
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

    @Override
    public void onAttach(MostPopularView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        if(getMostPopular!=null) getMostPopular.cancel();
        view = null;
    }

    void loadData(){
        getMostPopular = App.getRestClient()
                .getService()
                .getPopularMovie(1);

        getMostPopular.enqueue(new Callback<MostPopularResponse>() {
            @Override
            public void onResponse(@NonNull Call<MostPopularResponse> call, @NonNull Response<MostPopularResponse> response) {
                if(response.isSuccessful()){
                    view.onDataReceived(response.body().getResults());
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
