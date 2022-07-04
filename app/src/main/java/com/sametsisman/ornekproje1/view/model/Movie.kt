package com.sametsisman.ornekproje1.view.model

import android.content.res.Resources
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Movie(

    @ColumnInfo(name = "movieId")
    @SerializedName("id")
    val movieId : String?,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title : String?,

    @ColumnInfo(name = "genres")
    @SerializedName("genre_ids")
    val genres : String?,

    @ColumnInfo(name = "language")
    @SerializedName("original_language")
    val language : String?,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    val overview : String?,

    @ColumnInfo(name = "imageUrl")
    @SerializedName("poster_path")
    val imageUrl : String?,

    @ColumnInfo(name = "releaseDate")
    @SerializedName("release_date")
    val releaseDate : String?,

    @ColumnInfo(name = "score")
    @SerializedName("vote_average")
    val score : String?
) {
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0
}