package com.hilbing.mymovies.apiConnection;

import com.hilbing.mymovies.model.MovieResults;
import com.hilbing.mymovies.model.Review;
import com.hilbing.mymovies.model.ReviewResults;
import com.hilbing.mymovies.model.Trailer;
import com.hilbing.mymovies.model.TrailerResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBInterface {

    @GET("/3/movie/{category}")
    Call<MovieResults> getMovies(
            @Path("category") String category,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page);

    @GET("3/movie/{id}/videos")
    Call<TrailerResults> getMovieTrailers(
            @Path("id") int id,
            @Query("api_key") String api_key);

    @GET("3/movie/{id}/reviews")
    Call<ReviewResults> getMovieReviews(
            @Path("id") int id,
            @Query("api_key") String api_key);

}
