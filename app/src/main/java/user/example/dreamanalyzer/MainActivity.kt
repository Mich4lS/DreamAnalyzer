package user.example.dreamanalyzer

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Znajdź ImageView
        val btnTellDream: ImageView = findViewById(R.id.btnTellDream)

        // Załaduj animację pulsowania
        val pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse)
        btnTellDream.startAnimation(pulseAnimation)

        // Obsługa kliknięcia przycisku
        btnTellDream.setOnClickListener {
            startActivity(Intent(this, AddDreamActivity::class.java))
        }
    }
}



