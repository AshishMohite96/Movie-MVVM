package com.example.moviemvvm.details

import androidx.lifecycle.LiveData
import com.example.moviemvvm.data.api.TMDBAPIInterface
import com.example.moviemvvm.data.repository.MovieDetailsDataSource
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.vo.moviedetails.MovieDetailsVO
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService: TMDBAPIInterface){

    lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetailsVO>{

        movieDetailsDataSource = MovieDetailsDataSource(apiService, compositeDisposable)
        movieDetailsDataSource.fetchMovieDetails(movieId)

        return movieDetailsDataSource.movieDetailsResponse
    }

    fun getDetailNetworkState() : LiveData<NetworkState>{
        return movieDetailsDataSource.newtworkState
    }
}