package com.ultimate.ultimatesophos.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

object DocumentConverterHelper {
    fun encodeUriToBase64(context: Context, uri: Uri): String? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use { c ->
            val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (c.moveToFirst()) {
                val name = c.getString(nameIndex)
                inputStream?.let { inputStream ->
                    // create same file with same name
                    val file = File(context.cacheDir, name)
                    val os = file.outputStream()
                    os.use {
                        inputStream.copyTo(it)
                    }
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    return encodeToBase64(bitmap)
                }
            }
        }
        return null
    }

    private fun encodeToBase64(image: Bitmap): String {
        // Necessary for image size reduction
        val scaleDivider = 4
        val scaleWidth: Int = image.width / scaleDivider
        val scaleHeight: Int = image.height / scaleDivider
        // The compressed image is converted to byte for easy handling
        val b: ByteArray? = getDownsizedImageBytes(image, scaleWidth, scaleHeight)
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun decodeFromBase64(imageString: String): Bitmap? {
        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    @Throws(IOException::class)
    fun getDownsizedImageBytes(
        fullBitmap: Bitmap?,
        scaleWidth: Int,
        scaleHeight: Int,
    ): ByteArray? {
        val scaledBitmap = fullBitmap?.let {
            Bitmap.createScaledBitmap(it, scaleWidth, scaleHeight, true)
        }

        // Instantiate the downsized image content as a byte[]
        val baos = ByteArrayOutputStream()
        scaledBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        return baos.toByteArray()
    }
}
