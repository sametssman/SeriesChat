package com.sametsisman.ornekproje1.view.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sametsisman.ornekproje1.view.model.Moviex
import com.sametsisman.ornekproje1.view.service.MovieAPIService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val apiService = MovieAPIService()
    private val compositeDisposable = CompositeDisposable()

    val movies = MutableLiveData<Moviex>()
    val totalPages = MutableLiveData<Int>()
    val movieError = MutableLiveData<Boolean>()
    val movieLoading = MutableLiveData<Boolean>()

    fun getDataFromAPI(page: String,first : Boolean){

        if (first){
            movieLoading.postValue(true)
        }

        try {
            compositeDisposable.add(
                apiService.getPopularMovies(page)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            movies.postValue(it)
                            totalPages.postValue(it.total_pages)
                            movieLoading.postValue(false)
                            movieError.postValue(false)
                        },
                        {
                            Log.e("MovieDetailsDataSource",it.message!!)
                            movieLoading.postValue(false)
                            movieError.postValue(true)
                        }
                    )
            )
        }catch (e: Exception){
            Log.e("MovieDetailsDataSource",e.message!!)
            movieLoading.postValue(false)
            movieError.postValue(true)
        }
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}