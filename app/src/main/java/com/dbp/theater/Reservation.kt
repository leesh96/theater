package com.dbp.theater

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reservation(
    val id: Int,
    val payment: String,
    val seat: String,
    val userName: String,
    val startTime: String,
    val endTime: String,
    val moviePoster: String,
    val movieTitle: String,
    val screenName: String,
    val theaterName: String
) : Parcelable
