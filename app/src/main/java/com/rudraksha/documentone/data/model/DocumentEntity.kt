package com.rudraksha.documentone.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "documents")
data class DocumentEntity(
    @Serializable
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Serializable
    val fileName: String,

    @Serializable
    val filePath: String,

    @Serializable
    val fileType: String,  // e.g., "pdf", "docx", "jpg", "c", etc.

    @Serializable
    val createdAt: Long = System.currentTimeMillis()
)
