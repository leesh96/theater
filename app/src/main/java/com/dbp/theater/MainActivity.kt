package com.dbp.theater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dbp.theater.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val TAG = "WITHDRAW"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            GlobalApplication.prefs.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnWithdraw.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("회원 탈퇴")
                .setMessage("탈퇴하시겠습니까?")
                .setPositiveButton("확인") { dialog, which ->
                    withdraw()
                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        binding.btnUserinfo.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }
    }

    private fun withdraw() {
        val email = GlobalApplication.prefs.getString("user_email")

        val url = URL + "delete_user.php"
        Log.d(TAG, url)

        val requestBody = FormBody.Builder()
            .add("email", email!!)
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
                Toast.makeText(this@MainActivity, "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, responseBody.toString())

                if (responseBody.toBoolean()) {
                    GlobalApplication.prefs.clear()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "회원 탈퇴 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}