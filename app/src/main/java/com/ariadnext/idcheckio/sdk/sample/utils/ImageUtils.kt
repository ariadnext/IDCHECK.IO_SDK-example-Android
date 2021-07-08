package com.ariadnext.idcheckio.sdk.sample.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import java.io.ByteArrayOutputStream
import java.io.File

object ImageUtils {

    fun getImageViewFromPath(context: Context, uri: Uri): ImageView {
        val imageView = ImageView(context)
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        val byteArray = getByteArrayFromUri(context, uri)
        byteArray?.let {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))
        }
        return imageView
    }

    private fun getByteArrayFromUri(context: Context, uri: Uri): ByteArray? {
        var data: ByteArray? = null
        try {
            val cr = context.contentResolver
            val inputStream = cr.openInputStream(uri)
            val bos = ByteArrayOutputStream()
            val b = ByteArray(1024)
            var bytesRead: Int
            bytesRead = inputStream!!.read(b)
            while (bytesRead != -1) {
                bos.write(b, 0, bytesRead)
                bytesRead = inputStream.read(b)
            }
            data = bos.toByteArray()
            bos.close()
            inputStream.close()
        } catch (ex: Exception) { /* Failed to retrieve image from internal memory */
        }
        return data
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        val byteArray = getByteArrayFromUri(context, uri)
        return byteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    /**
     * Move the results images to show them later in the results pages.
     */
    fun moveImages(context: Context, idcheckioResult: IdcheckioResult) {
        val dirs = File("${context.cacheDir.absolutePath}/session_images")
        if (!dirs.exists()) {
            dirs.mkdirs()
        }
        for (image in idcheckioResult.images) {
            image.source.takeIf { it.isNotEmpty() }?.let {
                image.source = moveFile(it, dirs.absolutePath)
            }
            image.cropped.takeIf { it.isNotEmpty() }?.let {
                image.cropped = moveFile(it, dirs.absolutePath)
            }
            image.face.takeIf { it.isNotEmpty() }?.let {
                image.face = moveFile(it, dirs.absolutePath)
            }
        }
    }

    /**
     * Move a file
     */
    private fun moveFile(origin: String, destination: String): String {
        val fileName = origin.split("/").last()
        val from = File(origin)
        val to = File("$destination/$fileName")
        from.renameTo(to)
        return to.absolutePath
    }
}
