package com.instagramreel.ui.model.auth.otpverification


import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) {
    class Body
}