package id.dekz.popularmovies.util.rest;

import id.dekz.popularmovies.model.apiresponse.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DEKZ on 6/16/2017.
 */

public interface APIService {

    @GET("movie/popular?")
    Call<MovieResponse> getPopularMovie(@Query("page") int page);

    @GET("movie/top_rated?")
    Call<MovieResponse> getHighRatedMovie(@Query("page") int page);
}
