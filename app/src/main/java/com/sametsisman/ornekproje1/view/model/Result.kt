package com.sametsisman.ornekproje1.view.model

import com.google.gson.annotations.SerializedName

data class Result(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val rating: Double,
    val name: String,
)