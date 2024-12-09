package com.example.hack_u_2024

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LoadingActivity : AppCompatActivity() {

    private lateinit var loadingTextView: TextView
    private lateinit var webSocket: WebSocket
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // UI要素を取得
        loadingTextView = findViewById(R.id.loadingTextView)

        // SharedPreferencesからユーザー情報を取得
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "No UserID") ?: "No UserID"

        // WebSocket通信を開始
        initializeWebSocket(userId)
    }

    private fun initializeWebSocket(userId: String) {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://153.121.33.221:8080/ws/matching") // WebSocket URL
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "接続成功")
                runOnUiThread {
                    loadingTextView.text = "サーバーに接続しました"
                }

                // ユーザーIDをJSON形式でサーバーに送信
                val message = JSONObject().put("user-id", userId).toString()
                webSocket.send(message)
                Log.d("WebSocket", "送信メッセージ: $message")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "メッセージ受信: $text")

                // サーバーからの応答を処理
                processServerResponse(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("WebSocket", "バイナリメッセージ受信: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "接続終了: $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "エラー: ${t.message}")
                runOnUiThread {
                    loadingTextView.text = "接続エラー"
                }
            }
        })

        client.dispatcher.executorService.shutdown()
    }

    private fun processServerResponse(response: String) {
        try {
            val jsonResponse = JSONObject(response)
            val roomId = jsonResponse.getInt("room-id")
            val players = jsonResponse.getJSONArray("players")

            // ログ出力（デバッグ用）
            Log.d("WebSocket", "ルームID: $roomId")
            for (i in 0 until players.length()) {
                val player = players.getJSONObject(i)
                val userId = player.getInt("user-id")
                val cards = player.getJSONArray("cards")
                for (j in 0 until cards.length()) {
                    val card = cards.getJSONObject(j)
                    val cardId = card.getInt("card-id")
                    val name = card.getString("name")
                    val attribute = card.getString("attribute")
                    val picture = card.getString("picture")

                    Log.d("WebSocket", "カード情報: ID=$cardId, 名前=$name, 属性=$attribute, 画像=$picture")
                }
            }

            navigateToGameActivity(roomId, players)

        } catch (e: Exception) {
            Log.e("WebSocket", "JSON解析エラー: ${e.message}")
        }
    }

    private fun navigateToGameActivity(roomId: Int, players: Any) {
        // Intentにデータを詰めてGameActivityに遷移
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("room-id", roomId)
        intent.putExtra("players", players.toString()) // 必要に応じてJSON文字列を渡す
        startActivity(intent)
        finish() // LoadingActivityを終了
    }

    override fun onDestroy() {
        super.onDestroy()
        // WebSocket接続を閉じる
        if (::webSocket.isInitialized) {
            webSocket.close(1000, "アクティビティ終了")
        }
    }
}
