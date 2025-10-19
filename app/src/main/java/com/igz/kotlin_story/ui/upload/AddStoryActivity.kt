package com.igz.kotlin_story.ui.upload

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModelProvider
import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.databinding.ActivityAddStoryBinding
import com.igz.kotlin_story.ui.ViewModelFactory
import com.igz.kotlin_story.util.ImageUtils
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel

    private var currentImageFile: File? = null
    private var tempCameraUri: Uri? = null

    private val pickGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = ImageUtils.copyUriToCache(this, it)
            currentImageFile = file
            binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            currentImageFile?.let { file ->
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
            }
        }
    }

    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) openCameraInternal() else Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AddStoryViewModel::class.java]

        binding.btnGallery.setOnClickListener { pickGallery.launch("image/*") }
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.buttonAdd.setOnClickListener { doUpload() }

        viewModel.uploadState.observe(this) { state ->
            when (state) {
                is ResultState.Loading -> binding.progress.visibility = View.VISIBLE
                is ResultState.Success -> {
                    binding.progress.visibility = View.GONE
                    setResult(RESULT_OK)
                    finish()
                }
                is ResultState.Error -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openCamera() {
        val granted = PermissionChecker.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED
        if (!granted) {
            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
        } else {
            openCameraInternal()
        }
    }

    private fun openCameraInternal() {
        val imageFile = ImageUtils.createImageFile(this)
        currentImageFile = imageFile
        val authority = packageName + ".fileprovider"
        val photoUri = FileProvider.getUriForFile(this, authority, imageFile)
        tempCameraUri = photoUri
        takePicture.launch(photoUri)
    }

    private fun doUpload() {
        val file = currentImageFile
        val description = binding.edAddDescription.text?.toString()?.trim().orEmpty()
        if (file == null) {
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }
        if (description.isEmpty()) {
            Toast.makeText(this, "Isi deskripsi", Toast.LENGTH_SHORT).show()
            return
        }
        val reduced = ImageUtils.reduceImageFile(file)
        viewModel.upload(reduced, description)
    }
}
