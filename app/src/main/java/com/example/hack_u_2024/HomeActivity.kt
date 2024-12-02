package com.example.hack_u_2024

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // UI要素を取得
        val usernameTextView: TextView = findViewById(R.id.usernameTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val gameStartButton: Button = findViewById(R.id.gameStartButton)
        val deckCreationButton: Button = findViewById(R.id.deckCreationButton)
        val captureButton: Button = findViewById(R.id.captureButton)

        // SharedPreferencesからユーザーネームを取得して表示
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "No Username")
        usernameTextView.text = "ようこそ、$username さん！"

        // ログアウトボタンのクリックイベント
        logoutButton.setOnClickListener {
            Log.d("HomeActivity", "Logout clicked")

            // SharedPreferencesをクリア
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // LoginActivityへ遷移
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // HomeActivityを終了
            finish()
        }

        // ゲームスタートボタンのクリックイベント
        gameStartButton.setOnClickListener {
            Log.d("HomeActivity", "Game Start clicked")

            // LoadingActivityへ遷移
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }

        // デッキ作成ボタンのクリックイベント
        deckCreationButton.setOnClickListener {
            Log.d("HomeActivity", "Deck Creation clicked")

            // CardSetActivityへ遷移
            val intent = Intent(this, CardSetActivity::class.java)
            startActivity(intent)
        }

        // ポチ袋キャプチャーボタンのクリックイベント
        captureButton.setOnClickListener {
            Log.d("HomeActivity", "Capture clicked")

            // CaptureActivityへ遷移
            val intent = Intent(this, CaptureActivity::class.java)
            startActivity(intent)
        }
    }
}
