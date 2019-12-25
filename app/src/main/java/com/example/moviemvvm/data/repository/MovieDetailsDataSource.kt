package com.example.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviemvvm.data.api.TMDBAPIInterface
import com.example.moviemvvm.data.vo.moviedetails.MovieDetailsVO
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsDataSource (private val apiService: TMDBAPIInterface, private val compositeDisposable: CompositeDisposable){

    private val _networkState = MutableLiveData<NetworkState>()

    val newtworkState: LiveData<NetworkState>
        get() = _networkState //with this get, no need to implement get function to get networkstate


    private val _movieDetailsResponse = MutableLiveData<MovieDetailsVO>()

    val movieDetailsResponse: LiveData<MovieDetailsVO>
        get() = _movieDetailsResponse //with this get, no need to implement get function to get networkstate

    fun fetchMovieDetails(movieId: Int){

        _networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe (
                    {
                        _movieDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDetailsDataSource", it.message)
                    }
                )
        )
    }
}