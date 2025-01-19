package com.omdbapi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omdbapi.R
import com.omdbapi.databinding.FragmentMovieDetailBinding
import com.omdbapi.di.DaggerAppComponent
import com.omdbapi.presentation.MovieViewModel
import com.omdbapi.presentation.MovieViewModelFactory

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        val appComponent = DaggerAppComponent.create()
        val repository = appComponent.movieRepository()
        viewModel = ViewModelProvider(
            this,
            MovieViewModelFactory(repository)
        )[MovieViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.let { MovieDetailFragmentArgs.fromBundle(it).movieId }

        viewModel.movieDetails.observe(viewLifecycleOwner) { movie ->
            binding.title.text = movie.title
            binding.plot.text = movie.plot
        }

        movieId?.let { viewModel.getMovieDetails("431d51d7", it) }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
