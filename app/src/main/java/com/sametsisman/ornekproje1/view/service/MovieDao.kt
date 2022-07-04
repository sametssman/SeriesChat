package com.sametsisman.ornekproje1.view.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sametsisman.ornekproje1.view.model.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insertAll(vararg movies : Movie) : List<Long>

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies() : List<Movie>

    @Query("SELECT * FROM movie WHERE uuid = :movieId")
    suspend fun getMovie(movieId : Int) : Movie

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()
}