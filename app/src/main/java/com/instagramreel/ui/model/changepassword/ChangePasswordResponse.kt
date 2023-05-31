package com.instagramreel.ui.model.changepassword


import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
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