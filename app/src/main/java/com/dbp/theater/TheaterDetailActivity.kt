package com.dbp.theater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dbp.theater.databinding.ActivityTheaterDetailBinding

class TheaterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTheaterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTheaterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val theaterData = intent.getSerializableExtra("theaterData") as TheaterData

        binding.btnBack.setOnClickListener { finish() }

        setData(theaterData)

    }

    private fun setData(theaterData: TheaterData) {
        binding.tvTheaterAddress.text = "주소 : " + theaterData.address
        binding.tvTheaterContact.text = "전화번호 : " + theaterData.contact
        binding.tvTheaterName.text = theaterData.name
        binding.tvTheaterScreen.text = "상영관 수 : " + theaterData.screen.toString() + "개"
        binding.tvTheaterTime.text = "영업 시 : " + theaterData.opentime
        Glide.with(this).load(theaterData.logo).into(binding.ivLogo)
        Glide.with(this).load(theaterData.map).into(binding.ivTheaterMap)
    }
}