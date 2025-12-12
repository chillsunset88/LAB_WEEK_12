package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn



class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "16ff13c9dc377e0866b25ca536647b59"
    // LiveData that contains a list of movies
    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData
    // LiveData that contains an error message
    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
// emit the list of popular movies from the API
            emit(movieService.getPopularMovies(apiKey).results)
// use Dispatchers.IO to run this coroutine on a shared pool of threads
        }.flowOn(Dispatchers.IO)
    }
}
