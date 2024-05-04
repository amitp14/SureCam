package com.example.surecam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import dev.hotwire.turbo.activities.TurboActivity

class TurboActivity : AppCompatActivity() , TurboActivity {

    override lateinit var delegate: TurboActivityDelegate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_turbo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_turbo)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById(R.id.toolbar)) // Use the custom toolbar as the ActionBar

        supportActionBar?.apply {
            title = ""
            // Other customization options like setting icons, enabling home button, etc.
        }

        delegate = TurboActivityDelegate(this, R.id.main_nav_host)
    }



    override fun onRestart() {
        delegate.refresh()

        super.onRestart()
    }


}