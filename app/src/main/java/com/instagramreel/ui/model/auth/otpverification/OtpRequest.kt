package com.instagramreel.ui.model.auth.otpverification


import com.google.gson.annotations.SerializedName

data class OtpRequest(
    @SerializedName("otp")
    val otp: String
)