package com.pburdelak.randomcityapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pburdelak.randomcityapp.model.CityColorCombination

@Database(
    entities = [
        CityColorCombination::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityColorCombinationDao(): CityColorCombinationDao

    companion object {
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "rca-database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}