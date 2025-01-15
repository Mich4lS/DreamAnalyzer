package user.example.dreamanalyzer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddDreamActivity : AppCompatActivity() {

    private lateinit var editDream: EditText
    private lateinit var saveDreamButton: Button
    private lateinit var recordButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dream)

        editDream = findViewById(R.id.editDream)
        saveDreamButton = findViewById(R.id.saveDreamButton)
        recordButton = findViewById(R.id.recordButton)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Listener do zapisu snu
        saveDreamButton.setOnClickListener {
            val dreamText = editDream.text.toString()
            if (dreamText.isNotBlank()) {
                Toast.makeText(this, "Sen został zapisany!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Sen nie może być pusty!", Toast.LENGTH_SHORT).show()
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
}

