package com.sametsisman.ornekproje1.view.feed

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.adapter.SearchAdapter
import com.sametsisman.ornekproje1.view.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*


class SearchFragment : Fragment() {
    private lateinit var viewModel : SearchViewModel
    private val searchAdapter = SearchAdapter()
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        imageSearch.setOnClickListener {
            searchAdapter.movieList.clear()
            searchAdapter.notifyDataSetChanged()
            currentPage = 1
            fetchSearchMovies()
        }

        init()

        observeLiveData()

        searchScrollUpButton.setOnClickListener {
            searchRecyclerview.layoutManager!!.scrollToPosition(0)
        }
    }

    private fun init() {
        searchRecyclerview.layoutManager = GridLayoutManager(this.context,2)
        searchRecyclerview.adapter = searchAdapter

        searchRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = (searchRecyclerview.layoutManager as GridLayoutManager).childCount
                val pastVisibleItem = (searchRecyclerview.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
                val total = searchAdapter.itemCount
                if (visibleItemCount + pastVisibleItem > 20){
                    searchScrollUpButton.visibility = View.VISIBLE
                }else{
                    searchScrollUpButton.visibility = View.GONE
                }
                if (visibleItemCount + pastVisibleItem >= total){
                    if (!inputText.text.toString().isEmpty()){
                        if (currentPage < totalPages){
                            currentPage++
                            fetchSearchMovies()
                        }
                    }
                }
            }
        })

        inputText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    fetchSearchMovies()
                    return true
                }
                return false
            }
        })
    }

    private fun fetchSearchMovies() {
        val query = inputText.text.toString()

        if (query.isEmpty()){
            searchAdapter.movieList.clear()
            searchAdapter.notifyDataSetChanged()
        }else{
            CoroutineScope(Dispatchers.IO).launch {

                val job1 : Deferred<Unit> = async {
                    viewModel.searchMovieFromApi(query,currentPage.toString())
                }

                job1.await()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.searchMovies.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            it.let {
                val oldCount = searchAdapter.itemCount
                searchAdapter.setList(it.results)
                searchAdapter.notifyItemRangeInserted(oldCount,searchAdapter.itemCount)
                searchRecyclerview.visibility = View.VISIBLE
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
                    searchErrorText.visibility = View.VISIBLE
                } else {
                    searchErrorText.visibility = View.GONE
                }
            }
        })

        viewModel.movieLoading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it) {
                    searchMovieLoading.visibility = View.VISIBLE
                    searchRecyclerview.visibility = View.GONE
                    searchErrorText.visibility = View.GONE
                } else {
                    searchMovieLoading.visibility = View.GONE
                }
            }
        })
    }

}