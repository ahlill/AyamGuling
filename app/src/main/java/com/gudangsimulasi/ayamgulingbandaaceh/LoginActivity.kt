package com.gudangsimulasi.ayamgulingbandaaceh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.ActivityLoginBinding
import com.gudangsimulasi.ayamgulingbandaaceh.util.Session

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val session = Session(this)
        binding.btnLogin.setOnClickListener {

            if (binding.etUsername.text.isBlank()) binding.etUsername.error = "Isi Username"
            if (binding.etPassword.text.isBlank()) binding.etPassword.error = "Isi Password"

            if (binding.etUsername.text.toString() == USERNAME && binding.etPassword.text.toString() == PASSWORD) {
                session.setLogin(true)
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(this, "Gagal Masuk", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val USERNAME = "ayam"
        private const val PASSWORD = "1234"

    }
}