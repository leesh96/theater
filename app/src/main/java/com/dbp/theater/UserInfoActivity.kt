package com.dbp.theater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dbp.theater.databinding.ActivityUserInfoBinding
import okhttp3.*
import java.io.IOException

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding

    private val TAG = "UPDATE_USER"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tvUserEmail.text = GlobalApplication.prefs.getString("user_email")
        binding.etPassword.setText(GlobalApplication.prefs.getString("user_password"))
        binding.etPasswordCheck.setText(GlobalApplication.prefs.getString("user_password"))
        binding.etName.setText(GlobalApplication.prefs.getString("user_name"))
        binding.etPhoneNumber.setText(GlobalApplication.prefs.getString("user_phone"))

        binding.btnModify.setOnClickListener {
            modifyUser(
                binding.etPassword.text.toString(),
                binding.etName.text.toString(),
                binding.etPhoneNumber.text.toString()
            )
        }
    }

    private fun modifyUser(password: String, name: String, phone: String) {
        val url = URL + "update_user.php"
        Log.d(TAG, url)

        val requestBody = FormBody.Builder()
            .add("email", GlobalApplication.prefs.getString("user_email").toString())
            .add("pswd", password)
            .add("name", name)
            .add("phone", phone)
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
                Toast.makeText(this@UserInfoActivity, "정보 수정 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, responseBody.toString())

                if (responseBody.toBoolean()) {
                    startActivity(Intent(this@UserInfoActivity, LoginActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                    finish()
                } else {
                    Toast.makeText(this@UserInfoActivity, "정보 수정 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}