/*
ここはログインが必要か判断する
ログインが必要な場合はLoginActivity.kt
ログインが必要でない場合はHomeActivity.kt
ユーザーネーム、トークン、ユーザーIDがあるかどうかで判断する
 */

package com.example.hack_u_2024

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SharedPreferencesからデータを取得
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        val token = sharedPreferences.getString("token", null)
        val userId = sharedPreferences.getString("userId", null)

        // ユーザーネーム、トークン、ユーザーIDが存在するか確認
        if (username.isNullOrEmpty() || token.isNullOrEmpty() || userId.isNullOrEmpty()) {
            // データのいずれかがない場合、LoginActivityに遷移
            Log.d("MainActivity", "LoginActivityに遷移")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            // 全てのデータがある場合、HomeActivityに遷移
            Log.d("MainActivity", "HomeActivityに遷移")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // MainActivity自体を終了
        finish()
    }
}
