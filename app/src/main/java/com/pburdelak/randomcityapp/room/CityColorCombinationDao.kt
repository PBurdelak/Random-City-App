package com.pburdelak.randomcityapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pburdelak.randomcityapp.model.CityColorCombination

@Dao
interface CityColorCombinationDao {

    @Query("SELECT * FROM city_color_combination ORDER BY city, creation_date")
    fun getAll(): List<CityColorCombination>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: CityColorCombination)

    @Query("DELETE FROM city_color_combination")
    fun deleteAll()
}