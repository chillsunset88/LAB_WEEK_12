package com.example.test_lab_week_12

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_12.model.Movie
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    // Use the by viewModels() delegate to get the ViewModel
    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MovieApplication).movieRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.adapter = movieAdapter

        // Observe the LiveData from the ViewModel
//        movieViewModel.popularMovies.observe(this) { popularMovies ->
//            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
//            movieAdapter.addMovies(
//                popularMovies
//                    .filter { movie ->
//                        // Safe from null
//                        movie.releaseDate?.startsWith(currentYear) == true
//                    }
//                    .sortedByDescending { it.popularity }
//            )
//        }
//        movieViewModel.error.observe(this) { error ->
//            if (error.isNotEmpty()) {
//                Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show()
//            }
//        }

        lifecycleScope.launch {
// repeatOnLifecycle is a lifecycle-aware coroutine builder
// Lifecycle.State.STARTED means that the coroutine will run
// when the activity is started
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
// collect the list of movies from the StateFlow
                    movieViewModel.popularMovies.collect {
// add the list of movies to the adapter
                            movies ->
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
                        movieAdapter.addMovies(
                            movies
                                .filter { movie ->
                                    movie.releaseDate?.startsWith(currentYear) == true
                                }
                                .sortedByDescending { it.popularity }
                        )
                    }
                }
                launch {
// collect the error message from the StateFlow
                    movieViewModel.error.collect { error ->
// if an error occurs, show a Snackbar with the error message
                        if (error.isNotEmpty()) Snackbar
                            .make(
                                recyclerView, error, Snackbar.LENGTH_LONG
                            ).show()
                    }
                }
            }
        }
    }

    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}