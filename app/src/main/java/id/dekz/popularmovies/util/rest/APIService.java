package id.dekz.popularmovies.util.rest;

import id.dekz.popularmovies.model.apiresponse.mostpopular.MostPopularResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DEKZ on 6/16/2017.
 */

public interface APIService {

    @GET("movie/popular?")
    Call<MostPopularResponse> getPopularMovie(@Query("page") int page);

}
