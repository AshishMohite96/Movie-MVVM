package com.example.moviemvvm.data.api

import com.example.moviemvvm.data.vo.moviedetails.MovieDetailsVO
import com.example.moviemvvm.data.vo.popularmovies.PopularMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBAPIInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int) : Single<MovieDetailsVO>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Single<PopularMoviesResponse>
}