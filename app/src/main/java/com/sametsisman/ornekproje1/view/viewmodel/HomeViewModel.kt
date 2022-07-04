package com.sametsisman.ornekproje1.view.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.sametsisman.ornekproje1.view.model.Movie
import com.sametsisman.ornekproje1.view.service.MovieAPIService
import com.sametsisman.ornekproje1.view.service.MovieDatabase
import com.sametsisman.ornekproje1.view.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val movieAPIService = MovieAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val movies = MutableLiveData<List<Movie>>()
    val movieError = MutableLiveData<Boolean>()
    val movieLoading = MutableLiveData<Boolean>()

    fun refreshData() {
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPI()
        }
    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        movieLoading.value = true
        launch {
            val movies = MovieDatabase(getApplication()).movieDao().getAllMovies()
            showMovieList(movies)
            Toast.makeText(getApplication(),"Movies From SQLite",Toast.LENGTH_LONG).show()
        }
    }

    fun getDataFromAPI() {
        movieLoading.value = true

        disposable.add(
            movieAPIService.getMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>(){
                    override fun onSuccess(t: List<Movie>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Movies From API",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        movieLoading.value = false
                        movieError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun showMovieList(countryList : List<Movie>){
        movies.value = countryList
        movieLoading.value = false
        movieError.value = false
    }

    private fun storeInSQLite(movieList: List<Movie>){
        launch {
            val dao = MovieDatabase(getApplication()).movieDao()
            dao.deleteAllMovies()
            val listLong = dao.insertAll(*movieList.toTypedArray())
            var i = 0
            while (i < listLong.size){
                movieList[i].uuid = listLong[i].toInt()
                i++
            }
            showMovieList(movieList)
        }

        customPreferences.saveTime(System.nanoTime())
    }
}