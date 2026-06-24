package com.example.animeapp.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun encodeImageUriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val maxWidth = 400
        val maxHeight = 400
        var width = originalBitmap.width
        var height = originalBitmap.height

        if (width > maxWidth || height > maxHeight) {
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

            if (ratioMax > ratioBitmap) {
                width = (maxHeight * ratioBitmap).toInt()
                height = maxHeight
            } else {
                width = maxWidth
                height = (maxWidth / ratioBitmap).toInt()
            }
        }
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)

        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        val byteArray = outputStream.toByteArray()

        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        null
    }
}


fun getImageModel(imageUrl: String?): Any? {
    if (imageUrl == null) return null

    return if (imageUrl.length > 1000) {
        try {
           Base64.decode(imageUrl, Base64.DEFAULT)
        } catch (e: Exception) {
            imageUrl
        }
    } else {
        imageUrl
    }
}