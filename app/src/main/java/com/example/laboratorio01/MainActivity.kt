package com.example.laboratorio01

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        startButton = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            val playerName = nameEditText.text.toString().trim()

            if (isValidName(playerName)) {
                hideKeyboard()
                clearNameField()

                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("PLAYER_NAME", playerName)
                startActivity(intent)
            } else {
                Toast.makeText(this, getValidationErrorMessage(playerName), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && name.length <= 10 && !name[0].isDigit()
    }

    private fun getValidationErrorMessage(name: String): String {
        return when {
            name.isEmpty() -> getString(R.string.invalid_name)
            name.length > 10 -> "El nombre debe tener un máximo de 10 caracteres."
            name[0].isDigit() -> "El nombre no puede comenzar con un número."
            else -> getString(R.string.invalid_name)
        }
    }

    private fun clearNameField() {
        nameEditText.text.clear()
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
