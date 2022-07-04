package com.sametsisman.ornekproje1.view.service

import com.sametsisman.ornekproje1.view.model.Movie
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieAPIService {
    private val Base_Url = "https://api.themoviedb.org/3/"

    private val api =
        Retrofit.Builder()
            .baseUrl(Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieAPI::class.java)

    fun getMovies() : Single<List<Movie>> {
        return api.getMovies()
    }
}