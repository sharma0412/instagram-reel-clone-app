package com.instagramreel.ui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.instagramreel.R
import com.instagramreel.databinding.FragmentSpinnerPopupBinding
import com.instagramreel.ui.base.BaseActivity.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*


private val TAG = "Utils"

fun getError(throwable: Throwable): String {
    if (throwable is UnknownHostException) {
        return "No Internet Connection"
    } else if (throwable is SocketTimeoutException) {
        return "Server is not responding. Please try again"
    } else if (throwable is ConnectException) {
        return "Failed to connect server"
    } else {
        return "something went wrong !! please try again"
    }
}

fun getDateWithWeekName(date:String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("dd MMM (EEEE)")
    val date = inputFormat.parse(date)
    val formattedDate = outputFormat.format(date!!)
    println(formattedDate) // prints 10-0
    return formattedDate
}

fun gameDetailDateFormat(date:String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("EEE, dd MMM")
    val date = inputFormat.parse(date)
    val formattedDate = outputFormat.format(date!!)
    println(formattedDate) // prints (THU, 10 MAR)
    return formattedDate
}

fun gameDetailTimeFormat(time:String): String {
    val inputFormat = SimpleDateFormat("HH:mm")
    val outputFormat = SimpleDateFormat("hh:mm aa")
    val time = inputFormat.parse(time)
    val formattedDate = outputFormat.format(time!!)
    println(formattedDate) // prints (07:28 PM)
    return formattedDate
}

fun Context.hideKeyboard(view: View) {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

private fun onSNACK(context: Context, view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.blue)).show()
}

fun getDateString(date: String): String {
    val inputFormat = SimpleDateFormat("d-M-yyyy")
    val outputFormat = SimpleDateFormat("dd-MM-yyyy")
    val dt = inputFormat.parse(date)
    val formattedDate = outputFormat.format(dt!!)
    println(formattedDate) // prints 10-0
    return formattedDate
}

private fun datePicker(textView: TextView, context: Context) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(context, { _, yr, monthOfYear, dayOfMonth ->
        val mm = monthOfYear + 1
        val dateFrom =
            getDD(dayOfMonth).toString() + "/" + getDD(mm).toString() + "/" + yr.toString()
        textView.text = dateFrom
    }, year, month, day)

    dpd.show()
}

fun getDD(num: Int): String? {
    return if (num > 9) "" + num else "0$num"
}

@SuppressLint("ClickableViewAccessibility")
fun showPopupWindow(spinner: TextView, mainCon: ConstraintLayout) {
    val binding: FragmentSpinnerPopupBinding? = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.fragment_spinner_popup,
        null,
        false
    )
    val width = 800
    val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
    val focusable = true
    val popupWindow = PopupWindow(binding?.root, width, height, focusable)
    popupWindow.showAsDropDown(spinner, 0, 0, Gravity.TOP)
    mainCon.setOnTouchListener { _, _ ->
        popupWindow.dismiss()
        true
    }
}

suspend fun Activity.getImagePath(
    path: String,
    shouldOverride: Boolean = true,
    uri: Uri,
    isFromGallery: Boolean
): String {
    return withContext(Dispatchers.IO) {

        var scaledBitmap: Bitmap? = null

        try {
            val (hgt, wdt) = getImageHgtWdt(uri)
            try {
                scaledBitmap = getBitmapFromUri(uri)
                Log.d(
                    TAG,
                    "original bitmap height${scaledBitmap?.height} width${scaledBitmap?.width}"
                )
                Log.d(TAG, "Dynamic height$hgt width$wdt")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Store to tmp file
            val mFolder = File("$filesDir/Images")

            if (!mFolder.exists()) {
                mFolder.mkdir()
            }

            val tmpFile = File(mFolder.absolutePath, "IMG_${getTimestampString()}.jpg")

            val fos: FileOutputStream?
            try {
                fos = FileOutputStream(tmpFile)
                scaledBitmap?.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    fos
                )
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var compressedPath = ""
            if (tmpFile.exists() && tmpFile.length() > 0) {
                compressedPath = tmpFile.absolutePath
                if (shouldOverride) {
                    val srcFile = File(path)
                    val result = tmpFile.copyTo(srcFile, true)
                    Log.d(TAG, "copied file ${result.absolutePath}")
                    Log.d(TAG, "Delete temp file ${tmpFile.delete()}")
                }
            }

            scaledBitmap?.recycle()

            return@withContext if (shouldOverride) path else compressedPath
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return@withContext ""
    }

}

fun showHidePass(editText: EditText, imageView: ImageView) {
    if (editText.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
        imageView.setImageResource(R.drawable.ic_eye)
        //show password
        editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
    } else {
        imageView.setImageResource(R.drawable.ic_close_eye)
        //Hide Password
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
    }
}

fun getTimestampString(): String {
    val date = Calendar.getInstance()
    return SimpleDateFormat("yyyy MM dd hh mm ss", Locale.US).format(date.time).replace(" ", "")
}

fun Context.getImageHgtWdt(uri: Uri): Pair<Int, Int> {
    val opt = BitmapFactory.Options()

    /* by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded.
    If you try the use the bitmap here, you will get null.*/
    opt.inJustDecodeBounds = true
    val bm = getBitmapFromUri(uri, opt)

    var actualHgt = (opt.outHeight).toFloat()
    var actualWdt = (opt.outWidth).toFloat()

    /*val maxHeight = 816.0f
    val maxWidth = 612.0f*/
    val maxHeight = 720f
    val maxWidth = 1280f
    var imgRatio = actualWdt / actualHgt
    val maxRatio = maxWidth / maxHeight

//    width and height values are set maintaining the aspect ratio of the image
    if (actualHgt > maxHeight || actualWdt > maxWidth) {
        when {
            imgRatio < maxRatio -> {
                imgRatio = maxHeight / actualHgt
                actualWdt = (imgRatio * actualWdt)
                actualHgt = maxHeight
            }
            imgRatio > maxRatio -> {
                imgRatio = maxWidth / actualWdt
                actualHgt = (imgRatio * actualHgt)
                actualWdt = maxWidth
            }
            else -> {
                actualHgt = maxHeight
                actualWdt = maxWidth
            }
        }
    }

    return Pair(actualHgt.toInt(), actualWdt.toInt())
}

@Throws(IOException::class)
fun Context.getBitmapFromUri(uri: Uri, options: BitmapFactory.Options? = null): Bitmap? {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val image: Bitmap? = if (options != null)
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
    else
        BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}

fun getMediaPath(context: Context, uri: Uri, ext: String): String {

    val resolver = context.contentResolver
    val projection = arrayOf(MediaStore.Video.Media.DATA)
    var cursor: Cursor? = null
    try {
        cursor = resolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)

        } else ""

    } catch (e: Exception) {
        resolver.let {
            val filePath = (context.applicationInfo.dataDir + File.separator
                    + System.currentTimeMillis() + ext)

            val file = File(filePath)

            resolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    val buf = ByteArray(4096)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(
                        buf,
                        0,
                        len
                    )
                }
            }
            return file.absolutePath
        }
    } finally {
        cursor?.close()
    }
}


/*
fun showErrorDialog2(context: Context, message:String) {
    val dialog = Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    dialog.setContentView(R.layout.commom_dialog);


    val tvOkay = dialog.findViewById<TextView>(R.id.tvOkay)
    val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
    tvMessage.setText(message)

    tvOkay!!.setOnClickListener {
        dialog.dismiss()
        logout(context)
    }

    dialog.show()
}*/

/*
fun showProgress(context: Context) {
    try {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog!!.cancel()
        }
        progressDialog = Dialog(context)
        progressDialog!!.setContentView(R.layout.layout_progress_dialog)
        progressDialog!!.setCancelable(false)

        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        loader = progressDialog?.findViewById(R.id.loadingView)
        loader?.start()

        progressDialog!!.show()
    } catch (e: Exception) {
        e.printStackTrace()

    }

}

fun hideProgress() {
    try {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog!!.cancel()
        }
        loader?.stop()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}
*/
