package com.sametsisman.ornekproje1.view.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    @SerializedName("number_of_seasons")
    val budget: Int,
    val id: Int,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("first_air_date")
    val releaseDate: String,
    @SerializedName("number_of_episodes")
    val revenue: Int,
    @SerializedName("vote_average")
    val rating: Double,
    val tagline: String,
    @SerializedName("name")
    val title: String
)