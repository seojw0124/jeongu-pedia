package com.example.jeongu_pedia.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie (
    val id: String,
    val title: String,
    val poster: String,
    val pubDate: String,
    val director: String,
    val casts: String,
    val country: String,
    val userDating: String
    ):Parcelable {
    constructor(): this("","","","","","","","")
}