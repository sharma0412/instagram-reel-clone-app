package com.instagramreel.ui.utils

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.*
import java.util.*

class PhotoSaver(private val activity : Activity,val callback : PhotoSaverCallback) {

    private val TAG = "PhotoSaver"
    private var file : File? = null

    private fun generateFileName() : String{
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)?.absolutePath +
                "/${Random().nextInt()}.jpg"
    }

    private fun saveImageBitmap(bm:Bitmap?,fileName : String){

        file = File(fileName)

        if(!file!!.parentFile!!.exists()){
            file!!.parentFile!!.mkdirs()
        }

        return try{
            //save bitmap here...
            val outputStream = FileOutputStream(fileName) //to store images
            saveDataToGallery(bm,outputStream)
            //to show files in gallery
            MediaScannerConnection.scanFile(activity, arrayOf(fileName),null,null)
        } catch (e: IOException){
            Toast.makeText(activity, "Failed to store bitmap", Toast.LENGTH_SHORT).show()
        }
    }

    //for api level 29 or above
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageBitmap(bm: Bitmap?) {

        val imageName = "${Random().nextInt()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,imageName)
            put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH,"Pictures")
        }

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)?.absolutePath +
                "/$imageName"
        file = File(path)

        val uri = activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)

        uri?.let {
            activity.contentResolver.openOutputStream(it).use { outputStream ->
                outputStream?.let { stream->
                    try{
                        saveDataToGallery(bm,stream)
                    } catch (e:IOException){
                        Toast.makeText(activity, "Failed to store bitmap", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun saveDataToGallery(bm: Bitmap?,outputStream: OutputStream){
        val outputData = ByteArrayOutputStream()
        bm?.compress(Bitmap.CompressFormat.JPEG,90,outputData) //store bytes in outputData
        outputData.writeTo(outputStream)
        outputStream.flush()
        outputStream.close()
        callback.getPath(file)
    }

    fun initSavePhoto(bitmap: Bitmap?) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val fileName = generateFileName()
            saveImageBitmap(bitmap, fileName)
        } else {
            saveImageBitmap(bitmap)
        }
    }

    interface PhotoSaverCallback{
        fun getPath(file:File?)
    }

}