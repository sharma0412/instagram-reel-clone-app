package com.instagramreel.ui.model.auth.signinmodel


import com.google.gson.annotations.SerializedName

data class SignInResponse(
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
        @SerializedName("token")
        val token: String,
        @SerializedName("userData")
        val userData: UserData
    ) {
        data class UserData(
            @SerializedName("country_code")
            val countryCode: String,
            @SerializedName("country_name")
            val countryName: Any,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("deviceToken")
            val deviceToken: Any,
            @SerializedName("deviceType")
            val deviceType: Any,
            @SerializedName("dob")
            val dob: Any,
            @SerializedName("email")
            val email: String,
            @SerializedName("fullname")
            val fullname: String,
            @SerializedName("height")
            val height: Any,
            @SerializedName("id")
            val id: Int,
            @SerializedName("is_verified")
            val isVerified: Int,
            @SerializedName("otp")
            val otp: String,
            @SerializedName("password")
            val password: String,
            @SerializedName("phone_code")
            val phoneCode: Int,
            @SerializedName("phone_no")
            val phoneNo: Long,
            @SerializedName("position")
            val position: Any,
            @SerializedName("profile_pic")
            val profilePic: Any,
            @SerializedName("role")
            val role: Int,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("weight")
            val weight: Any
        )
    }
}