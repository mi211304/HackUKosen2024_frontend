/*
ここではデッキ制作を行う
画面はactivity_card_set.xmlを使用
 */
package com.example.hack_u_2024

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

import android.widget.Toast
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import coil.load
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.GsonBuilder



class CardSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_set)

        data class Card(
            val attribute: String,
            val cardId: Int // JSONの"card-id"に対応
        )

        data class SelectedCards(
            val selectedCards: MutableList<Card> = mutableListOf()
        )

        val data = SelectedCards()

        fun addCard(data: SelectedCards, attribute: String, cardId: Int) {
            val newCard = Card(attribute, cardId)
            data.selectedCards.add(newCard)
        }

        val gson = GsonBuilder().setPrettyPrinting().create()

        val attributes = listOf(
            findViewById<ImageButton>(R.id.attribute1),
            findViewById<ImageButton>(R.id.attribute2),
            findViewById<ImageButton>(R.id.attribute3),
            findViewById<ImageButton>(R.id.attribute4),
            findViewById<ImageButton>(R.id.attribute5),
            findViewById<ImageButton>(R.id.attribute6)
        )

        val card1: ImageButton = findViewById(R.id.card1)
        val card2: ImageButton = findViewById(R.id.card2)
        val card3: ImageButton = findViewById(R.id.card3)
        val card4: ImageButton = findViewById(R.id.card4)
        val card5: ImageButton = findViewById(R.id.card5)
        val card6: ImageButton = findViewById(R.id.card6)

        val cards = listOf(card1, card2, card3, card4, card5, card6)

        val hopUp: View = findViewById(R.id.hopUp)
        val textView: TextView = findViewById(R.id.textView)
        val decideButtonUp: Button = findViewById(R.id.decideButton_up)
        val backButtonUp: Button = findViewById(R.id.backButton_up)

        val downBlack: View = findViewById(R.id.downBlack)
        val upBlack: View = findViewById(R.id.upBlack)
        val first: ImageView = findViewById(R.id.first)
        val second: ImageView = findViewById(R.id.second)

        val circles = listOf(
            findViewById<ImageView>(R.id.circle1),
            findViewById<ImageView>(R.id.circle2),
            findViewById<ImageView>(R.id.circle3),
            findViewById<ImageView>(R.id.circle4),
            findViewById<ImageView>(R.id.circle5),
            findViewById<ImageView>(R.id.circle6)
        )
        val borders = listOf(
            findViewById<ImageView>(R.id.border1),
            findViewById<ImageView>(R.id.border2),
            findViewById<ImageView>(R.id.border3),
            findViewById<ImageView>(R.id.border4),
            findViewById<ImageView>(R.id.border5),
            findViewById<ImageView>(R.id.border6)
        )

        val baseUrl = Uri.Builder()
            .scheme("http")
            .encodedAuthority("153.121.33.221:8080")
            .path("cards")
            .build()
            .toString()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("CardSetActivity", "Request URL: $baseUrl")
                val response = get(baseUrl)

                val jsonResponse = JSONObject(response)
                val cardsArray = jsonResponse.optJSONArray("Cards")

                if (cardsArray != null && cardsArray.length() > 0) {
                    val firstCard = cardsArray.getJSONObject(0)
                    val secondCard = cardsArray.getJSONObject(1)
                    val thirdCard = cardsArray.getJSONObject(2)
                    val fourCard = cardsArray.getJSONObject(3)
                    val fiveCard = cardsArray.getJSONObject(4)
                    val sixCard = cardsArray.getJSONObject(5)

                    val pictureUrl1 = firstCard.optString("picture")
                    val pictureUrl2 = secondCard.optString("picture")
                    val pictureUrl3 = thirdCard.optString("picture")
                    val pictureUrl4 = fourCard.optString("picture")
                    val pictureUrl5 = fiveCard.optString("picture")
                    val pictureUrl6 = sixCard.optString("picture")

                    card1.load(pictureUrl1)
                    card2.load(pictureUrl2)
                    card3.load(pictureUrl3)
                    card4.load(pictureUrl4)
                    card5.load(pictureUrl5)
                    card6.load(pictureUrl6)
                } else {
                    Log.w("CardSetActivity", "Cards array is empty or missing in response: $response")
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Network error occurred: ${e.message}", e) // エラー時のログ
                Toast.makeText(this@CardSetActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // UI要素を取得
        val backButton: Button = findViewById(R.id.backButton)
        // 戻るボタンのクリックイベント
        backButton.setOnClickListener {
            // HomeActivity へ遷移
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // 現在のアクティビティを終了
        }

        var attributeClicked = false
        var flag = false
        var flagSecond = false
        var number = 0

        first.visibility = ImageView.VISIBLE
        downBlack.visibility = View.VISIBLE

        for ((index, button) in attributes.withIndex()) {
            Log.d("CardSetActivity", "first")
            button.setOnClickListener {
                Log.d("CardSetActivity", "Button clicked")
                if (circles[index].visibility == View.VISIBLE) {
                    circles[index].visibility = View.GONE
                } else {
                    circles[index].visibility = View.VISIBLE
                }

                if (flag == false) {
                    first.visibility = ImageView.GONE
                    downBlack.visibility = View.GONE

                    second.visibility = ImageView.VISIBLE
                    upBlack.visibility = View.VISIBLE

                    flag=true
                }

                // attributeClicked の状態をトグル
                attributeClicked = !attributeClicked
                Log.d("CardSetActivity", "Attribute clicked, new state: $attributeClicked")
                //サークルのindexの情報を渡せば組み合わせを実装できる
                number = index
                Log.d("CardSetActivity", "number: $number")
            }
        }

        for ((index, button) in cards.withIndex()) {
            button.setOnClickListener {
                if (flagSecond == false) {
                    second.visibility = ImageView.GONE
                    upBlack.visibility = ImageView.GONE

                    flagSecond = true
                }

                Log.d("CardSetActivity", "Button clicked")
                if (attributeClicked) {
                    // attributeClicked が true の場合、対応するbordersのvisibilityをトグル
                    if (borders[index].visibility == View.VISIBLE) {
                        Log.d("CardSetActivity", "now")
                        borders[index].visibility = View.GONE
                        attributeClicked = !attributeClicked
                    } else {
                        Log.d("CardSetActivity", "Circle isEnabled: ${circles[number].isEnabled}")
                        borders[index].visibility = View.VISIBLE
                        attributes[number].isClickable = false

                        attributeClicked = !attributeClicked
                        Log.d("CardSetActivity", "Circle isEnabled: ${circles[number].isEnabled}")
                    }
                }
            }
        }

        val decideButton: Button = findViewById(R.id.decideButton)

        decideButton.setOnClickListener {
            hopUp.visibility = View.VISIBLE
            textView.visibility = TextView.VISIBLE
            decideButtonUp.visibility = Button.VISIBLE
            backButtonUp.visibility = Button.VISIBLE

            backButtonUp.setOnClickListener {
                hopUp.visibility = View.GONE
                textView.visibility = View.GONE
                decideButtonUp.visibility = Button.GONE
                backButtonUp.visibility = Button.GONE
            }

            decideButtonUp.setOnClickListener {
                // HomeActivity へ遷移
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish() // 現在のアクティビティを終了
            }
        }
    }


    private suspend fun get(url: String): String = withContext(Dispatchers.IO) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Authorization", "Bearer $token")
        }

        try {
            val responseCode = connection.responseCode
            Log.d("CardSetActivity", "Server responded with status code: $responseCode") // レスポンスコードを記録
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
