package com.example.animeapp.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.roundToInt

private const val TARGET_BASE64_BYTES = 220_000
private const val INITIAL_MAX_DIMENSION = 640
private const val MIN_DIMENSION = 220
private const val INITIAL_QUALITY = 72
private const val MIN_QUALITY = 34

fun encodeImageUriToBase64(context: Context, uri: Uri): String? {
    return try {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        }

        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        val decodeOptions = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.RGB_565
            inSampleSize = calculateInSampleSize(
                sourceWidth = bounds.outWidth,
                sourceHeight = bounds.outHeight,
                targetMaxDimension = INITIAL_MAX_DIMENSION
            )
        }

        val decoded = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        } ?: return null

        compressAdaptively(decoded)
    } catch (e: Exception) {
        null
    } catch (e: OutOfMemoryError) {
        null
    }
}

private fun compressAdaptively(source: Bitmap): String? {
    var maxDimension = INITIAL_MAX_DIMENSION
    var quality = INITIAL_QUALITY
    var workingBitmap: Bitmap? = null

    try {
        while (maxDimension >= MIN_DIMENSION) {
            if (workingBitmap != null && workingBitmap !== source) {
                workingBitmap.recycle()
            }
            workingBitmap = resizeKeepingAspectRatio(source, maxDimension)

            while (quality >= MIN_QUALITY) {
                val bytes = compressJpeg(workingBitmap, quality)
                val estimatedBase64Bytes = ((bytes.size + 2) / 3) * 4
                if (estimatedBase64Bytes <= TARGET_BASE64_BYTES ||
                    (maxDimension <= MIN_DIMENSION && quality <= MIN_QUALITY)
                ) {
                    return Base64.encodeToString(bytes, Base64.NO_WRAP)
                }
                quality -= 8
            }

            maxDimension = (maxDimension * 0.82f).roundToInt()
            quality = max(quality + 18, MIN_QUALITY)
        }
    } finally {
        if (workingBitmap != null && workingBitmap !== source) {
            workingBitmap.recycle()
        }
        source.recycle()
    }

    return null
}

private fun calculateInSampleSize(
    sourceWidth: Int,
    sourceHeight: Int,
    targetMaxDimension: Int
): Int {
    var inSampleSize = 1
    var halfWidth = sourceWidth / 2
    var halfHeight = sourceHeight / 2

    while (halfWidth / inSampleSize >= targetMaxDimension &&
        halfHeight / inSampleSize >= targetMaxDimension
    ) {
        inSampleSize *= 2
    }
    return inSampleSize.coerceAtLeast(1)
}

private fun resizeKeepingAspectRatio(source: Bitmap, maxDimension: Int): Bitmap {
    val largestSide = max(source.width, source.height)
    if (largestSide <= maxDimension) return source

    val scale = maxDimension.toFloat() / largestSide.toFloat()
    val targetWidth = (source.width * scale).roundToInt().coerceAtLeast(1)
    val targetHeight = (source.height * scale).roundToInt().coerceAtLeast(1)
    return Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true)
}

private fun compressJpeg(bitmap: Bitmap, quality: Int): ByteArray {
    val estimatedSize = bitmap.width * bitmap.height / 5
    val outputStream = ByteArrayOutputStream(estimatedSize.coerceAtLeast(16 * 1024))
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
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
