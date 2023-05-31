package com.instagramreel.ui.model.auth.otpverification


import com.google.gson.annotations.SerializedName

data class NumberRequest(
    @SerializedName("phone_code")
    val countryCode: String,
    @SerializedName("country_code")
    val countryNameCode: String,
    @SerializedName("phone_no")
    val phoneNum: String
)