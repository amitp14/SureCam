package com.example.surecam.features.web

import android.content.Intent
import android.webkit.CookieManager
import android.webkit.WebStorage
import com.example.surecam.MainActivity
import com.example.surecam.MainSessionNavHost
import com.example.surecam.SessionManager
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.fragments.TurboWebFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import dev.hotwire.turbo.delegates.TurboActivityDelegate

@TurboNavGraphDestination(uri = "turbo://fragment/web")
class WebFragment: TurboWebFragment(), TurboActivity {

    override lateinit var delegate: TurboActivityDelegate

    override fun shouldNavigateTo(newLocation: String): Boolean {

        println("VTS 2 newLocation $newLocation")

        if(newLocation.contains("/auth/logout")){
            startMainActivity()
            return  false
        }else {
            return true
        }
    }



    private fun startMainActivity() {
        SessionManager(requireActivity()).loggOut()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }
}