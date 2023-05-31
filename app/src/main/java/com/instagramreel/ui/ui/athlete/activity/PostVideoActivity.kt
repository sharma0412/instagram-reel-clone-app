package com.instagramreel.ui.ui.athlete.activity

import android.app.Application
import android.content.Intent
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.bumptech.glide.Glide
import com.instagramreel.R
import com.instagramreel.databinding.ActivityPostVideoBinding
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.model.scout.getsupportsdata.GetSportsResponse
import com.instagramreel.ui.repository.Repository
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.utils.Constants
import com.instagramreel.ui.utils.Status
import com.instagramreel.ui.viewModelFactory.ViewModelFactory
import com.instagramreel.ui.viewmodel.reels.UploadReelsViewModel
import com.instagramreel.ui.viewmodel.scout.GetSupportsDataViewModel
import java.io.File


class PostVideoActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "PostVideoActivity"
    private var videoURL: String? = null
    lateinit var binding : ActivityPostVideoBinding
    private var url = ""
    private val appPrefs: AppPrefs by lazy {
        AppPrefs(this@PostVideoActivity)
    }
    private var sportId = "0"
    private var sportsList: ArrayList<GetSportsResponse.Body> = ArrayList()
    lateinit var viewModelFactory: ViewModelFactory
    private var uploadReelsViewModel: UploadReelsViewModel? = null

    private val getSupportsDataViewModel by viewModels<GetSupportsDataViewModel> {
        ViewModelFactory(
            Application(),
            Repository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory(Application(), Repository())
        uploadReelsViewModel = ViewModelProvider(this, viewModelFactory).get(
            UploadReelsViewModel::class.java
        )
        setObserver()
        url = appPrefs.getStringKey("videoPath").toString()
        viewModelFactory = ViewModelFactory(Application(), Repository())
        getSupportsDataViewModel.getSportsList()
        initUi()

        val thumb = ThumbnailUtils.createVideoThumbnail(url, MediaStore.Images.Thumbnails.MINI_KIND)
        Glide.with(this@PostVideoActivity).load(thumb).into(binding.videoThumbnail)
    }


    private fun initUi() {
        //setImage()
        binding.apply {
            ivBack.setOnClickListener(this@PostVideoActivity)
            btCancel.setOnClickListener(this@PostVideoActivity)
            btPostVideo.setOnClickListener(this@PostVideoActivity)
        }
    }

   /* private fun setImage() {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(Uri.parse(url), filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        url = cursor.getString(columnIndex)
        cursor.close()
        val thumb = try {
            ThumbnailUtils.createVideoThumbnail(
                url,
                MediaStore.Images.Thumbnails.FULL_SCREEN_KIND)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val configuration = resources.configuration
        val smallestScreenWidthDp = configuration.smallestScreenWidthDp
        val thumbBitmap =
            Bitmap.createScaledBitmap(thumb!!, smallestScreenWidthDp, smallestScreenWidthDp, false)
        try {
            val outputStream: FileOutputStream
            val videoThumb = File(filesDir, "thumb.jpg")
            outputStream = FileOutputStream(videoThumb)
            thumbBitmap!!.compress(Bitmap.CompressFormat.PNG, 95, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Glide.with(this@PostVideoActivity).load(thumb).into(binding.videoThumbnail)
    }*/

    private fun setObserver() {
        getSupportsDataViewModel.getData.observe(this) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        sportsList = it?.data?.body as java.util.ArrayList<GetSportsResponse.Body>

                        val adapter: ArrayAdapter<String> = ArrayAdapter(
                            this@PostVideoActivity,
                            R.layout.custom_spinner,
                            sportsList.map { d -> d.name }
                        )
                        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)

                        /*val arrayAdapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_item,

                        )*/
                        sportId = binding.spnSports.selectedItemId.toString()
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spnSports.adapter = adapter
                    }
                    Status.LOADING -> {
                        BaseActivity.showLoader(this)
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        uploadReelsViewModel?.uploadReels?.observe(this) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        BaseActivity.hideLoader()
                        //do Something
                        startActivity(Intent(this@PostVideoActivity,AthleteMainActivity::class.java))
                        finish()
                    }
                    Status.LOADING -> {
                        BaseActivity.showLoader(this)
                    }
                    Status.ERROR -> {
                        BaseActivity.hideLoader()
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        binding.apply {
            when(p0) {
                ivBack -> {
                    startActivity(Intent(this@PostVideoActivity,VideoPlayerActivity::class.java).putExtra("videoUrl",url))
                    finish()
                }
                btCancel -> {
                    startActivity(Intent(this@PostVideoActivity,CameraActivity::class.java).putExtra("videoUrl",url))
                    finish()
                }
                btPostVideo -> {
                    //uploadToS3(File(url))
                    uploadVideo(File(url),1,File(url))
                   /* uploadReelsViewModel?.onUploadReels(
                        UploadReelsRequest(sportId,url,videoURL.toString())
                    )*/
                }
            }
        }
    }

    /*private fun uploadToS3(newFile:File){
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val awsSecret = appPrefs.getStringKey("Secret_Key")
        val awsKey = appPrefs.getStringKey("Access_KEY")
        val bucketName = appPrefs.getStringKey("Bucket_name")

        val credentials =
            BasicAWSCredentials(awsKey,awsSecret)

        val s3 = AmazonS3Client(credentials)
        java.security.Security.setProperty("networkaddress.cache.ttl","60")

        s3.setRegion(com.amazonaws.regions.Region.getRegion(Regions.AP_SOUTH_1))

        TransferNetworkLossHandler.getInstance(this)
        val transferUtility = TransferUtility.builder()
            .defaultBucket(bucketName)
            .context(this).s3Client(s3).build()

        var name = newFile.name
        val pos = name.lastIndexOf(".")

        if (pos > 0) {
            name = name.substring(0, pos)
        }

        val path = "$name.${newFile.extension}"

        val transferListener = object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState)  {
                when (state) {
                    TransferState.WAITING ->{
                        Log.d(TAG, "onProgressChanged: Video Upload Waiting")
                    }
                    TransferState.IN_PROGRESS->{
                        Log.d(TAG, "onProgressChanged: Video Upload In Progress")
                    }
                    TransferState.COMPLETED -> {
                        Log.d(TAG, "onProgressChanged: Video Upload Success")
                        videoURL = if(BuildConfig.DEBUG){
                            "${Constants.AWS_VIDEO_URL}$path"
                        } else{
                            "${Constants.AWS_VIDEO_URL}$path"
                        }
                        uploadReelsViewModel?.onUploadReels(
                            UploadReelsRequest(sportId,url,videoURL.toString())
                        )
                    }
                    TransferState.WAITING -> {
                        Log.d(TAG, "onStateChanged: Waiting S3")
                    }
                    TransferState.CANCELED -> {
                        Log.d(TAG, "onStateChanged: Cancelled S3")
                    }
                    TransferState.FAILED -> {
                        Log.d(TAG, "onStateChanged: Failed S3")
                    }
                    TransferState.PAUSED -> {
                        Log.d(TAG, "onStateChanged: Paused S3")
                    }
                    else -> {

                    }
                }
            }
            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val status = (((current.toDouble() / total) * 100.0).toInt())
                Log.d(TAG, "onProgressChanged: Progress Status $status")
            }
            override fun onError(id: Int, ex: Exception) {
                Log.e(TAG, "Error $ex")
            }
        }

        val uploadObserver =
            transferUtility.upload(
                bucketName,
                "$name.${newFile.extension}",
                newFile,
                CannedAccessControlList.PublicRead
            )

        uploadObserver.setTransferListener(transferListener)

    }*/


    private fun uploadVideo(thumbnailFile: File, type:Int, thumbnail: File){

        Log.i("TAG", "uploadThumbnail: ${thumbnailFile.absolutePath}")

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        /*  val awsSecret = userSharePrefernces?.awS_SECRET
          val awsKey = userSharePrefernces?.awS_KEY
          val bucketName = userSharePrefernces?.buckeT_NAME
  */

        //   val awsSecret = userSharePrefernces?.awS_SECRET
        val awsSecret = "AWS SECRET"
        //   val awsKey = userSharePrefernces?.awS_KEY
        val awsKey = "AWS KEY"
        // val bucketName = userSharePrefernces?.buckeT_NAME
        val bucketName = "BUCKET NAME"
        val credentials = BasicAWSCredentials(awsKey, awsSecret)
        val s3 = AmazonS3Client(credentials)
        java.security.Security.setProperty("networkaddress.cache.ttl","60")
        s3.setRegion(com.amazonaws.regions.Region.getRegion(Regions.AP_SOUTH_1))

        val transferUtility = TransferUtility.builder()
            .defaultBucket(bucketName)
            .context(this).s3Client(s3).build()

        val newFile = File(thumbnailFile.absolutePath)

        var name = newFile.name
        val pos = name.lastIndexOf(".")

        if (pos > 0) {
            name = name.substring(0, pos)
        }

        val path = "$name.${newFile.extension}"

        val uploadObserver =
            transferUtility.upload(
                bucketName,
                "$name.${newFile.extension}",
                newFile,
                CannedAccessControlList.PublicRead
            )


         val transferListener = object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState)  {
                if (state == TransferState.COMPLETED) {
                    val preview = path
                    Log.d("SignUpActivity", Constants.AWS_VIDEO_URL+preview)
                }
            }
            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val status = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("SignUpActivity", "onProgressChanged: Progress Status $status")
            }
            override fun onError(id: Int, ex: Exception) {
                Log.e("SignUpActivity", "Error ${ex.toString()}")
            }
        }

        uploadObserver.setTransferListener(transferListener)

    }
}