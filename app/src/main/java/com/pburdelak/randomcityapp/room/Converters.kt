package com.pburdelak.randomcityapp.room

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time
}