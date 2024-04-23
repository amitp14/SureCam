package com.example.surecam

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.lock.AuthenticationCallback
import com.auth0.android.lock.Lock
import com.auth0.android.result.Credentials
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var lock: Lock
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val account = Auth0(this)
        // Create a reusable Lock instance
        lock = Lock.newBuilder(account, callback)
            // Customize Lock
            .closable(true).withScope("openid profile")
            .withScheme("@string/com_auth0_scheme")
            .build(this)

        findViewById<Button>(R.id.button_login).setOnClickListener { launchLock() }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release Lock resources
        lock.onDestroy(this)
    }

    private fun launchLock() {
        startActivity(lock.newIntent(this))
    }
    private val callback = object : AuthenticationCallback() {
        override fun onError(error: AuthenticationException) {


            Snackbar.make(
                findViewById(R.id.main),
                "Error : ${error.getCode()}",
                Snackbar.LENGTH_LONG
            ).show()

        }

        override fun onAuthentication(credentials: Credentials) {
            val next = Intent(this@MainActivity, TurboActivity::class.java)
            startActivity(next)
            finish()
        }
    }
}