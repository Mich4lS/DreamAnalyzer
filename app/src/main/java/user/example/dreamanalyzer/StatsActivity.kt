package user.example.dreamanalyzer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class StatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        bottomNavigation.selectedItemId = R.id.nav_stats

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
                R.id.nav_stats -> true
                R.id.nav_tips -> {
                    startActivity(Intent(this, TipsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
