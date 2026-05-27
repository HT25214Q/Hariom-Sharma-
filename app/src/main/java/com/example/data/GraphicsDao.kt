package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GraphicsDao {
    // Design Requests queries
    @Query("SELECT * FROM design_requests ORDER BY timestamp DESC")
    fun getAllDesignRequests(): Flow<List<DesignRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDesignRequest(request: DesignRequest): Long

    @Update
    suspend fun updateDesignRequest(request: DesignRequest)

    @Delete
    suspend fun deleteDesignRequest(request: DesignRequest)

    @Query("SELECT * FROM design_requests WHERE id = :id LIMIT 1")
    suspend fun getDesignRequestById(id: Int): DesignRequest?

    // Saved Palettes queries
    @Query("SELECT * FROM saved_palettes ORDER BY timestamp DESC")
    fun getAllSavedPalettes(): Flow<List<SavedPalette>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPalette(palette: SavedPalette): Long

    @Delete
    suspend fun deleteSavedPalette(palette: SavedPalette)
}
