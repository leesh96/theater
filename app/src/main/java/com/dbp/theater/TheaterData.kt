package com.dbp.theater

import java.io.Serializable

data class TheaterData(
    val logo: String,
    val name: String,
    val screen: Int,
    val opentime: String,
    val contact: String,
    val address: String,
    val map: String
): Serializable