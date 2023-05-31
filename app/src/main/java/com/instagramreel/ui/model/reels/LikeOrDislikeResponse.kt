package com.instagramreel.ui.model.reels

data class LikeOrDislikeResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
) {
    class Body
}