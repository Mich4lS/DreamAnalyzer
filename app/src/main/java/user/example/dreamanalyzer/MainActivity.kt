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

        val btnTellDream: ImageView = findViewById(R.id.btnTellDream)

        val pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse)
        btnTellDream.startAnimation(pulseAnimation)

        btnTellDream.setOnClickListener {
            startActivity(Intent(this, AddDreamActivity::class.java))
        }
    }
}



