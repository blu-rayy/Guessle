package com.example.memorygame

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameInterface : AppCompatActivity() {

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

        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                timerText.text = "Time's up!"
                cards.forEach { (card, _) ->
                    card.isClickable = false
                }

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
        }.start()

        for ((index, cardData) in cards.withIndex()) {
            val (card, cardImage) = cardData
            cardImage.setImageResource(images[index])
            card.setOnClickListener {
                val scale = applicationContext.resources.displayMetrics.density
                card.cameraDistance = 8000 * scale

                val flipOut = AnimatorInflater.loadAnimator(applicationContext, R.animator.flip_out)
                val flipIn = AnimatorInflater.loadAnimator(applicationContext, R.animator.flip_in)

                flipOut.setTarget(card)
                flipIn.setTarget(card)

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
        }
    }
}
