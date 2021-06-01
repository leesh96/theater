package com.dbp.theater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbp.theater.databinding.ActivityMovieInfoBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable

class MovieInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieInfoBinding
    private lateinit var mMovieAdapter: MovieAdapter
    private val movieList = mutableListOf<MovieData>()

    private val TAG = "UPDATE_USER"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcviewMovie.apply {
            mMovieAdapter = MovieAdapter { item -> clickItem(item) }
            layoutManager = LinearLayoutManager(this@MovieInfoActivity)
            setHasFixedSize(true)
            adapter = mMovieAdapter
            movieList.clear()
            mMovieAdapter.datas = movieList
            mMovieAdapter.notifyDataSetChanged()
        }

        binding.btnBack.setOnClickListener { finish() }

        getTheaterJson()
    }

    private fun clickItem(movieData: MovieData) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movieData", movieData as Serializable)
        startActivity(intent)
    }

    private fun getTheaterJson() {
        val url = URL + "read_movie.php"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.toString())
                e.printStackTrace()
                Toast.makeText(this@MovieInfoActivity, "영화 정보 로드 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, responseBody.toString())
                val movieBody = JSONObject(responseBody!!)
                val movieJsonArray = movieBody.optJSONArray("movie")
                for (i in 0 until movieJsonArray!!.length()) {
                    val jsonObject = movieJsonArray.getJSONObject(i)
                    val newMovie = MovieData(
                        URL + jsonObject.getString("poster"),
                        jsonObject.getString("title"),
                        jsonObject.getString("openingDay"),
                        jsonObject.getString("time"),
                        jsonObject.getString("genre"),
                        jsonObject.getString("content")
                    )
                    Log.d("data", newMovie.toString())
                    runOnUiThread {
                        movieList.add(newMovie)
                        mMovieAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
