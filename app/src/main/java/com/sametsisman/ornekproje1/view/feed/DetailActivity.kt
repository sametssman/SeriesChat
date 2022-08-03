package com.sametsisman.ornekproje1.view.feed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.StructuredQuery
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.model.MovieDetails
import com.sametsisman.ornekproje1.view.viewmodel.DetailActivityViewModel
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel : DetailActivityViewModel
    private val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
    private lateinit var firestore: FirebaseFirestore
    private lateinit var choosenSerie : MovieDetails
    private lateinit var senderId : String
    private lateinit var senderDocumentId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val movieId = intent.getIntExtra("id",1)
        viewModel = ViewModelProviders.of(this).get(DetailActivityViewModel::class.java)
        viewModel.getDataFromAPI(movieId)
        senderId = getSharedPreferences("preferences", MODE_PRIVATE).getString("senderId","")!!
        senderDocumentId = getSharedPreferences("preferences", MODE_PRIVATE).getString("senderDocumentId","")!!
        firestore = Firebase.firestore
        joinRoomButton.setOnClickListener {
            val roomName = hashMapOf<String,Any>()
            roomName.put("roomName",choosenSerie.title)
            firestore.collection("rooms").document(choosenSerie.id.toString()).set(roomName)
            firestore.collection("usersss").document(senderDocumentId).collection("registeredRooms").document(choosenSerie.id.toString()).set(roomName)
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.movieDetail.observe(this, Observer { movieDetail ->

            movieDetail?.let {
                bindUi(it)
                choosenSerie = it
                detail_ui.visibility = View.VISIBLE
            }

        })

        viewModel.movieError.observe(this, Observer { error->
            error?.let {
                if(it) {
                    detailErrorText.visibility = View.VISIBLE
                } else {
                    detailErrorText.visibility = View.GONE
                }
            }
        })

        viewModel.movieLoading.observe(this, Observer { loading->
            loading?.let {
                if (it) {
                    detailMovieLoading.visibility = View.VISIBLE
                    detail_ui.visibility = View.GONE
                    detailErrorText.visibility = View.GONE
                } else {
                    detailMovieLoading.visibility = View.GONE
                }
            }
        })
    }

    private fun bindUi(it: MovieDetails) {
        titleText.text = it.title
        taglineText.text = it.tagline
        budgetText.text = it.budget.toString()
        ratingText.text = it.rating.toString()
        releaseDateText.text = it.releaseDate
        runtimeText.text = " minutes"
        revenueText.text = it.revenue.toString()
        overviewText.text = it.overview

        val moviePosterUrl = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterUrl)
            .into(imageMovie)
    }
}
