package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "design_requests")
data class DesignRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val designType: String,       // e.g., "Logo", "Business Card", "Social Banner", "Vector Portrait", "App Mockup"
    val businessName: String,     // Text copy (e.g., "Hariom's Cafe")
    val tagline: String,          // Tagline copy (e.g., "Freshly Brewed Coding")
    val styleSelected: String,    // e.g., "Neon Cyberpunk", "Minimalist Slate", "Royal Retro", "Modern Brutalist"
    val complexity: String,       // e.g., "Standard Effort", "Detailed Artistic", "Creative Masterpiece"
    val notes: String,            // custom instructions
    val colorsString: String,     // comma-separated list of hex values (e.g. "#FFB703,#FB8500")
    val estimatedPrice: Double,   // Calculated based on options
    val status: String = "Draft", // "Draft", "Submitted", "Reviewing", "In Progress", "Completed"
    val timestamp: Long = System.currentTimeMillis()
)
