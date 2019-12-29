package com.example.moviemvvm.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviemvvm.data.api.TMDBAPIInterface
import com.example.moviemvvm.data.vo.popularmovies.Result
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService: TMDBAPIInterface, private val compositeDisposable: CompositeDisposable): DataSource.Factory<Int, Result>(){

    val movieLiveDataSource = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, Result> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        movieLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}