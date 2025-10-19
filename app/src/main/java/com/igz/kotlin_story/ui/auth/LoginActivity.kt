package com.igz.kotlin_story.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.igz.kotlin_story.databinding.ActivityLoginBinding
import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.ui.ViewModelFactory
import com.igz.kotlin_story.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AuthViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text?.toString()?.trim().orEmpty()
            val password = binding.edLoginPassword.text?.toString()?.trim().orEmpty()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lengkapi email & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(email, password)
        }

        binding.tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                is ResultState.Loading -> binding.progress.visibility = View.VISIBLE
                is ResultState.Success -> {
                    binding.progress.visibility = View.GONE
                    // Navigate to Home and clear back stack
                    val intent = HomeActivity.newIntent(this)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                is ResultState.Error -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
