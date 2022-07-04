package com.sametsisman.ornekproje1.view.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.adapter.MovieAdapter
import com.sametsisman.ornekproje1.view.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var viewModel : HomeViewModel
    private val movieAdapter = MovieAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.refreshData()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = movieAdapter

        swipeRefreshLayout.setOnRefreshListener {
            recyclerView.visibility = View.GONE
            errorText.visibility = View.GONE
            movieLoading.visibility = View.INVISIBLE
            viewModel.refreshFromAPI()

            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.movies.observe(viewLifecycleOwner, Observer {movies ->

            movies?.let {
                recyclerView.visibility = View.VISIBLE
                movieAdapter.updateMovieList(movies)
            }

        })

        viewModel.movieError.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                if(it) {
                    errorText.visibility = View.VISIBLE
                } else {
                    errorText.visibility = View.GONE
                }
            }
        })

        viewModel.movieLoading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it) {
                    movieLoading.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    errorText.visibility = View.GONE
                } else {
                    movieLoading.visibility = View.GONE
                }
            }
        })
    }

}