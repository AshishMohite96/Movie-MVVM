package com.example.moviemvvm.ui.popularmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemvvm.R
import com.example.moviemvvm.data.api.TMDBAPIInterface
import com.example.moviemvvm.data.api.TMDBMovieClient
import com.example.moviemvvm.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_progressbar.*

class PopularMoviesActivity : AppCompatActivity() {

    private lateinit var viewModel: PopularMoviesViewModel
    lateinit var movieRepository: MoviePageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService : TMDBAPIInterface = TMDBMovieClient.getClient()

        movieRepository = MoviePageListRepository(apiService)

        viewModel = getViewModel()

        val moviePageAdapter = PopularMoviePagedListAdapter(this)

        val gridLayoutManager : GridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType : Int = moviePageAdapter.getItemViewType(position)

                return if (viewType == moviePageAdapter.MOVIE_VIEW_TYPE)
                    1 // movie type will occupy 1 out of 3 spans
                else
                    3 // network type will occupy all 3 spans
            }
        }

        popular_movies_rv.layoutManager = gridLayoutManager
        popular_movies_rv.setHasFixedSize(true)
        popular_movies_rv.adapter = moviePageAdapter

        viewModel.moviePagedList.observe(this, Observer{
            moviePageAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty())
                moviePageAdapter.setNetworkState(it)
        })
    }

    private fun getViewModel(): PopularMoviesViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PopularMoviesViewModel(movieRepository) as T
            }
        })[PopularMoviesViewModel::class.java]
    }
}
