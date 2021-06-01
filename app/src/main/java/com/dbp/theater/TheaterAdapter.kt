package com.dbp.theater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbp.theater.databinding.ItemTheaterBinding

class TheaterAdapter(val click: (TheaterData) -> Unit) : RecyclerView.Adapter<TheaterAdapter.ViewHolder>() {
    var datas = mutableListOf<TheaterData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTheaterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(val binding: ItemTheaterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TheaterData) {
            Log.d("item", item.toString())
            Glide.with(binding.root).load(item.logo).into(binding.imgLogo)
            binding.tvName.text = item.name
            binding.tvAddress.text = item.address
            binding.tvContact.text = item.contact
            binding.root.setOnClickListener {
                click(item)
            }
        }
    }
}