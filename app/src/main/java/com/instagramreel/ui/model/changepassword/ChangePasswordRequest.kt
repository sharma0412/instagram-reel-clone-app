package com.instagramreel.ui.model.changepassword


import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("oldPassword")
    val oldPassword: String
)