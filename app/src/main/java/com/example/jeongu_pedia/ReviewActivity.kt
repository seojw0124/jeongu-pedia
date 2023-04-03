package com.example.jeongu_pedia

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jeongu_pedia.databinding.ActivityReviewBinding
import com.example.jeongu_pedia.datamodel.Movie
import com.example.jeongu_pedia.datamodel.Review
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ReviewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private var movie: Movie? = null
    private var reviewScore: String? = null
    private var reviewText: String? = null

    private val database = Firebase.firestore
    private var reviewColl = database.collection("reviews")

    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movie = intent.getParcelableExtra<Movie>("movieModel")
        binding.averageScoreTextView.text = "관람객 평점 " + movie?.userDating
        binding.titleTextView.text = movie?.title
        binding.dateCountryTextView.text = movie?.pubDate + "·" + movie?.country
        binding.directorTextView.text = "감독: " + movie?.director + "\n출연진: " + movie?.casts
        Glide.with(binding.posterImageView.context)
            .load(movie?.poster)
            .into(binding.posterImageView)

        reviewColl.whereEqualTo("movieId", "${movie?.id}").whereEqualTo("reviewer", "${auth.currentUser?.uid}").get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    Log.d("파이어스토어", "${doc.id}")
                    binding.ratingBar.rating = "${doc.data.get("score")}".toFloat()
                    binding.reviewFieldEditText.text = Editable.Factory.getInstance().newEditable("${doc.data.get("text")}")
                    binding.submitButton.isEnabled = false
                    binding.deleteButton.isEnabled = true
                    documentId = doc.id
                }
            }.addOnFailureListener {
                Log.e("파이어스토어", "Data is not found")
            }

        val submitButton = findViewById<Button>(R.id.submitButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        if (auth.currentUser != null) {
            submitButton.setOnClickListener {
                reviewScore = binding.ratingBar.rating.toString()
                reviewText = binding.reviewFieldEditText.text.toString()

                addReview()
            }
            deleteButton.setOnClickListener {
                deleteReview()
            }
        } else {
            submitButton.setOnClickListener {
                Snackbar.make(binding.root, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    private fun addReview() {
        val review = Review("${movie!!.id}", "${auth.currentUser!!.uid}","${movie!!.poster}","$reviewScore","$reviewText", Date().toString())
        reviewColl.add(review).addOnSuccessListener {
            Toast.makeText(this@ReviewActivity, "리뷰 등록에 성공하셨습니다.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(this@ReviewActivity, "에러 발생!! 리뷰 등록에 실패하셨습니다.", Toast.LENGTH_LONG).show()
            Log.e("firestore", it.toString())
        }
        Log.d("왜 안돼", "$reviewText")
    }
    private fun deleteReview() {
        documentId?.let { it1 -> reviewColl.document(it1).delete() }
        Toast.makeText(this@ReviewActivity, "리뷰가 삭제되었습니다.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
}