package com.example.moviemvvm.ui.popularmovies

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviemvvm.data.api.POST_PER_PAGE
import com.example.moviemvvm.data.api.TMDBAPIInterface
import com.example.moviemvvm.data.repository.MovieDataSource
import com.example.moviemvvm.data.repository.MovieDataSourceFactory
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.vo.popularmovies.Result
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val apiService: TMDBAPIInterface) {

    lateinit var moviePageList: LiveData<PagedList<Result>>
    lateinit var moviesDaFactory: MovieDataSourceFactory

    fun fetchMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Result>>{
        moviesDaFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(moviesDaFactory, config).build();

        return moviePageList
    }

    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(moviesDaFactory.movieLiveDataSource, MovieDataSource::networkState)
    }
}