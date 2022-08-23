package com.gudangsimulasi.ayamgulingbandaaceh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.ActivitySplashScreenBinding
import com.gudangsimulasi.ayamgulingbandaaceh.util.Session

class SplashScreenActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val session = Session(this)

        Handler().postDelayed({
            if (session.isLogin()){
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 2500)


    }
}