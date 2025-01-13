package user.example.dreamanalyzer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicjalizacja dolnego paska nawigacyjnego
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Listener dolnego paska nawigacyjnego
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_calendar -> {
                    // Przejście do aktywności kalendarza
                    startActivity(Intent(this, CalendarActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Przycisk do otwierania aktywności "Opowiedz mi swój sen"
        findViewById<Button>(R.id.btnTellDream).setOnClickListener {
            startActivity(Intent(this, AddDreamActivity::class.java))
        }
    }
}

