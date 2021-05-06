package com.pburdelak.randomcityapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class CityColorCombination(
    val city: String,
    val color: String,
    val creationDate: Date
): Parcelable