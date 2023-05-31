package com.instagramreel.ui.ui.athlete.activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.material.snackbar.Snackbar
import com.instagramreel.R
import com.instagramreel.databinding.ActivityCameraBinding
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.utils.CustomRecordButton
import com.instagramreel.ui.utils.FilePath2
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var camera: Camera? = null
    var timer: CountDownTimer? = null
    private var lens = CameraSelector.DEFAULT_BACK_CAMERA
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(this@CameraActivity)
    }

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera(lens)
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // on Back Press Button
        binding.backBTN.setOnClickListener {
            startActivity(Intent(this@CameraActivity,AthleteMainActivity::class.java))
            finish()
        }

        // Change Camera Position
        binding.switchCameraBTN.setOnClickListener {
            if (lens == CameraSelector.DEFAULT_BACK_CAMERA) {
                lens = CameraSelector.DEFAULT_FRONT_CAMERA
                binding.flashBTNOff.visibility = View.GONE
                startCamera(lens)
            }
            else {
                lens = CameraSelector.DEFAULT_BACK_CAMERA
                binding.flashBTNOff.visibility = View.VISIBLE
                startCamera(lens)
            }
        }

        binding.flashBTNOff.setOnClickListener {
            binding.flashBTNOn.visibility = View.VISIBLE
            binding.flashBTNOff.visibility = View.GONE
            camera?.cameraControl?.enableTorch(true)
        }

        binding.flashBTNOn.setOnClickListener {
            binding.flashBTNOn.visibility = View.GONE
            binding.flashBTNOff.visibility = View.VISIBLE
            camera?.cameraControl?.enableTorch(false)
        }

        binding.component.enableVideoRecording(true)
        binding.component.enablePhotoTaking(false)


        binding.component.actionListener = object : CustomRecordButton.ActionListener {
            override fun onCancelled() {
                binding.time.text = ""
            }

            override fun onDurationTooShortError() {
                onSNACK(binding.root, "At least 1 second required.")
            }

            override fun onEndRecord() {
                timer?.cancel()
                binding.time.text = ""

                // Stop Video Recording and save
                val curRecording = recording
                if (curRecording != null) {
                    // Stop the current recording session.
                    curRecording.stop()
                    camera?.cameraControl?.enableTorch(false)
                    Toast.makeText(
                        this@CameraActivity,
                        "Video recorded successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    recording = null
                    return
                }

                binding.flashBTNOn.visibility = View.GONE
                binding.flashBTNOff.visibility = View.VISIBLE
            }

            override fun onSingleTap() {
                onSNACK(binding.root, "Image capture not allowed.")
            }

            override fun onStartRecord() {
                captureVideo()
            }

        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun onSNACK(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
            .setBackgroundTint(ContextCompat.getColor(this@CameraActivity, R.color.blue)).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera(lens)
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        // create and start a new recording session
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Video_${System.currentTimeMillis()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/ScoutStarr")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        this@CameraActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) ==
                    PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                            timer = object : CountDownTimer(30000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    binding.time.text = "00:" + millisUntilFinished / 1000
                                    //here you can have your logic to set text to edittext
                                }

                                override fun onFinish() {
                                    binding.time.text = ""
                                }
                            }
                            (timer as CountDownTimer).start()
                    }
                    is VideoRecordEvent.Finalize -> {
                        timer?.cancel()
                        binding.time.text = ""
                        if (!recordEvent.hasError()) {
                            val path = FilePath2.getPath(this,recordEvent.outputResults.outputUri)

                            if(!path.isNullOrEmpty()){
                                val intent = Intent(this@CameraActivity, VideoPlayerActivity::class.java)
                                //intent.putExtra("videoUrl",recordEvent.outputResults.outputUri.toString())

                                appPrefs.setString("videoPath",path)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(
                                TAG, "Video capture ends with error: " +
                                        "${recordEvent.error}"
                            )
                        }
                    }
                }
            }
    }

    private fun startCamera(lensFacing: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(this, lensFacing, preview, videoCapture)
                camera!!.cameraInfo.hasFlashUnit()

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "ScoutStarr"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}