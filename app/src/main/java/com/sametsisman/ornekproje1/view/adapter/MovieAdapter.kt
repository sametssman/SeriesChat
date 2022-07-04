package com.sametsisman.ornekproje1.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.databinding.RecyclerviewRowBinding
import com.sametsisman.ornekproje1.view.model.Movie
import com.sametsisman.ornekproje1.view.util.downloadFromUrl
import com.sametsisman.ornekproje1.view.util.placeholderProgressBar

class MovieAdapter(val movieArrayList: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    class MovieHolder(var view : RecyclerviewRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.view.rowTitleText.text = movieArrayList[position].title
        holder.view.rowScoreText.text = movieArrayList[position].score
        holder.view.rowImage.downloadFromUrl(movieArrayList[position].imageUrl,
            placeholderProgressBar(holder.view.root.context))
    }

    fun updateMovieList(newMovieList : List<Movie>){
        movieArrayList.clear()
        movieArrayList.addAll(newMovieList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return movieArrayList.size
    }

}