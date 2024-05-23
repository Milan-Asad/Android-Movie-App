package com.example.movieapplication.model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    suspend fun getMovies(@Query("api_key") apiKey: String): Response<MyData>
}