package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie


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

    suspend fun fetchMovies() {
        try {
            // get the list of popular movies from the API
            val popularMovies = movieService.getPopularMovies(apiKey)
            // FIX: Use the 'popularMovies' variable you created above.
            movieLiveData.postValue(popularMovies.results)
        } catch (exception: Exception) {
            // if an error occurs, post the error message to the errorLiveData
            errorLiveData.postValue(
                "An error occurred: ${exception.message}")
        }
    }
}
