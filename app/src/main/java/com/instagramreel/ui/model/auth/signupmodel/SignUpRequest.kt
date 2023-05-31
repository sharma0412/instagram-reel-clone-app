package com.instagramreel.ui.model.auth.signupmodel

import com.google.gson.annotations.SerializedName

data class SignUpRequest(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("fullname")
    val fullname: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("role")
    val role: String
)
