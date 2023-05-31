package com.instagramreel.ui.base

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Window
import com.instagramreel.R
import com.wang.avi.AVLoadingIndicatorView
import java.util.*

class BaseActivity : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        private var mLoader: Dialog? = null
        private var mLoadingView: AVLoadingIndicatorView? = null

        @JvmName("getContext1")
        fun getContext(): Context {
            return context
        }

        fun showLoader(context: Context) {
            try {
                if (mLoader != null) {
                    mLoader!!.dismiss()
                    mLoader!!.cancel()
                }
                if (mLoadingView != null) {
                    mLoadingView!!.hide()
                }
                mLoader = Dialog(context)
                mLoader!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mLoader!!.setContentView(R.layout.layout_progress_dialog)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(mLoader!!.window)
                        ?.setBackgroundDrawable(
                            ColorDrawable(
                                Color.TRANSPARENT
                            )
                        )
                }
                mLoader!!.setCancelable(false)
                mLoadingView = mLoader!!.findViewById(R.id.loadingView)
                mLoadingView!!.show()
                mLoader!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideLoader() {
            if (mLoader != null) {
                mLoader!!.dismiss()
                mLoader!!.cancel()
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}