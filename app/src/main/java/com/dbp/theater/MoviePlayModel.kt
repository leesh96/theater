package com.dbp.theater

data class MoviePlayModel(
    val id: Int,
    val startTime: String,
    val endTime: String,
    val movieTitle: String,
    val screenName: String,
    val chairRow: String,
    val chairCol: String,
    val theaterName: String
)
