package com.sametsisman.ornekproje1.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sametsisman.ornekproje1.view.model.Moviex
import com.sametsisman.ornekproje1.view.service.MovieAPIService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel : ViewModel() {
    private val apiService = MovieAPIService()
    private val compositeDisposable = CompositeDisposable()

    val searchMovies = MutableLiveData<Moviex>()
    val totalPages = MutableLiveData<Int>()
    val movieError = MutableLiveData<Boolean>()
    val movieLoading = MutableLiveData<Boolean>()

    fun searchMovieFromApi(query : String , page : String){

        movieLoading.postValue(true)

        try {
            compositeDisposable.add(
                apiService.searchMovie(query, page)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            searchMovies.postValue(it)
                            totalPages.postValue(it.total_pages)
                            movieLoading.postValue(false)
                            movieError.postValue(false)
                        },
                        {
                            Log.e("SearchViewModel",it.message!!)
                            movieLoading.postValue(false)
                            movieError.postValue(true)
                        }
                    )
            )
        }catch (e: Exception){
            Log.e("SearchViewModel",e.message!!)
            movieLoading.postValue(false)
            movieError.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}