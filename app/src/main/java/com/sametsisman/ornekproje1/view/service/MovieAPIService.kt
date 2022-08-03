package com.sametsisman.ornekproje1.view.service

import com.sametsisman.ornekproje1.view.model.MovieDetails
import com.sametsisman.ornekproje1.view.model.Moviex
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

class MovieAPIService {
    private val BASE_URL = "https://api.themoviedb.org/"

    private val api =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieAPI::class.java)

    fun getPopularMovies(page : String) : Single<Moviex> {
        return api.getPopularMovie(page)
    }

    fun getMovieDetails(movieId : Int) : Single<MovieDetails> {
        return api.getMovieDetails(movieId)
    }

    fun searchMovie(query: String , page : String) : Single<Moviex>{
        return api.searchMovie(query, page)
    }
}