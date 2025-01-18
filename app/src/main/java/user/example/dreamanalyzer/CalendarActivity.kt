package user.example.dreamanalyzer

import android.content.Intent
import android.os.Bundle
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

        // Wczytaj sny z SharedPreferences
        val dreams = loadDreamsFromPreferences(this)

        // Ustaw adapter dla RecyclerView
        dreamList.layoutManager = LinearLayoutManager(this)
        dreamList.adapter = DreamAdapter(dreams) { dream ->
            // Obsługa kliknięcia elementu
            val intent = Intent(this, DreamDetailActivity::class.java)
            intent.putExtra("EXTRA_DATE", dream.date)
            intent.putExtra("EXTRA_DREAM", dream.dreamText)
            intent.putExtra("EXTRA_INTERPRETATION", dream.interpretation)
            startActivity(intent)
        }
        // Ustaw wybrany element nawigacji
        bottomNavigation.selectedItemId = R.id.nav_calendar

        // Obsługa dolnego paska nawigacyjnego
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, AddDreamActivity::class.java))
                    true
                }
                R.id.nav_calendar -> true
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


