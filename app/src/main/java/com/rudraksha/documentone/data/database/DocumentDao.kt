package com.rudraksha.documentone.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rudraksha.documentone.data.model.DocumentEntity

@Dao
interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: DocumentEntity): Long

    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    suspend fun getAllDocuments(): List<DocumentEntity>

    @Query("SELECT * FROM documents WHERE id = :docId")
    suspend fun getDocumentById(docId: Int): DocumentEntity?

    @Query("SELECT * FROM documents WHERE createdAt = :createdAt")
    suspend fun getDocumentAtCreation(createdAt: Long): DocumentEntity?

    @Query("DELETE FROM documents WHERE id = :docId")
    suspend fun deleteDocument(docId: Int)
}
