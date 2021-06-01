package com.dbp.theater

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dbp.theater.databinding.ActivitySignupBinding
import okhttp3.*
import java.io.IOException

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val TAG = "SIGNUP"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            signup(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etName.text.toString(),
                binding.etPhoneNumber.text.toString()
            )
        }
    }

    private fun signup(email: String, password: String, name: String, phone: String) {
        val url = URL + "create_user.php"
        Log.d(TAG, url)

        val requestBody = FormBody.Builder()
            .add("email", email)
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
                Toast.makeText(this@SignupActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, responseBody.toString())

                if (responseBody.toBoolean()) {
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignupActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
