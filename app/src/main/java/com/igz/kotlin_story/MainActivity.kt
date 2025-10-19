package com.igz.kotlin_story

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.igz.kotlin_story.data.local.SessionManager
import com.igz.kotlin_story.ui.home.HomeActivity
import com.igz.kotlin_story.ui.auth.LoginActivity
import com.igz.kotlin_story.util.LocaleUtils
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Route based on session without showing UI
        val session = SessionManager(this)
        // Apply theme & locale at startup
        runBlocking {
            when (session.themeFlow.first()) {
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            LocaleUtils.applyLocale(this@MainActivity, session.localeFlow.first())
        }
        lifecycleScope.launch {
            val loggedIn = session.isLoggedInFlow.first()
            val intent = if (loggedIn) {
                HomeActivity.newIntent(this@MainActivity)
            } else {
                LoginActivity.newIntent(this@MainActivity)
            }
            startActivity(intent)
            finish()
        }
    }
}