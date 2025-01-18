package user.example.dreamanalyzer

import android.annotation.SuppressLint
import android.app.Dialog // Import klasy Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalendarActivity : AppCompatActivity() {

    private lateinit var dreamList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        dreamList = findViewById(R.id.dreamList)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Ustaw zaznaczenie dla ikonki Kalendarza
        bottomNavigation.selectedItemId = R.id.nav_calendar




        dreamList.layoutManager = LinearLayoutManager(this)


        // Obsługa dolnego paska nawigacyjnego
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, AddDreamActivity::class.java))
                    true
                }
                R.id.nav_calendar -> true // Nic nie robi, jesteśmy na tej stronie
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

