package com.igz.kotlin_story.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {

    fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_${'$'}timeStamp", ".jpg", storageDir)
    }

    fun copyUriToCache(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val cacheDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
        val outFile = File(cacheDir, "picked_${'$'}{System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            FileOutputStream(outFile).use { output ->
                if (input != null) input.copyTo(output)
            }
        }
        return outFile
    }

    fun reduceImageFile(file: File, maxSize: Int = 1024): File {
        // Basic compression to keep under ~1MB
        val bmp = BitmapFactory.decodeFile(file.path)
        var compressQuality = 90
        var streamLength: Int
        do {
            val bmpStream = java.io.ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > maxSize * 1024 && compressQuality > 10)
        FileOutputStream(file).use { fos ->
            bmp.compress(Bitmap.CompressFormat.JPEG, compressQuality, fos)
        }
        return file
    }
}
