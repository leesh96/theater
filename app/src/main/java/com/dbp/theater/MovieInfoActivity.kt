package com.dbp.theater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dbp.theater.databinding.ActivityMovieInfoBinding

class MovieInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieInfoBinding

    private val TAG = "UPDATE_USER"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}