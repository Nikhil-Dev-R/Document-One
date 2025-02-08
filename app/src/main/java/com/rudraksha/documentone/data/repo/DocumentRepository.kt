package com.rudraksha.documentone.data.repo

import com.rudraksha.documentone.data.database.DocumentDao
import com.rudraksha.documentone.data.model.DocumentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DocumentRepository(private val documentDao: DocumentDao) {

    suspend fun insertDocument(document: DocumentEntity): Long = withContext(Dispatchers.IO) {
        documentDao.insert(document)
    }

    suspend fun getAllDocuments(): List<DocumentEntity> = withContext(Dispatchers.IO) {
        documentDao.getAllDocuments()
    }

    suspend fun getDocumentById(docId: Int): DocumentEntity? = withContext(Dispatchers.IO) {
        documentDao.getDocumentById(docId)
    }

    suspend fun getDocumentAtCreation(createdAt: Long): DocumentEntity? = withContext(Dispatchers.IO) {
        documentDao.getDocumentAtCreation(createdAt)
    }

    suspend fun deleteDocument(docId: Int) = withContext(Dispatchers.IO) {
        documentDao.deleteDocument(docId)
    }
}
