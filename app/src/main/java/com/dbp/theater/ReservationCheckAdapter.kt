package com.dbp.theater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbp.theater.databinding.ItemReservationBinding

class ReservationCheckAdapter(val onClick: (Reservation) -> Unit) : RecyclerView.Adapter<ReservationCheckAdapter.ViewHolder>() {
    var datas = mutableListOf<Reservation>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservationCheckAdapter.ViewHolder {
        val binding = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationCheckAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemReservationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Reservation) {
            binding.tvMovieTitle.text = item.movieTitle
            binding.tvTheaterName.text = item.theaterName
            binding.tvScreenName.text = item.screenName
            binding.tvSeat.text = "좌석 : ${item.seat}"
            binding.tvStartTime.text = item.startTime
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }
}