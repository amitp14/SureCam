package com.example.surecam

import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.surecam.features.web.WebFragment
import com.example.surecam.util.BASE_URL_login_with_app
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import kotlin.reflect.KClass
import dev.hotwire.turbo.BuildConfig

class MainSessionNavHost : TurboSessionNavHostFragment() {

    override val sessionName = "main"

    override val startLocation = BASE_URL_login_with_app

    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() = listOf()

    override val registeredFragments: List<KClass<out Fragment>>
        get() = listOf(
            //
            WebFragment::class
        )

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() = TurboPathConfiguration.Location(
            assetFilePath = "json/configuration.json",
        )

    override fun onSessionCreated() {
        super.onSessionCreated()
        session.webView.settings.userAgentString = customUserAgent(session.webView)
        if (BuildConfig.DEBUG) {
            session.setDebugLoggingEnabled(true)
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }


    private fun customUserAgent(webView: WebView): String {
        return "Turbo Native Android ${webView.settings.userAgentString}"
    }

}