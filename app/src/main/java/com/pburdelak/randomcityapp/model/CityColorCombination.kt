package com.pburdelak.randomcityapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "city_color_combination")
data class CityColorCombination(
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "color") val color: String,
    @PrimaryKey @ColumnInfo(name = "creation_date") val creationDate: Date
): Parcelable