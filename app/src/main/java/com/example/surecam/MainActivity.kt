package com.example.surecam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.lock.AuthenticationCallback
import com.auth0.android.lock.Lock
import com.auth0.android.result.Credentials
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.surecam.util.BASE_URL_login_with_app
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var lock: Lock
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(SessionManager(this@MainActivity).isLoggedIn()){
            var email  = SessionManager(this@MainActivity).getUserEmail()

            BASE_URL_login_with_app = SessionManager(this@MainActivity).getCurrentUrl().toString()
            //BASE_URL_login_with_app = "$BASE_URL_login_with_app/$email"

            val next = Intent(this@MainActivity, TurboActivity::class.java)
            next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(next)

        }


        this.cacheDir.deleteRecursively()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById(R.id.toolbar)) // Use the custom toolbar as the ActionBar

        supportActionBar?.apply {
            title = ""
            // Other customization options like setting icons, enabling home button, etc.
        }

        account = Auth0(this)
        // Create a reusable Lock instance
        lock = Lock.newBuilder(account, callback)
            // Customize Lock
            .closable(true).withScope("openid profile email read:current_user update:current_user_metadata")
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
            cachedCredentials = credentials

            val idToken: String = credentials.idToken

            try {
                val decodedJwt = decodeIdToken(idToken)
                // Access decoded JWT claims
                val userId = decodedJwt.subject
                val emailClaim = decodedJwt.getClaim("email")
                val name = decodedJwt.getClaim("name").asString()

                val email = if (emailClaim.isNull) {
                    // Handle case where email claim is null
                    null
                } else {
                    emailClaim.asString()
                }

                if (!email.isNullOrEmpty()) {
                    BASE_URL_login_with_app = "$BASE_URL_login_with_app/$email"
                }

                SessionManager(this@MainActivity).setUserId(userId)
                SessionManager(this@MainActivity).setUserEmail(email.toString())

                // Access other claims as needed
            } catch (e: JWTDecodeException) {
                // Handle decoding error
                println("Failed to decode ID token: ${e.message}")
            }

            val next = Intent(this@MainActivity, TurboActivity::class.java)
            next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(next)



        }
    }

    fun decodeIdToken(idToken: String): DecodedJWT {
        val jwt = JWT.decode(idToken)
        val userId = jwt.subject
        val email = jwt.getClaim("email").asString()
        // Access other claims as needed
        return jwt
    }
}