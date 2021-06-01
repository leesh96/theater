package com.dbp.theater

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbp.theater.databinding.ActivityTheaterInfoBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class TheaterInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTheaterInfoBinding
    private lateinit var mTheaterAdapter: TheaterAdapter
    private val theaterList = mutableListOf<TheaterData>()

    private val TAG = "UPDATE_USER"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTheaterInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcviewTheater.apply {
            mTheaterAdapter = TheaterAdapter()
            layoutManager = LinearLayoutManager(this@TheaterInfoActivity)
            setHasFixedSize(true)
            adapter = mTheaterAdapter
            theaterList.clear()
            mTheaterAdapter.datas = theaterList
            mTheaterAdapter.notifyDataSetChanged()
        }

        getTheaterJson()
    }

    private fun getTheaterJson() {
        val url = URL + "read_theater.php"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.toString())
                e.printStackTrace()
                Toast.makeText(this@TheaterInfoActivity, "영화관 정보 로드 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, responseBody.toString())
                val theaterBody = JSONObject(responseBody!!)
                val theaterJsonArray = theaterBody.optJSONArray("theater")
                Log.d("array length", theaterJsonArray.length().toString())
                for(i in 0 until theaterJsonArray.length()) {
                    val jsonObject = theaterJsonArray.getJSONObject(i)
                    val newTheater = TheaterData(
                        URL + jsonObject.getString("logo"),
                        jsonObject.getString("name"),
                        jsonObject.getInt("screen"),
                        jsonObject.getString("opentime"),
                        jsonObject.getString("contact"),
                        jsonObject.getString("address"),
                        URL + jsonObject.getString("map")
                    )
                    Log.d("data", newTheater.toString())
                    runOnUiThread {
                        theaterList.add(newTheater)
                        mTheaterAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
