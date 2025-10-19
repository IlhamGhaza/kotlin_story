package com.igz.kotlin_story.ui.settings

import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.igz.kotlin_story.R
import com.igz.kotlin_story.data.local.SessionManager
import com.igz.kotlin_story.databinding.ActivitySettingsBinding
import com.igz.kotlin_story.util.LocaleUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        lifecycleScope.launch {
            // Initialize current selections
            when (session.themeFlow.first()) {
                "light" -> binding.rbLight.isChecked = true
                "dark" -> binding.rbDark.isChecked = true
                else -> binding.rbSystem.isChecked = true
            }
            when (session.localeFlow.first()) {
                "en" -> binding.rbEn.isChecked = true
                else -> binding.rbId.isChecked = true
            }
        }

        binding.rgTheme.setOnCheckedChangeListener { _: RadioGroup, checkedId: Int ->
            lifecycleScope.launch {
                val mode = when (checkedId) {
                    R.id.rb_light -> "light"
                    R.id.rb_dark -> "dark"
                    else -> "system"
                }
                session.setTheme(mode)
                when (mode) {
                    
                    "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

        binding.rgLanguage.setOnCheckedChangeListener { _: RadioGroup, checkedId: Int ->
            lifecycleScope.launch {
                val lang = if (checkedId == R.id.rb_en) "en" else "id"
                session.setLocale(lang)
                LocaleUtils.applyLocale(this@SettingsActivity, lang)
                recreate()
            }
        }
    }
}
