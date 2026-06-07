package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "presets")
data class AudioPreset(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val bassPunch: Float,
    val spatialField: Float,
    val eqDataString: String // comma separated levels
)

@Dao
interface PresetDao {
    @Query("SELECT * FROM presets")
    fun getAllPresets(): Flow<List<AudioPreset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreset(preset: AudioPreset)

    @Query("DELETE FROM presets WHERE id = :id")
    suspend fun deletePreset(id: Int)
}
