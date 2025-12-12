package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.MovieRepository
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData


class MovieViewModel(private val movieRepository: MovieRepository)
    : ViewModel() {
    init {
        fetchPopularMovies()
    }
    // define the LiveData
    val popularMovies: LiveData<List<Movie>>
        get() = movieRepository.movies
    val error: LiveData<String>
        get() = movieRepository.error
    // fetch movies from the API
    private fun fetchPopularMovies() {
// launch a coroutine in viewModelScope
// Dispatchers.IO means that this coroutine will run on a shared       pool of threads
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
        }
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}