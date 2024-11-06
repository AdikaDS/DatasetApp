package com.example.datasetapp.data.model

import android.net.Uri

data class SelfiePhoto(
    val id: Int,
    var imageUri: Uri?,
    val title: String
)
