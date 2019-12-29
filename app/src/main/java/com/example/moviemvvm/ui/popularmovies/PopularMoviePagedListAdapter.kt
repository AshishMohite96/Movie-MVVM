package com.example.moviemvvm.ui.popularmovies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemvvm.R
import com.example.moviemvvm.data.api.POSTER_BASE_URL
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.vo.popularmovies.Result
import com.example.moviemvvm.ui.moviedetails.MovieDetailsActivity
import kotlinx.android.synthetic.main.layout_progressbar.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*

class PopularMoviePagedListAdapter(public val context: Context) : PagedListAdapter<Result, RecyclerView.ViewHolder>(MovieDiffCallback()){

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState : NetworkState? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view : View

        if (viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else{
            view = layoutInflater.inflate(R.layout.layout_progressbar, parent, false)
            return NetworkViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else{
            (holder as NetworkViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1)  NETWORK_VIEW_TYPE  else MOVIE_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() : Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }


    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(movie: Result?, context: Context){
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_release_date.text = movie?.releaseDate

            val moviePoster = POSTER_BASE_URL + movie?.posterPath

            Glide.with(itemView.context)
                .load(moviePoster)
                .into(itemView.cv_movie_poster)

            itemView.setOnClickListener{
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            if (networkState != null){
                when(networkState){
                    NetworkState.LOADING -> itemView.progress_bar.visibility = View.VISIBLE

                    NetworkState.ERROR -> {
                        itemView.txt_error.visibility = View.VISIBLE
                        itemView.txt_error.text = networkState.msg
                    }

                    NetworkState.ENDOFLIST->{
                        itemView.txt_error.visibility = View.VISIBLE
                        itemView.txt_error.text = networkState.msg
                    }

                    else -> {
                        itemView.progress_bar.visibility = View.GONE
                        itemView.txt_error.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun setNetworkState(networkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow : Boolean= hasExtraRow()
        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if (hasExtraRow != hadExtraRow){
            if (hadExtraRow)
                notifyItemRemoved(super.getItemCount()) // remove progress barr
            else
                notifyItemInserted(super.getItemCount()) // add progress barr
        } else if (hasExtraRow && previousState != networkState)
            notifyItemChanged(itemCount - 1) // for error
    }
}