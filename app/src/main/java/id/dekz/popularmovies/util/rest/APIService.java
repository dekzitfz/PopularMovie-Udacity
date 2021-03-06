package id.dekz.popularmovies.util.rest;

import id.dekz.popularmovies.model.apiresponse.MovieResponse;
import id.dekz.popularmovies.model.apiresponse.ReviewResponse;
import id.dekz.popularmovies.model.apiresponse.TrailerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DEKZ on 6/16/2017.
 */

public interface APIService {

    @GET("movie/popular?")
    Call<MovieResponse> getPopularMovie(@Query("page") int page);

    @GET("movie/top_rated?")
    Call<MovieResponse> getHighRatedMovie(@Query("page") int page);

    @GET("movie/{id}/videos?")
    Call<TrailerResponse> getTrailers(@Path("id") long id);

    @GET("movie/{id}/reviews?")
    Call<ReviewResponse> getReviews(@Path("id") long id);
}
