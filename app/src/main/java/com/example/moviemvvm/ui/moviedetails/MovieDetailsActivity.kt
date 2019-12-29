package com.example.moviemvvm.ui.moviedetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviemvvm.R
import com.example.moviemvvm.data.api.POSTER_BASE_URL
import com.example.moviemvvm.data.api.TMDBAPIInterface
import com.example.moviemvvm.data.api.TMDBMovieClient
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.vo.moviedetails.MovieDetailsVO
import com.example.moviemvvm.details.MovieDetailsRepository
import com.example.moviemvvm.details.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.layout_progressbar.*
import java.text.NumberFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movieInt = intent.getIntExtra("id", 1)

        val apiService : TMDBAPIInterface = TMDBMovieClient.getClient()
        movieDetailsRepository =
            MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieInt)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.newtworkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it: MovieDetailsVO) {
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.voteAverage.toString()
        movie_runtime.text = it.runtime.toString() + "minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePoster = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePoster)
            .into(movie_poster)
    }

    private fun getViewModel(movieId: Int): MovieDetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailsViewModel(
                    movieDetailsRepository,
                    movieId
                ) as T
            }
        })[MovieDetailsViewModel::class.java]
    }
}
