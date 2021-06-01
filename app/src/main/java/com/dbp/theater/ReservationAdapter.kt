package com.dbp.theater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbp.theater.databinding.ItemMoviePlayBinding

class ReservationAdapter(val onClick: (MoviePlayModel) -> Unit) :
    RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {
    var datas = mutableListOf<MoviePlayModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMoviePlayBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemMoviePlayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviePlayModel) {
            binding.tvMovieTitle.text = item.movieTitle
            binding.tvTheaterName.text = item.theaterName
            binding.tvScreenName.text = item.screenName
            binding.tvStartTime.text = item.startTime
            binding.tvEndTime.text = item.endTime
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }
}