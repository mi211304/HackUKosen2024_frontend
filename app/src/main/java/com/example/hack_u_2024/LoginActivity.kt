/*
ここではログインを実装する
画面はactivity_login.xmlを使用する
ユーザーネーム、パスワードを入力しバックエンドからトークンとユーザーIDを受け取る
ユーザーネーム、トークン、ユーザーIDはSharedPreferencesを利用して保存
 */

package com.example.hack_u_2024

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import android.net.Uri
import kotlinx.coroutines.withContext
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_login_fields), Toast.LENGTH_SHORT).show()
            } else {
                val baseUrl = Uri.Builder()
                    .scheme("http")
                    .encodedAuthority("153.121.33.221:8080")
                    .path("login")
                    .appendQueryParameter("username", username)
                    .appendQueryParameter("password", password)
                    .build()
                    .toString()

                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        Log.d("LoginActivity", "Request URL: $baseUrl")
                        val response = get(baseUrl)
                        val jsonResponse = JSONObject(response)
                        val token = jsonResponse.optString("token", null)
                        val userId = jsonResponse.optInt("user-id", -1).takeIf { it != -1 }

                        if (token != null && userId != null) {
                            Log.i("LoginActivity", "Login successful. Token: $token, UserId: $userId") // 成功時のログ
                            saveUserCredentials(username, token, userId.toString())
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.w("LoginActivity", "Invalid response from server: $response") // 不正なレスポンス時のログ
                            Toast.makeText(this@LoginActivity, "Invalid response from server", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("LoginActivity", "Network error occurred: ${e.message}", e) // エラー時のログ
                        Toast.makeText(this@LoginActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUserCredentials(username: String, token: String, userId: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("username", username)
        editor.putString("token", token)
        editor.putString("userId", userId)

        val success = editor.commit()
        if (!success) {
            Log.e("LoginActivity", "Failed to save user credentials") // 保存失敗時のログ
        }
    }

    private suspend fun get(url: String): String = withContext(Dispatchers.IO) {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Accept", "application/json")
        }

        try {
            val responseCode = connection.responseCode
            Log.d("LoginActivity", "Server responded with status code: $responseCode") // レスポンスコードを記録
            if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                throw IOException("Server returned non-OK response: $responseCode")
            }
        } finally {
            connection.disconnect()
        }
    }
}