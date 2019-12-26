package com.example.moviemvvm.data.api

import com.example.moviemvvm.data.vo.moviedetails.MovieDetailsVO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TMDBAPIInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int) : Single<MovieDetailsVO>
}