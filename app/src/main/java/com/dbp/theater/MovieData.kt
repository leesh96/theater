package com.dbp.theater

import java.io.Serializable

data class MovieData(
    val poster: String,
    val title: String,
    val openingDay: String,
    val time: String,
    val genre: String,
    val content: String,
): Serializable