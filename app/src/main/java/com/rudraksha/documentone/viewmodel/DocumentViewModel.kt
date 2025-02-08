package com.rudraksha.documentone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.documentone.data.model.DocumentEntity
import com.rudraksha.documentone.data.repo.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DocumentUiState {
    data object Loading : DocumentUiState()
    data class Success(val documents: List<DocumentEntity>) : DocumentUiState()
    data class Error(val message: String) : DocumentUiState()
}

class DocumentViewModel(private val repository: DocumentRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DocumentUiState>(DocumentUiState.Loading)
    val uiState: StateFlow<DocumentUiState> = _uiState

    init { fetchDocuments() }

    fun fetchDocuments() {
        viewModelScope.launch {
            try {
                _uiState.value = DocumentUiState.Loading
                val docs = repository.getAllDocuments()
                _uiState.value = DocumentUiState.Success(docs)
            } catch (e: Exception) {
                _uiState.value = DocumentUiState.Error("Error loading documents: ${e.localizedMessage}")
            }
        }
    }

    fun addDocument(document: DocumentEntity) {
        viewModelScope.launch {
            try {
                repository.insertDocument(document)
                fetchDocuments() // refresh list after insert
            } catch (e: Exception) {
                _uiState.value = DocumentUiState.Error("Error adding document: ${e.localizedMessage}")
            }
        }
    }

    fun deleteDocument(docId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteDocument(docId)
                fetchDocuments()
            } catch (e: Exception) {
                _uiState.value = DocumentUiState.Error("Error deleting document: ${e.localizedMessage}")
            }
        }
    }
}
