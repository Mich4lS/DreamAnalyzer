package user.example.dreamanalyzer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddDreamActivity : AppCompatActivity() {

    private lateinit var editDream: EditText
    private lateinit var saveDreamButton: Button
    private lateinit var recordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dream)

        editDream = findViewById(R.id.editDream)
        saveDreamButton = findViewById(R.id.saveDreamButton)
        recordButton = findViewById(R.id.recordButton)

        // Listener do zapisu snu
        saveDreamButton.setOnClickListener {
            val dreamText = editDream.text.toString()
            if (dreamText.isNotBlank()) {
                // TODO: Zapisz sen lokalnie lub prześlij do API
                Toast.makeText(this, "Sen został zapisany!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Sen nie może być pusty!", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener do nagrywania głosu
        recordButton.setOnClickListener {
            // TODO: Dodaj funkcję nagrywania dźwięku
            Toast.makeText(this, "Nagrywanie nie jest jeszcze dostępne!", Toast.LENGTH_SHORT).show()
        }
    }
}
