package com.happy.friendogly.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Throws(IOException::class)
fun saveBitmapToFile(
    context: Context,
    bitmap: Bitmap,
): File {
    val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val file = File(directory, "image.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return file
}

fun File.toMultipartBody(): MultipartBody.Part {
    val requestFile = this.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("image", this.name, requestFile)
}

fun Uri.toBitmap(context: Context): Bitmap =
    if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    }

fun Bitmap.toSoftwareBitmap(): Bitmap {
    return copy(Bitmap.Config.ARGB_8888, true)
}
