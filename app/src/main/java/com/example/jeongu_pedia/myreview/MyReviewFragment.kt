package com.example.jeongu_pedia.myreview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jeongu_pedia.R
import com.example.jeongu_pedia.databinding.FragmentMyreviewBinding
import com.example.jeongu_pedia.datamodel.Review
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyReviewFragment: Fragment(R.layout.fragment_myreview) {
    private var binding: FragmentMyreviewBinding? = null
    private lateinit var reviewAdapter: MyReviewAdapter

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val database = Firebase.firestore
    private var reviewColl = database.collection("reviews")

    private var reviewList = mutableListOf<Review>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMyreviewBinding = FragmentMyreviewBinding.bind(view)
        binding = fragmentMyreviewBinding

        reviewAdapter = context?.let { MyReviewAdapter(it, reviewList) }!!

        fragmentMyreviewBinding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        fragmentMyreviewBinding.recyclerView.adapter = reviewAdapter

        if (auth.currentUser?.uid != null) {
            reviewColl.whereEqualTo("reviewer", "${auth.currentUser?.uid}").get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        val review = Review("${doc.data.get("movieId")}","${doc.data.get("reviewer")}","${doc.data.get("poster")}",
                            "${doc.data.get("score")}","${doc.data.get("text")}","${doc.data.get("createdAt")}")
                        reviewList.add(review)
                        reviewAdapter.submitList(reviewList)
                    }
                }.addOnFailureListener {
                    Log.e("파이어스토어", "Data is not found")
                }
        } else {
            Snackbar.make(binding!!.root, "로그인 후 확인해주세요.", Snackbar.LENGTH_LONG).show()
        }

    }
    override fun onResume() {
        super.onResume()

        reviewAdapter.notifyDataSetChanged()
    }
    override fun onDestroyView() {
        super.onDestroyView()

        reviewList = mutableListOf()
    }
}