package com.instagramreel.ui.model.auth.signupmodel

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("body")
    val body: Body? = null
)

data class Body(
    @field:SerializedName("token")
    val token: String? = null
)
