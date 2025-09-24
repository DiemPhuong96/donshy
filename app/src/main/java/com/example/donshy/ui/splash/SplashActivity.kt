package com.example.donshy.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.donshy.databinding.SplashActivityBinding
import com.example.donshy.ui.MainActivity
import com.example.donshy.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : ComponentActivity() {
    private lateinit var binding: SplashActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val progression = binding.lottieView
        progression.speed = 6F
        progression.playAnimation()
        progression.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                //Not yet start
            }

            override fun onAnimationEnd(p0: Animator) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                }
                finish()
            }

            override fun onAnimationCancel(p0: Animator) {
                //Not yet start
            }

            override fun onAnimationRepeat(p0: Animator) {
                //Not yet start
            }

        })
    }
}
