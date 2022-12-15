package com.example.wagba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerTxt = findViewById<TextView>(R.id.loginRegisterText)
        registerTxt.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java )
            startActivity(intent)
        }
    }
}