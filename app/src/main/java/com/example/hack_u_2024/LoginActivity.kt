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
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // UI要素を取得
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        // ログインボタンのクリックイベント
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // 入力が空かチェック
            if (username.isEmpty() || password.isEmpty()) {
                // トーストでエラーメッセージを表示
                Toast.makeText(this, getString(R.string.error_empty_login_fields), Toast.LENGTH_SHORT).show()
            } else {
                // 入力内容をログに出力
                Log.d("LoginActivity", "Username: $username, Password: $password")

                // 仮のトークンとユーザーIDを作成
                val token = "sample_token_123"
                val userId = "12345" // 仮のユーザーID

                // SharedPreferencesに保存
                saveUserCredentials(username, token, userId)

                // MainActivityへ画面遷移
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }
    }

    // ユーザーネーム、トークン、ユーザーIDの保存
    private fun saveUserCredentials(username: String, token: String, userId: String) {
        // SharedPreferencesを取得
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // ユーザーネーム、トークン、ユーザーIDを保存
        editor.putString("username", username)
        editor.putString("token", token)
        editor.putString("userId", userId)
        editor.apply()

        // 保存したことをログに出力
        Log.d("LoginActivity", "Saved Username: $username, Token: $token, UserId: $userId")
    }
}
