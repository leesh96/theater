package com.dbp.theater

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dbp.theater.databinding.ActivityLoginBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val TAG = "LOGIN"
    private val URL = "http://141.164.50.235/shyg/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun login(email: String, password: String) {
        val url = URL + "read_user.php"
        Log.d(TAG, url)

        val requestBody = FormBody.Builder()
            .add("email", email)
            .add("pswd", password)
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
                Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                Handler(mainLooper).post {
                    val jsonObject = JSONObject(response.body?.string())

                    if (jsonObject.getBoolean("success")) {
                        GlobalApplication.prefs.setInt("user_id", jsonObject.getInt("id"))
                        GlobalApplication.prefs.setString(
                            "user_email",
                            jsonObject.getString("email")
                        )
                        GlobalApplication.prefs.setString(
                            "user_password",
                            jsonObject.getString("password")
                        )
                        GlobalApplication.prefs.setString("user_name", jsonObject.getString("name"))
                        GlobalApplication.prefs.setString(
                            "user_phone",
                            jsonObject.getString("phone")
                        )

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}