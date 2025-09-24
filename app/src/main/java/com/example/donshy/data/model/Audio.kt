package com.example.donshy.data.model

data class Audio(
    val name: String,
    val path: String,
    val type: String,
    var isPlaying: Boolean = false
)
