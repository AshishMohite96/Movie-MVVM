package com.example.moviemvvm.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.vo.moviedetails.MovieDetailsVO
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(private val movieDetailsRepository: MovieDetailsRepository, movieId: Int) :ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetailsVO> by lazy {
        movieDetailsRepository.fetchMovieDetails(compositeDisposable, movieId)
    }

    val newtworkState : LiveData<NetworkState> by lazy {
        movieDetailsRepository.getDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}