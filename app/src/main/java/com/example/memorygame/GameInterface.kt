package com.example.memorygame

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameInterface : AppCompatActivity() {

    private var flippedCards = mutableListOf<FrameLayout>()
    private var matchedPairs = 0
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_interface)

        val timerText = findViewById<TextView>(R.id.timerText)

        val images = mutableListOf(
            R.drawable.lace, R.drawable.hornet, R.drawable.shakra, R.drawable.sherma, R.drawable.garmond, R.drawable.trobbio
        )
        images.addAll(images) // Duplicate the images to create pairs
        images.shuffle() // Shuffle the images

        val cards = listOf(
            Pair(findViewById<FrameLayout>(R.id.card1), findViewById<ImageView>(R.id.card1Image)),
            Pair(findViewById<FrameLayout>(R.id.card2), findViewById<ImageView>(R.id.card2Image)),
            Pair(findViewById<FrameLayout>(R.id.card3), findViewById<ImageView>(R.id.card3Image)),
            Pair(findViewById<FrameLayout>(R.id.card4), findViewById<ImageView>(R.id.card4Image)),
            Pair(findViewById<FrameLayout>(R.id.card5), findViewById<ImageView>(R.id.card5Image)),
            Pair(findViewById<FrameLayout>(R.id.card6), findViewById<ImageView>(R.id.card6Image)),
            Pair(findViewById<FrameLayout>(R.id.card7), findViewById<ImageView>(R.id.card7Image)),
            Pair(findViewById<FrameLayout>(R.id.card8), findViewById<ImageView>(R.id.card8Image)),
            Pair(findViewById<FrameLayout>(R.id.card9), findViewById<ImageView>(R.id.card9Image)),
            Pair(findViewById<FrameLayout>(R.id.card10), findViewById<ImageView>(R.id.card10Image)),
            Pair(findViewById<FrameLayout>(R.id.card11), findViewById<ImageView>(R.id.card11Image)),
            Pair(findViewById<FrameLayout>(R.id.card12), findViewById<ImageView>(R.id.card12Image))
        )

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                timerText.text = "Time's up!"
                cards.forEach { (card, _) ->
                    card.isClickable = false
                }
                showGameOverPopup()
            }
        }.start()

        for ((index, cardData) in cards.withIndex()) {
            val (card, cardImage) = cardData
            cardImage.setImageResource(images[index])
            cardImage.tag = images[index]

            card.setOnClickListener {
                if (flippedCards.size < 2 && !flippedCards.contains(card)) {
                    flipCard(card)
                    flippedCards.add(card)

                    if (flippedCards.size == 2) {
                        checkForMatch()
                    }
                }
            }
        }
    }

    private fun flipCard(card: FrameLayout) {
        val scale = applicationContext.resources.displayMetrics.density
        card.cameraDistance = 8000 * scale

        val flipOut = AnimatorInflater.loadAnimator(applicationContext, R.animator.flip_out)
        val flipIn = AnimatorInflater.loadAnimator(applicationContext, R.animator.flip_in)

        flipOut.setTarget(card)
        flipIn.setTarget(card)

        val cardImage = card.getChildAt(1) as ImageView

        flipOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (cardImage.visibility == View.GONE) {
                    card.getChildAt(0).visibility = View.GONE
                    cardImage.visibility = View.VISIBLE
                } else {
                    card.getChildAt(0).visibility = View.VISIBLE
                    cardImage.visibility = View.GONE
                }
                flipIn.start()
            }
        })
        flipOut.start()
    }

    private fun checkForMatch() {
        val card1 = flippedCards[0]
        val card2 = flippedCards[1]
        val image1 = card1.getChildAt(1) as ImageView
        val image2 = card2.getChildAt(1) as ImageView

        if (image1.tag == image2.tag) {
            matchedPairs++
            card1.isClickable = false
            card2.isClickable = false
            image1.setBackgroundResource(R.drawable.card_front_correct)
            image2.setBackgroundResource(R.drawable.card_front_correct)
            flippedCards.clear()

            if (matchedPairs == 6) {
                timer.cancel()
                showWinPopup()
            }
        } else {
            image1.setBackgroundResource(R.drawable.card_front_wrong)
            image2.setBackgroundResource(R.drawable.card_front_wrong)

            val shake1 = AnimatorInflater.loadAnimator(applicationContext, R.animator.shake)
            shake1.setTarget(card1)
            shake1.start()

            val shake2 = AnimatorInflater.loadAnimator(applicationContext, R.animator.shake)
            shake2.setTarget(card2)
            shake2.start()

            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(500)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                card1.rotation = 0f
                card2.rotation = 0f
                flipCard(card1)
                flipCard(card2)
                image1.setBackgroundResource(R.drawable.card_front)
                image2.setBackgroundResource(R.drawable.card_front)
                flippedCards.clear()
            }, 1250)
        }
    }

    private fun showGameOverPopup() {
        val dialog = Dialog(this@GameInterface)
        dialog.setContentView(R.layout.popup_game_over)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val retryButton = dialog.findViewById<Button>(R.id.retryButton)
        retryButton.setOnClickListener {
            val intent = Intent(this@GameInterface, GameInterface::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }

    private fun showWinPopup() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_win)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val retryButton = dialog.findViewById<Button>(R.id.retryButtonWin)
        val exitButton = dialog.findViewById<Button>(R.id.exitButtonWin)

        retryButton.setOnClickListener {
            val intent = Intent(this, GameInterface::class.java)
            startActivity(intent)
            finish()
        }

        exitButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
}
