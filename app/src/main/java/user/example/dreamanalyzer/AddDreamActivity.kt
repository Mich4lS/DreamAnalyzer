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
import java.text.SimpleDateFormat
import java.util.Locale
import android.text.method.ScrollingMovementMethod

class AddDreamActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    companion object {
        val dreamsList = mutableListOf<Dream>() // Lista przechowująca sny
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dream)
        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val txtResponse = findViewById<TextView>(R.id.txtResponse)
        val recordButton = findViewById<ImageButton>(R.id.recordButton)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        btnSubmit.setOnClickListener {
            val userInput = etQuestion.text.toString()
            val question = "Zinterpretuj ten sen oraz powiedz co może oznaczać: $userInput"

            getResponse(question) { response ->
                runOnUiThread {
                    txtResponse.text = response

                    // Zapisz nowy sen w liście
                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
                    val dream = Dream(currentDate, userInput, response)
                    dreamsList.add(dream)

                    // Wczytaj istniejącą listę snów
                    val dreams = loadDreamsFromPreferences(this)

                    // Dodaj nowy sen do listy
                    dreams.add(dream)

                    // Zapisz zaktualizowaną listę
                    saveDreamsToPreferences(this, dreams)

                    Toast.makeText(this, "Sen zapisany!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        txtResponse.movementMethod = ScrollingMovementMethod()

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
        val apiKey = BuildConfig.OPENAI_API_KEY

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



