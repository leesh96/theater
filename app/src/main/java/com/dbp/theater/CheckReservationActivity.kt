package com.dbp.theater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbp.theater.databinding.ActivityCheckReservationBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CheckReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckReservationBinding

    private val reservationList = mutableListOf<Reservation>()
    private lateinit var mAdapter: ReservationCheckAdapter

    private val TAG = "RESERVATION_CHECK"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.rcviewReservation.apply {
            mAdapter = ReservationCheckAdapter({ item -> moveToDetail(item) })
            layoutManager = LinearLayoutManager(this@CheckReservationActivity)
            setHasFixedSize(true)
            adapter = mAdapter
            reservationList.clear()
            mAdapter.datas = reservationList
            mAdapter.notifyDataSetChanged()
        }

        readReservation(GlobalApplication.prefs.getInt("user_id"))
    }

    private fun readReservation(userid: Int) {
        val url = URL + "read_reservation.php"
        Log.d(TAG, url)

        val requestBody = FormBody.Builder()
            .add("userid", userid.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.toString())
                e.printStackTrace()
                Toast.makeText(this@CheckReservationActivity, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body?.string())
                val jsonArray = jsonObject.getJSONArray("reservation")

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val data = Reservation(
                        item.getString("id").toInt(),
                        item.getString("payment"),
                        item.getString("seat"),
                        item.getString("userName"),
                        item.getString("startTime"),
                        item.getString("endTime"),
                        URL + item.getString("moviePoster"),
                        item.getString("movieTitle"),
                        item.getString("screenName"),
                        item.getString("theaterName"),
                    )

                    runOnUiThread {
                        reservationList.add(data)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun moveToDetail(item: Reservation) {
        startActivity(Intent(this, ReservationDetailActivity::class.java).apply {
            putExtra("reservation data", item)
        })
        finish()
    }
}