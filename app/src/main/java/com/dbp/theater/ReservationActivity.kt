package com.dbp.theater

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbp.theater.databinding.ActivityReservationBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private val playList = mutableListOf<MoviePlayModel>()
    private lateinit var mAdapter: ReservationAdapter

    private val TAG = "RESERVATION"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.rcviewMoviePlay.apply {
            mAdapter = ReservationAdapter()
            layoutManager = LinearLayoutManager(this@ReservationActivity)
            setHasFixedSize(true)
            adapter = mAdapter
            playList.clear()
            mAdapter.datas = playList
            mAdapter.notifyDataSetChanged()
        }

        getMoviePlay()
    }

    private fun getMoviePlay() {
        val url = URL + "read_movie_play.php"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.toString())
                e.printStackTrace()
                Toast.makeText(this@ReservationActivity, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body?.string())
                val jsonArray = jsonObject.getJSONArray("moviePlay")

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val data = MoviePlayModel(
                        item.getString("id").toInt(),
                        item.getString("startTime"),
                        item.getString("endTime"),
                        item.getString("movieTitle"),
                        item.getString("screenName"),
                        item.getString("screenChairRow"),
                        item.getString("screenChairCol"),
                        item.getString("theaterName"),
                    )

                    runOnUiThread {
                        playList.add(data)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}