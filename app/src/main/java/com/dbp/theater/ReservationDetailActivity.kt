package com.dbp.theater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dbp.theater.databinding.ActivityReservationDetailBinding
import okhttp3.*
import java.io.IOException

class ReservationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationDetailBinding

    private val TAG = "RESERVATION_DETAIL"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, CheckReservationActivity::class.java))
            finish()
        }

        intent.getParcelableExtra<Reservation>("reservation data")?.let { data ->
            setView(data)
            binding.btnCancelReservation.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("예매 취소")
                    .setMessage("예매 취소하시겠습니까?")
                    .setPositiveButton("확인") { dialog, which ->
                        delete(data.id)
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun setView(data: Reservation) {
        Glide.with(this).load(data.moviePoster).into(binding.ivPoster)
        binding.tvUserName.text = "${data.userName}님, 예매 내역 입니다."
        binding.tvMovieTitle.text = "영화 : ${data.movieTitle}"
        binding.tvTheaterName.text = "영화관 : ${data.theaterName}"
        binding.tvScreenName.text = "상영관 : ${data.screenName}"
        binding.tvMovieTime.text = "상영 시간 : ${data.startTime} ~ ${data.endTime}"
        binding.tvSeat.text = "${data.seat} 좌석 예매 하셨습니다."
        binding.tvPayment.text = "결제 방법 : ${data.payment}"
    }

    private fun delete(id: Int) {
        val url = URL + "delete_reservation.php"
        Log.d(TAG, url)

        val requestBody = FormBody.Builder()
            .add("id", id.toString())
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
                Toast.makeText(this@ReservationDetailActivity, "예매 취소 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, responseBody.toString())

                if (responseBody.toBoolean()) {
                    startActivity(Intent(this@ReservationDetailActivity, CheckReservationActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@ReservationDetailActivity, "예매 취소 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}