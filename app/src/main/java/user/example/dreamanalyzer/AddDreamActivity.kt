package user.example.dreamanalyzer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AddDreamActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dream)
        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val txtResponse = findViewById<TextView>(R.id.txtResponse)
        val recordButton = findViewById<ImageButton>(R.id.recordButton)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        btnSubmit.setOnClickListener {
            val question = etQuestion.text.toString()
            Toast.makeText(this, question, Toast.LENGTH_SHORT).show()
            getResponse(question) { response ->
                runOnUiThread {
                    txtResponse.text = response
                }
            }
        }


        // Listener do nagrywania głosu
        recordButton.setOnClickListener {
            Toast.makeText(this, "Nagrywanie nie jest jeszcze dostępne!", Toast.LENGTH_SHORT).show()
        }

        // Obsługa dolnego paska nawigacyjnego
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, AddDreamActivity::class.java))
                    true
                }

                R.id.nav_calendar -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    true
                }

                R.id.nav_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java))
                    true
                }

                R.id.nav_tips -> {
                    startActivity(Intent(this, TipsActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = "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"
        val url = "https://api.openai.com/v1/chat/completions"

        val requestBody = """
        {
            "model": "gpt-3.5-turbo",
            "messages": [
                {
                    "role": "user",
                    "content": "$question"
                }
            ],
            "max_tokens": 500,
            "temperature": 0
        }
    """.trimIndent()

        Log.d("JSON Request", requestBody)

        val body = requestBody.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API Error", "Request failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val message = JSONObject(responseBody)
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    callback(message)
                } else {
                    Log.e("API Error", "Code: ${response.code}, Body: $responseBody")
                }
            }
        })
    }

}



