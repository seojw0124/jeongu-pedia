package com.example.jeongu_pedia.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jeongu_pedia.databinding.ItemMovieBinding
import com.example.jeongu_pedia.datamodel.Movie

class HomeAdapter(val onItemClicked: (Movie) -> Unit) : ListAdapter<Movie, HomeAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieModel: Movie) {

            binding.titleTextView.text = movieModel.title
            binding.dateCountryTextView.text = movieModel.pubDate + "·" + movieModel.country

            if (movieModel.poster.isNotEmpty()) {
                Glide.with(binding.posterImageView.context)
                    .load(movieModel.poster)
                    .into(binding.posterImageView)
            }

            binding.root.setOnClickListener {
                onItemClicked(movieModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.pubDate == newItem.pubDate
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

}