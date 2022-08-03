package com.sametsisman.ornekproje1.view.service

import com.sametsisman.ornekproje1.view.model.MovieDetails
import com.sametsisman.ornekproje1.view.model.Moviex
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    //https://api.themoviedb.org/3/movie/453395?api_key=56bf530ad2171a0459e13f5ebe3043ca
    //https://api.themoviedb.org/3/movie/popular?api_key=56bf530ad2171a0459e13f5ebe3043ca

    @GET("3/tv/popular?api_key=56bf530ad2171a0459e13f5ebe3043ca")
    fun getPopularMovie(@Query("page") page : String) : Single<Moviex>

    @GET("3/tv/{tv_id}?api_key=56bf530ad2171a0459e13f5ebe3043ca")
    fun getMovieDetails(@Path("tv_id") id : Int) : Single<MovieDetails>

    @GET("3/search/tv?api_key=56bf530ad2171a0459e13f5ebe3043ca")
    fun searchMovie(@Query("query") query : String,@Query("page") page : String) : Single<Moviex>
}