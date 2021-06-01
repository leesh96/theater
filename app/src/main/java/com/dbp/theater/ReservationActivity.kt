package com.dbp.theater

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbp.theater.databinding.ActivityReservationBinding
import com.dbp.theater.databinding.DialogReservationBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private val playList = mutableListOf<MoviePlayModel>()
    private lateinit var mAdapter: ReservationAdapter

    private lateinit var dialog: AlertDialog

    private val TAG = "RESERVATION"
    private val URL = "http://141.164.50.235/shyg/"

    private val dateList = listOf("선택하세요", "2021-06-03", "2021-06-04")
    private val movieList = listOf("선택하세요", "분노의 질주: 더 얼티메이트", "크루엘라", "컨저링 3: 악마가 시켰다", "프로페서 앤 매드맨", "파이프라인", "보이저스", "라이더스 오브 저스티스", "낫아웃", "썰", "포겟 미 낫-엄마에게 쓰는 편지", "굴뚝마을의 푸펠", "극장판 귀멸의 칼날-무한열차편", "#위왓치유", "도라에몽-스탠바이미 2", "전야", "비와 당신의 이야기")
    private val theaterList = listOf("선택하세요", "롯데시네마 엠비씨네(진주)", "CGV 진주혁신")
    private val paymentList = listOf("신용/체크카드", "무통장입급", "네이버페이", "카카오페이", "휴대폰")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.rcviewMoviePlay.apply {
            mAdapter = ReservationAdapter({ item -> showReservationDialog(item) })
            layoutManager = LinearLayoutManager(this@ReservationActivity)
            setHasFixedSize(true)
            adapter = mAdapter
            playList.clear()
            mAdapter.datas = playList
            mAdapter.notifyDataSetChanged()
        }

        getMoviePlay()

        binding.spDate.apply {
            adapter = ArrayAdapter(this@ReservationActivity, R.layout.simple_list_item_1, dateList)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        val filteredList =
                            mAdapter.datas.filter { it.startTime.contains(dateList[position]) }
                                .toMutableList()
                        mAdapter.datas = filteredList
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {   }
            }
        }

        binding.spMovie.apply {
            adapter = ArrayAdapter(this@ReservationActivity, R.layout.simple_list_item_1, movieList)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        val filteredList =
                            mAdapter.datas.filter { it.movieTitle == movieList[position] }.toMutableList()
                        mAdapter.datas = filteredList
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {   }
            }
        }

        binding.spTheater.apply {
            adapter = ArrayAdapter(this@ReservationActivity, R.layout.simple_list_item_1, theaterList)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        val filteredList =
                            mAdapter.datas.filter { it.theaterName == theaterList[position] }
                                .toMutableList()
                        mAdapter.datas = filteredList
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {   }
            }
        }
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
                        item.getString("screenChairRow").toCharArray()[0],
                        item.getString("screenChairCol").toInt(),
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

    private fun showReservationDialog(item: MoviePlayModel) {
        val dialogView = DialogReservationBinding.inflate(layoutInflater)
        var seatRow: Char = 'A'
        var seatCol: Int = 0
        var payment: String = ""

        val seatRowList = ('A'..item.chairRow).toList()
        val seatColList = (1..item.chairCol).toList()

        dialog = AlertDialog.Builder(this)
            .setView(dialogView.root)
            .setPositiveButton("예매") { dialog, which ->
                reservation(payment,
                    "$seatRow $seatCol", GlobalApplication.prefs.getInt("user_id"), item.id)
            }
            .setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .apply {
                dialogView.tvMovieTitle.text = "영화 : " + item.movieTitle
                dialogView.tvTheaterName.text = "극장 : " + item.theaterName
                dialogView.tvScreenName.text = "상영관 : " + item.screenName
                dialogView.tvPlayTime.text = "시간 : " + item.startTime + " ~ " + item.endTime
                dialogView.spPayment.apply {
                    adapter = ArrayAdapter(context, R.layout.simple_list_item_1, paymentList)
                    setSelection(0)
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            payment = paymentList[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {   }
                    }
                }
                dialogView.spSeatRow.apply {
                    adapter = ArrayAdapter(context, R.layout.simple_list_item_1, seatRowList)
                    setSelection(0)
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            seatRow = seatRowList[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {   }
                    }
                }
                dialogView.spSeatCol.apply {
                    adapter = ArrayAdapter(context, R.layout.simple_list_item_1, seatColList)
                    setSelection(0)
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            seatCol = seatColList[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {   }
                    }
                }
            }
            .create()

        dialog.show()
    }

    private fun reservation(payment: String, seat: String, userID: Int, moviePlayID: Int) {
        val url = URL + "create_reservation.php"
        Log.d(TAG, url)

        val requestBody = FormBody.Builder()
            .add("payment", payment)
            .add("seat", seat)
            .add("userID", userID.toString())
            .add("movieplayID", moviePlayID.toString())
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
                Toast.makeText(this@ReservationActivity, "예매 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, responseBody.toString())

                if (responseBody.toBoolean()) {
                    dialog.dismiss()
                    startActivity(Intent(this@ReservationActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@ReservationActivity, "예매 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}