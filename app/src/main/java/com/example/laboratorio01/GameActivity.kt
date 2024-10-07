package com.example.laboratorio01

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private lateinit var playerNameTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var targetImageView: ImageView
    private lateinit var imageButtons: List<ImageButton>

    private var playerName: String = ""
    private var score: Int = 0
    private var timeRemaining: Int = 75
    private var currentRound: Int = 1
    private var maxRounds: Int = 2

    private val images = listOf(
        R.drawable.imagen1, R.drawable.imagen2, R.drawable.imagen3,
        R.drawable.imagen4, R.drawable.imagen5, R.drawable.imagen6, R.drawable.imagen7
    )
    private var currentTarget: Int = 0

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        playerName = intent.getStringExtra("PLAYER_NAME") ?: ""

        initializeViews()
        setupGame()
        startTimer()
    }

    private fun initializeViews() {
        playerNameTextView = findViewById(R.id.playerNameTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        timeTextView = findViewById(R.id.timeTextView)
        targetImageView = findViewById(R.id.targetImageView)

        imageButtons = listOf(
            findViewById(R.id.imageButton1),
            findViewById(R.id.imageButton2),
            findViewById(R.id.imageButton3),
            findViewById(R.id.imageButton4),
            findViewById(R.id.imageButton5),
            findViewById(R.id.imageButton6),
            findViewById(R.id.imageButton7),
            findViewById(R.id.imageButton8),
            findViewById(R.id.imageButton9),
            findViewById(R.id.imageButton10),
            findViewById(R.id.imageButton11),
            findViewById(R.id.imageButton12),
            findViewById(R.id.imageButton13),
            findViewById(R.id.imageButton14),
            findViewById(R.id.imageButton15),
            findViewById(R.id.imageButton16),
            findViewById(R.id.imageButton17),
            findViewById(R.id.imageButton18),
            findViewById(R.id.imageButton19),
            findViewById(R.id.imageButton20)
        )
        playerNameTextView.text = getString(R.string.player_name, playerName)
        updateScoreDisplay()
    }

    private fun setupGame() {
        currentTarget = images.random()
        targetImageView.setImageResource(currentTarget)

        val allImages = images.shuffled().toMutableList()

        val repeatedImages = mutableListOf<Int>()
        while (repeatedImages.size < 20) {
            repeatedImages.addAll(allImages)
        }

        val finalImages = repeatedImages.shuffled().take(20)

        imageButtons.forEachIndexed { index, button ->
            button.setImageResource(R.drawable.pattern)
            button.isClickable = true
            button.setBackgroundColor(Color.TRANSPARENT)

            button.setOnClickListener {
                revealImage(button, finalImages[index])
            }
        }
    }

    private fun revealImage(button: ImageButton, image: Int) {
        button.setImageResource(image)
        button.isClickable = false

        if (image == currentTarget) {
            score += 100
            if (currentRound < maxRounds) {
                currentRound++
                setupGame()
            } else {
                endGame()
            }
        } else {
            score -= 10
            button.setBackgroundColor(Color.RED)
        }
        updateScoreDisplay()
    }

    private fun updateScoreDisplay() {
        scoreTextView.text = getString(R.string.score, score)
    }

    private fun startTimer() {
        timer = object : CountDownTimer((timeRemaining * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = (millisUntilFinished / 1000).toInt()
                val minutes = timeRemaining / 60
                val seconds = timeRemaining % 60
                timeTextView.text = getString(R.string.time_remaining, minutes, seconds)
            }

            override fun onFinish() {
                endGame()
            }
        }.start()
    }

    private fun endGame() {
        timer.cancel()
        showEndGameDialog()
    }

    @SuppressLint("StringFormatInvalid")
    private fun showEndGameDialog() {
        val message = getString(R.string.end_game_message, score)
        AlertDialog.Builder(this)
            .setTitle(R.string.game_over)
            .setMessage(message)
            .setPositiveButton(R.string.play_again) { _, _ ->
                restartGame()
            }
            .setNegativeButton(R.string.exit) { _, _ ->
                finish()
            }
            .show()
    }

    private fun restartGame() {
        score = 0
        currentRound = 1
        timeRemaining = 75
        updateScoreDisplay()
        setupGame()
        startTimer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_2_rounds -> maxRounds = 2
            R.id.menu_3_rounds -> maxRounds = 3
            R.id.menu_5_rounds -> maxRounds = 5
            R.id.menu_change_background_blue -> changeBackgroundColor(Color.BLUE)
            R.id.menu_change_background_green -> changeBackgroundColor(Color.GREEN)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeBackgroundColor(color: Int) {
        findViewById<ImageView>(R.id.gameLayout).setBackgroundColor(color)
    }
}
