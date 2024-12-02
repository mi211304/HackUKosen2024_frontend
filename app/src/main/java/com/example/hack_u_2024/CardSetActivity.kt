/*
ここではデッキ制作を行う
画面はactivity_card_set.xmlを使用
 */
package com.example.hack_u_2024

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CardSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_set)

        // UI要素を取得
        val backButton: Button = findViewById(R.id.backButton)

        // 戻るボタンのクリックイベント
        backButton.setOnClickListener {
            // HomeActivity へ遷移
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // 現在のアクティビティを終了
        }
    }
}
