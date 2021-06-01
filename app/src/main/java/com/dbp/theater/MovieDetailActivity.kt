package com.dbp.theater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dbp.theater.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieData = intent.getSerializableExtra("movieData") as MovieData
        setData(movieData)

        binding.btnBack.setOnClickListener { finish() }

    }

    private fun setData(movieData: MovieData) {
        binding.tvMovieTitle.text = movieData.title
        binding.tvMovieContent.text = movieData.content
        binding.tvMovieGenre.text = "장르 : " + movieData.genre
        binding.tvMovieOpeningDay.text = "개봉일 : " + movieData.openingDay
        binding.tvMovieTime.text = "상영시간 : " + movieData.time
        Glide.with(this).load(movieData.poster).into(binding.ivPoster)
    }
}