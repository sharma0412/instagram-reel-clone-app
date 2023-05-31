package com.instagramreel.ui.model.getmydetail


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) {
    data class Body(
        @SerializedName("age")
        val age: Any,
        @SerializedName("country")
        val country: Any,
        @SerializedName("country_code")
        val countryCode: Int,
        @SerializedName("country_name_code")
        val countryNameCode: String,
        @SerializedName("deviceToken")
        val deviceToken: Any,
        @SerializedName("deviceType")
        val deviceType: Any,
        @SerializedName("email")
        val email: String,
        @SerializedName("experince_from")
        val experinceFrom: Any,
        @SerializedName("experince_to")
        val experinceTo: Any,
        @SerializedName("fullname")
        val fullname: String,
        @SerializedName("gender")
        val gender: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_otp_verify")
        val isOtpVerify: String,
        @SerializedName("job_title")
        val jobTitle: Any,
        @SerializedName("num_otp_verification")
        val numOtpVerification: Int,
        @SerializedName("password")
        val password: String,
        @SerializedName("phone_num")
        val phoneNum: Long,
        @SerializedName("role")
        val role: Int
    )
}