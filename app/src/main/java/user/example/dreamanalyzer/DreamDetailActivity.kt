package user.example.dreamanalyzer

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DreamDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dream_detail)

        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvDream = findViewById<TextView>(R.id.tvDream)
        val tvInterpretation = findViewById<TextView>(R.id.tvInterpretation)
        val btnClose = findViewById<ImageButton>(R.id.btnClose)

        // Odbierz dane przekazane z CalendarActivity
        val date = intent.getStringExtra("EXTRA_DATE")
        val dream = intent.getStringExtra("EXTRA_DREAM")
        val interpretation = intent.getStringExtra("EXTRA_INTERPRETATION")

        // Ustaw dane w widokach
        tvDate.text = date
        tvDream.text = dream
        tvInterpretation.text = interpretation

        // Zamknij aktywność po kliknięciu przycisku
        btnClose.setOnClickListener {
            finish()
        }
    }
}
