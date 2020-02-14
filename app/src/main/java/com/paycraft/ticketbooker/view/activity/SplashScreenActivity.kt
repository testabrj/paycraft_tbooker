package com.paycraft.ticketbooker.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.paycraft.ticketbooker.R

class SplashScreenActivity : AppCompatActivity() {

    private var mAppLogo: TextView? = null
    private var anim: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mAppLogo = findViewById(R.id.app_logo)
        startAnimation()

    }

    private fun startAnimation() {
        anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in) // Create the animation.
        anim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mAppLogo!!.startAnimation(anim)

    }


}
