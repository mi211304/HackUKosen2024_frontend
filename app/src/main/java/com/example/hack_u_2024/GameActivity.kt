
package com.example.hack_u_2024

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // ゲーム開始のトーストを表示
        Toast.makeText(this, "ゲーム開始", Toast.LENGTH_SHORT).show()

        // 5秒後にダイアログを表示してHomeActivityに遷移
        Handler(Looper.getMainLooper()).postDelayed({
            showGameEndDialog()
        }, 5000) // 5秒後
    }

    private fun showGameEndDialog() {
        // ゲーム終了ダイアログ
        AlertDialog.Builder(this)
            .setTitle("ゲーム終了")
            .setMessage("ゲームが終了しました。")
            .setPositiveButton("OK") { _, _ ->
                // HomeActivityへ遷移
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish() // GameActivityを終了
            }
            .setCancelable(false) // キャンセル不可
            .show()
    }
}
