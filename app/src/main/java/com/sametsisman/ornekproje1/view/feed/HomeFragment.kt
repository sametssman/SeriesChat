package com.sametsisman.ornekproje1.view.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.adapter.MovieAdapter
import com.sametsisman.ornekproje1.view.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*


class HomeFragment : Fragment() {
    private lateinit var viewModel : HomeViewModel
    private val movieAdapter = MovieAdapter()
    private var currentPage = 1
    private var totalPages = 1
    private var first = true

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

        fetchPopularMovies()

        initRecyclerview()

        observeLiveData()

        first = false

        swipeRefreshLayout.setOnRefreshListener {
            home_ui.visibility = View.GONE
            errorText.visibility = View.GONE
            movieLoading.visibility = View.INVISIBLE
            fetchPopularMovies()

            swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun initRecyclerview() {
        recyclerview_popular.layoutManager = GridLayoutManager(context,2)
        recyclerview_popular.adapter = movieAdapter

        recyclerview_popular.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = (recyclerview_popular.layoutManager as GridLayoutManager).childCount
                val pastVisibleItem = (recyclerview_popular.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
                val total = movieAdapter.itemCount
                if (visibleItemCount + pastVisibleItem >= total){
                    currentPage++
                    fetchPopularMovies()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun fetchPopularMovies() {
        CoroutineScope(Dispatchers.IO).launch {

            val job1 : Deferred<Unit> = async {
                viewModel.getDataFromAPI(currentPage.toString(),first)
            }

            job1.await()
        }
    }

    private fun observeLiveData() {
        viewModel.movies.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            it.let {
                val oldCount = movieAdapter.itemCount
                movieAdapter.setList(it.results)
                movieAdapter.notifyItemRangeInserted(oldCount,movieAdapter.itemCount)
                home_ui.visibility = View.VISIBLE
            }
        })

        viewModel.totalPages.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            it.let {
                totalPages = it
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
                    home_ui.visibility = View.GONE
                    errorText.visibility = View.GONE
                } else {
                    movieLoading.visibility = View.GONE
                }
            }
        })
    }

}