package com.sametsisman.ornekproje1.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.feed.DetailActivity
import com.sametsisman.ornekproje1.view.model.Result
import kotlinx.android.synthetic.main.search_row.view.*

class SearchAdapter () : RecyclerView.Adapter<SearchAdapter.MovieHolder>() {
    var movieList : ArrayList<Result> = ArrayList()
    val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

    class MovieHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.search_row,parent,false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.view.search_row_title.text = movieList[position].name
        holder.view.search_row_score.text = movieList[position].rating.toString()

        val moviePosterUrl = POSTER_BASE_URL + movieList[position].posterPath
        Glide.with(holder.itemView.context)
            .load(moviePosterUrl)
            .centerCrop()
            .into(holder.itemView.search_row_image)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.view.context, DetailActivity::class.java)
            intent.putExtra("id",movieList[position].id)
            holder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setList(arraylist : List<Result>){
        movieList.addAll(arraylist)
    }
}