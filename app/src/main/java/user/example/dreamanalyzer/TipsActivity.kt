package user.example.dreamanalyzer

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class TipsActivity : AppCompatActivity() {

    private val tips = listOf(
        "Pełnia księżyca wpływa na sen – śpimy wtedy płycej i krócej.",
        "Nasz sen składa się z 4 powtarzających się cykli.",
        "Istnieje strach przed zasypianiem i nazywa się somnifobia.",
        "Śnimy jedynie o tych twarzach, które gdzieś już kiedyś widzieliśmy.",
        "Noworodki potrzebują aż 16 godzin snu.",
        "Sen wpływa na zdolność uczenia się i pamięć."
    )

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        val frames = listOf(
            findViewById<FrameLayout>(R.id.tip1),
            findViewById<FrameLayout>(R.id.tip2),
            findViewById<FrameLayout>(R.id.tip3),
            findViewById<FrameLayout>(R.id.tip4),
            findViewById<FrameLayout>(R.id.tip5),
            findViewById<FrameLayout>(R.id.tip6)
        )

        val images = listOf(
            R.drawable.photo1,
            R.drawable.photo2,
            R.drawable.photo3,
            R.drawable.photo4,
            R.drawable.photo5,
            R.drawable.photo6
        )

        frames.forEachIndexed { index, frame ->
            var isFlipped = false
            frame.setOnClickListener {
                val animation = AnimatorInflater.loadAnimator(this, R.anim.page_flip) as AnimatorSet
                animation.setTarget(frame)

                animation.start()

                frame.postDelayed({
                    frame.removeAllViews()
                    if (!isFlipped) {
                        val textView = TextView(this).apply {
                            text = tips[index]
                            textSize = 20f
                            setTextColor(ContextCompat.getColor(this@TipsActivity, android.R.color.white))
                            gravity = Gravity.CENTER
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                            )
                        }
                        frame.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
                        frame.addView(textView)
                    } else {
                        val imageView = ImageView(this).apply {
                            setImageResource(images[index])
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                            )
                        }
                        frame.setBackgroundResource(R.drawable.tip_background)
                        frame.addView(imageView)
                    }
                    isFlipped = !isFlipped
                }, 300) // Dostosuj opóźnienie do czasu animacji
            }
        }

        // Dodanie obsługi dolnego paska nawigacyjnego
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)
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
                    // Obecnie jesteśmy na stronie Tips
                    true
                }
                else -> false
            }
        }

        // Ustawienie aktywnej ikony w dolnym pasku
        bottomNavigation.selectedItemId = R.id.nav_tips
    }
}




