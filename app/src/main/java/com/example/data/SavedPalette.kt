package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_palettes")
data class SavedPalette(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val colorsString: String, // e.g. "#00B4D8,#219EBC,#FB8500,#FFB703"
    val timestamp: Long = System.currentTimeMillis()
)
