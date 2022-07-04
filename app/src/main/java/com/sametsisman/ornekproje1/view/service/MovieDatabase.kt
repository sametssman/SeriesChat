package com.sametsisman.ornekproje1.view.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sametsisman.ornekproje1.view.model.Movie

@Database(entities = arrayOf(Movie::class),version = 1)
abstract class MovieDatabase : RoomDatabase(){
    abstract fun movieDao() : MovieDao

    companion object {

        @Volatile private var instance : MovieDatabase? = null

        private val lock = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }


        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,MovieDatabase::class.java,"moviedatabase"
        ).build()
    }
}