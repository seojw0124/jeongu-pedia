package com.example.jeongu_pedia.datamodel

import java.util.Date

data class Review (
    val movieId: String,
    val reviewer: String,
    val poster: String,
    val score: String,
    val text: String,
    val createdAt: String,
) {

}