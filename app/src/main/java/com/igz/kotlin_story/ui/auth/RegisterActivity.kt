package com.igz.kotlin_story.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.databinding.ActivityRegisterBinding
import com.igz.kotlin_story.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AuthViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text?.toString()?.trim().orEmpty()
            val email = binding.edRegisterEmail.text?.toString()?.trim().orEmpty()
            val password = binding.edRegisterPassword.text?.toString()?.trim().orEmpty()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lengkapi data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.register(name, email, password)
        }

        viewModel.registerState.observe(this) { state ->
            when (state) {
                is ResultState.Loading -> binding.progress.visibility = View.VISIBLE
                is ResultState.Success -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this, "Registrasi berhasil, silakan masuk", Toast.LENGTH_LONG).show()
                    finish()
                }
                is ResultState.Error -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
