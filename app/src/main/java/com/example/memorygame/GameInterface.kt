package com.example.memorygame

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone

class GameInterface : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_interface)

        val cards = listOf(
            Pair(findViewById<FrameLayout>(R.id.card1), findViewById<TextView>(R.id.card1Text)),
            Pair(findViewById<FrameLayout>(R.id.card2), findViewById<TextView>(R.id.card2Text)),
            Pair(findViewById<FrameLayout>(R.id.card3), findViewById<TextView>(R.id.card3Text)),
            Pair(findViewById<FrameLayout>(R.id.card4), findViewById<TextView>(R.id.card4Text)),
            Pair(findViewById<FrameLayout>(R.id.card5), findViewById<TextView>(R.id.card5Text)),
            Pair(findViewById<FrameLayout>(R.id.card6), findViewById<TextView>(R.id.card6Text)),
            Pair(findViewById<FrameLayout>(R.id.card7), findViewById<TextView>(R.id.card7Text)),
            Pair(findViewById<FrameLayout>(R.id.card8), findViewById<TextView>(R.id.card8Text)),
            Pair(findViewById<FrameLayout>(R.id.card9), findViewById<TextView>(R.id.card9Text)),
            Pair(findViewById<FrameLayout>(R.id.card10), findViewById<TextView>(R.id.card10Text)),
            Pair(findViewById<FrameLayout>(R.id.card11), findViewById<TextView>(R.id.card11Text)),
            Pair(findViewById<FrameLayout>(R.id.card12), findViewById<TextView>(R.id.card12Text))
        )

        for ((card, cardText) in cards) {
            card.setOnClickListener {
                val scale = applicationContext.resources.displayMetrics.density
                card.cameraDistance = 8000 * scale

                // animations
                val flipOut = AnimatorInflater.loadAnimator(applicationContext, R.animator.flip_out)
                val flipIn = AnimatorInflater.loadAnimator(applicationContext, R.animator.flip_in)

                flipOut.setTarget(card)
                flipIn.setTarget(card)

                flipOut.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (cardText.isGone) {
                            card.getChildAt(0).visibility = View.GONE
                            cardText.visibility = View.VISIBLE
                        } else {
                            card.getChildAt(0).visibility = View.VISIBLE
                            cardText.visibility = View.GONE
                        }
                        flipIn.start()
                    }
                })
                flipOut.start()
            }
        }
    }
}
