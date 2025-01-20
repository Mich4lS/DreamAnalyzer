package user.example.dreamanalyzer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class AddDreamActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val MICROPHONE_PERMISSION_CODE = 100

    // Rozpoznawanie mowy
    private val speechRecognizerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!matches.isNullOrEmpty()) {
                    val spokenText = matches[0]
                    val etQuestion = findViewById<EditText>(R.id.etQuestion)
                    etQuestion.setText(spokenText)
                }
            }
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

                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
                    val dream = Dream(currentDate, userInput, response)

                    val dreams = loadDreamsFromPreferences()
                    dreams.add(0, dream)
                    saveDreamsToPreferences(dreams)

                    Toast.makeText(this, "Sen zapisany!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recordButton.setOnClickListener {
            requestMicrophonePermission()
        }

        // Obsługa dolnego paska nawigacyjnego
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
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

    private fun requestMicrophonePermission() {
        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                MICROPHONE_PERMISSION_CODE
            )
        } else {
            startSpeechToText()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MICROPHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienie zostało przyznane
                startSpeechToText()
            } else {
                // Uprawnienie zostało odrzucone
                Toast.makeText(this, "Musisz zezwolić na dostęp do mikrofonu, aby używać tej funkcji", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Powiedz coś, aby zostało przekształcone w tekst")

        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Rozpoznawanie mowy nie jest obsługiwane na tym urządzeniu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = BuildConfig.OPENAI_API_KEY

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
                "temperature": 0.7
            }
        """.trimIndent()

        val body = requestBody.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AddDreamActivity, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                }
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
                    runOnUiThread {
                        Toast.makeText(this@AddDreamActivity, "Błąd API: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun saveDreamsToPreferences(dreams: MutableList<Dream>) {
        val sharedPreferences = getSharedPreferences("DreamPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(dreams)
        editor.putString("dreamList", json)
        editor.apply()
    }

    private fun loadDreamsFromPreferences(): MutableList<Dream> {
        val sharedPreferences = getSharedPreferences("DreamPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("dreamList", null)

        return if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Dream>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
