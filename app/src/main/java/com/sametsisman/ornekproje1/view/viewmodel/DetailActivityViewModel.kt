package com.sametsisman.ornekproje1.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sametsisman.ornekproje1.view.model.MovieDetails
import com.sametsisman.ornekproje1.view.service.MovieAPIService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailActivityViewModel : ViewModel() {
    private val apiService = MovieAPIService()
    private val compositeDisposable = CompositeDisposable()

    val movieDetail = MutableLiveData<MovieDetails>()
    val movieError = MutableLiveData<Boolean>()
    val movieLoading = MutableLiveData<Boolean>()

    fun getDataFromAPI(movieId: Int){

        movieLoading.postValue(true)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            movieDetail.postValue(it)
                            movieLoading.postValue(false)
                            movieError.postValue(false)
                        },
                        {
                            Log.e("DetailActivityViewModel",it.message!!)
                            movieLoading.postValue(false)
                            movieError.postValue(true)
                        }
                    )
            )
        }catch (e: Exception){
            Log.e("DetailActivityViewModel",e.message!!)
            movieLoading.postValue(false)
            movieError.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}