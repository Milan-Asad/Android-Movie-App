package com.example.movieapplication.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.model.MyData
import com.example.movieapplication.model.Result
import com.example.movieapplication.model.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MovieViewModel : ViewModel() {

    // MOVIE VAL
    private val _movies = MutableLiveData<List<Result>>()
    val movies: LiveData<List<Result>> = _movies

    // LOADING VAL
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // FETCH MOVIE FUNCTION
    fun fetchMovies(apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true // LOADING

            // FETCHING MOVIES  (IF STATEMENTS)
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getMovies(apiKey)
                }
                if (response.isSuccessful) {
                    _movies.postValue(response.body()?.results)
                } else {
                    Log.e("MovieViewModel", "Response was not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Exception: ${e.message}", e)
            } finally {
                _isLoading.value = false // WHEN COMPLETED
            }
        }
    }

    // GETS MOVIE ID
    fun getMovieById(movieId: Int): Result? {
        return _movies.value?.find { it.id == movieId }
    }
}

