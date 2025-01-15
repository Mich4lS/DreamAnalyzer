package user.example.dreamanalyzer

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
    private lateinit var adapter: DreamAdapter  // Adapter jest teraz właściwością klasy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        dreamList = findViewById(R.id.dreamList)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Ustaw zaznaczenie dla ikonki Kalendarza
        bottomNavigation.selectedItemId = R.id.nav_calendar

        // Dane do RecyclerView
        val dreams = mutableListOf(
            Dream("12.01", "Śniło mi się, że lecę samolotem nad górami..."),
            Dream("13.01", "Byłem na pięknej wyspie z palmami..."),
            Dream("14.01", "Widziałem niezwykłe kolory na niebie...")
        )

        // Inicjalizacja adaptera
        adapter = DreamAdapter(dreams) { dream, position ->
            showDreamDetailsDialog(dream, dreams, position)
        }
        dreamList.layoutManager = LinearLayoutManager(this)
        dreamList.adapter = adapter

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

    private fun showDreamDetailsDialog(
        dream: Dream,
        dreams: MutableList<Dream>,
        position: Int
    ) {
        // Tworzenie dialogu
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_dream_details, null)
        dialog.setContentView(view)

        val dreamDetails = view.findViewById<TextView>(R.id.dreamDetails)
        val deleteButton = view.findViewById<Button>(R.id.deleteDreamButton)
        dreamDetails.text = dream.details

        // Usunięcie snu
        deleteButton.setOnClickListener {
            dreams.removeAt(position)
            adapter.notifyItemRemoved(position)
            dialog.dismiss()
        }

        // Ustawienie rozmiaru i wyświetlenie dialogu
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            (resources.displayMetrics.heightPixels * 0.6).toInt()
        )
        dialog.show()
    }
}

