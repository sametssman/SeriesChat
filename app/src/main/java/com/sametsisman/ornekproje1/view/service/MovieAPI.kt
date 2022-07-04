package com.sametsisman.ornekproje1.view.service

import com.sametsisman.ornekproje1.view.model.Movie
import io.reactivex.Single
import retrofit2.http.GET

interface MovieAPI {
    @GET("movie/popular?api_key=56bf530ad2171a0459e13f5ebe3043ca")
    fun getMovies() : Single<List<Movie>>
}