package com.sametsisman.ornekproje1.view.feed

import android.content.Intent
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
import com.sametsisman.ornekproje1.view.model.Room
import com.sametsisman.ornekproje1.view.viewmodel.DetailActivityViewModel
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel : DetailActivityViewModel
    private val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
    private lateinit var firestore: FirebaseFirestore
    private lateinit var choosenSerie : MovieDetails
    private lateinit var senderId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movieId = intent.getIntExtra("id",1)

        viewModel = ViewModelProviders.of(this).get(DetailActivityViewModel::class.java)

        viewModel.getDataFromAPI(movieId)

        senderId = getSharedPreferences("preferences", MODE_PRIVATE).getString("senderId","")!!

        firestore = Firebase.firestore

        observeLiveData()

        listenButton()

    }

    private fun listenButton(){
        joinRoomButton.setOnClickListener {
            val roomName = hashMapOf<String,Any>()
            roomName.put("roomName",choosenSerie.title)
            roomName.put("roomImageUrl",POSTER_BASE_URL + choosenSerie.posterPath)
            firestore.collection("rooms").document(choosenSerie.id.toString()).set(roomName)
            firestore.collection("usersss").document(senderId).collection("registeredRooms").document(choosenSerie.id.toString()).set(roomName)
            joinRoomButton.visibility = View.GONE
            leaveRoomButton.visibility = View.VISIBLE
            val intent = Intent(this,FeedActivity::class.java)
            intent.putExtra("fromDetail",1)
            startActivity(intent)
        }

        leaveRoomButton.setOnClickListener {
            firestore.collection("usersss").document(senderId).collection("registeredRooms").document(choosenSerie.id.toString())
                .delete()
                .addOnSuccessListener {
                    leaveRoomButton.visibility = View.GONE
                    joinRoomButton.visibility = View.VISIBLE
                }
        }
    }

    private fun observeLiveData() {
        viewModel.movieDetail.observe(this, Observer { movieDetail ->

            movieDetail?.let {
                bindUi(it)
                choosenSerie = it
                setJoinOrLeaveButton()
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
        revenueText.text = it.revenue.toString()
        overviewText.text = it.overview

        val moviePosterUrl = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterUrl)
            .into(imageMovie)
    }

    private fun setJoinOrLeaveButton(){
        firestore.collection("usersss").document(senderId).collection("registeredRooms").document(choosenSerie.id.toString())
            .get()
            .addOnSuccessListener {it1 ->
                if (it1.getString("roomName") != null){
                    joinRoomButton.visibility = View.GONE
                    leaveRoomButton.visibility = View.VISIBLE
                }else{
                    joinRoomButton.visibility = View.VISIBLE
                    leaveRoomButton.visibility = View.GONE
                }

            }.addOnFailureListener{
                joinRoomButton.visibility = View.VISIBLE
                leaveRoomButton.visibility = View.GONE
            }
    }
}
