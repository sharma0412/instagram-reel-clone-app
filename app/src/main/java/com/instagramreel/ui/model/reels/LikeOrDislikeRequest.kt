package com.instagramreel.ui.model.reels

data class LikeOrDislikeRequest(
    val reel_id: String,
    val status: String
)