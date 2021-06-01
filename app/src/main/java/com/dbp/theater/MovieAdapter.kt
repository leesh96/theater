package com.dbp.theater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbp.theater.databinding.ItemMovieBinding

class MovieAdapter(val click: (MovieData) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    var datas = mutableListOf<MovieData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieData) {
            binding.tvMovieTitle.text = item.title
            binding.tvTheaterGenre.text = item.genre
            binding.root.setOnClickListener {
                click(item)
            }
        }
    }
}