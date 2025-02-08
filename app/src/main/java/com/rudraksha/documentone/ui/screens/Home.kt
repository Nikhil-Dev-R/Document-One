package com.rudraksha.documentone.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rudraksha.documentone.data.database.DocumentDatabase
import com.rudraksha.documentone.data.model.DocumentEntity
import com.rudraksha.documentone.ui.navigation.AppNavigationBar
import com.rudraksha.documentone.ui.navigation.AppTopBar
import com.rudraksha.documentone.ui.navigation.Screen
import com.rudraksha.documentone.viewmodel.DocumentUiState
import com.rudraksha.documentone.viewmodel.DocumentViewModel

@Preview
@Composable
fun HomeScreen(
    uiState: DocumentUiState = DocumentUiState.Success(listOf()),
    onDocumentSelected: (DocumentEntity) -> Unit = {},
    onNavItemClick: (Screen) -> Unit = {},
) {
//    val viewModel: DocumentViewModel = viewModel(
//        factory = DocumentViewModelFactory(repository)
//    )
//    val uiState by viewModel.uiState.collectAsState()
//    val viewModel: DocumentViewModel = viewModel()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Documents"
            )
        },
        bottomBar = {
            AppNavigationBar(
                onItemClick = onNavItemClick,
                currentRoute = Screen.Home.toString(),
            )
        },
        content = { padding ->
            when (uiState) {
                is DocumentUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is DocumentUiState.Error -> {
                    val errorMsg = uiState.message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $errorMsg")
                    }
                }
                is DocumentUiState.Success -> {
                    val documents = uiState.documents
                    if (documents.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No documents available")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            items(documents) { document ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .clickable { onDocumentSelected(document) },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            Text(
                                                text = document.fileName,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = ".${document.fileType}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = document.filePath,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


