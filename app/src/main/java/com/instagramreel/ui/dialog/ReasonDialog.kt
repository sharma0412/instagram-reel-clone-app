package com.instagramreel.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.instagramreel.R
import com.instagramreel.databinding.CustomeLayoutBinding

class ReasonDialog(context: Context, private val listener: Listener) : Dialog(context), View.OnClickListener {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var binding: CustomeLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(inflater, R.layout.custome_layout, null, false)
        window?.decorView?.left = 30
        window?.decorView?.right = 30
        window?.attributes?.gravity= Gravity.CENTER
        setCancelable(false)
        setContentView(binding?.root!!)
        window?.setLayout(900, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding?.btCancel?.setOnClickListener(this)
        binding?.ivCancel?.setOnClickListener(this)
        binding?.btOk?.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v) {
            binding?.btCancel->{
                dismiss()
            }
            binding?.ivCancel->{
                dismiss()
            }
            binding?.btOk->{
                listener.onSelectVerifyKeyStatus(binding?.etReason?.text?.toString()!!)
            }
        }
    }

    interface Listener{
        fun onSelectVerifyKeyStatus(value: String)
    }

}