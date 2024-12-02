/*
ここではゲームのマッチングの待機をする
レイアウトはactivity_loading.xmlを使用
 */

package com.example.hack_u_2024

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // UI要素を取得
        val loadingTextView: TextView = findViewById(R.id.loadingTextView)

        // SharedPreferencesからユーザー情報を取得
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "No Username")
        val token = sharedPreferences.getString("token", "No Token")
        val userId = sharedPreferences.getString("userId", "No UserID")

        // ユーザー情報をログに出力
        Log.d("LoadingActivity", "Username: $username, Token: $token, UserID: $userId")

        // ゲーミング風に色を変える
        val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN)
        val handler = Handler(Looper.getMainLooper())
        var colorIndex = 0
        val colorChangeTask = object : Runnable {
            override fun run() {
                loadingTextView.setTextColor(colors[colorIndex])
                colorIndex = (colorIndex + 1) % colors.size
                handler.postDelayed(this, 100) // 500msごとに色を変更
            }
        }
        handler.post(colorChangeTask)

        // 5秒後にGameActivityへ遷移
        handler.postDelayed({
            // 色変更のループを停止
            handler.removeCallbacks(colorChangeTask)

            // GameActivityに遷移
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish() // LoadingActivityを終了
        }, 5000) // 5秒待つ
    }
}
