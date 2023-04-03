package com.example.jeongu_pedia.myreview

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jeongu_pedia.ReviewActivity
import com.example.jeongu_pedia.databinding.ItemReviewedMovieBinding
import com.example.jeongu_pedia.datamodel.Movie
import com.example.jeongu_pedia.datamodel.Review
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyReviewAdapter(val context: Context, val dataSet: MutableList<Review>?) : ListAdapter<Review, MyReviewAdapter.ViewHolder>(diffUtil) {

    private val database = Firebase.firestore
    private var movieColl = database.collection("movies")
    private var movie: Movie? = null

    inner class ViewHolder(private val binding: ItemReviewedMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reviewModel: Review) {
            binding.myScoreTextView.text = reviewModel.score
            if (reviewModel.poster.isNotEmpty()) {
                Glide.with(binding.posterImageView.context)
                    .load(reviewModel.poster)
                    .into(binding.posterImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReviewedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyReviewAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])

        if (position != RecyclerView.NO_POSITION) {
            holder.itemView.setOnClickListener {
                movieColl.whereEqualTo("id", currentList[position].movieId).get()
                    .addOnSuccessListener { documents ->
                        for (doc in documents) {
                            movie = Movie("${doc.data.get("id")}","${doc.data.get("title")}","${doc.data.get("poster")}",
                                "${doc.data.get("pubDate")}","${doc.data.get("director")}","${doc.data.get("casts")}","${doc.data.get("country")}","${doc.data.get("userDating")}")
                            val intent = Intent(context, ReviewActivity::class.java)
                            intent.putExtra("movieModel", movie)
                            startActivity(context, intent, null)
                        }
                    }.addOnFailureListener {
                        Log.e("파이어스토어", "Data is not found")
                    }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }
        }
    }
}