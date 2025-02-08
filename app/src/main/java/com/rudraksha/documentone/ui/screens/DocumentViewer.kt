package com.rudraksha.documentone.ui.screens

import android.content.Intent
import android.net.Uri.fromFile
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rudraksha.documentone.data.model.DocumentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentViewerScreen(
    document: DocumentEntity,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(document.fileName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // File sharing via an Intent
                        val file = File(document.filePath)
                        val uri = fromFile(file)
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "*/*"
                            putExtra(Intent.EXTRA_STREAM, uri)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Document"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        },
        content = { padding ->

            when (document.fileType.lowercase()) {
                "pdf" -> {
                    // Display PDF using AndroidView with the PDFView library.
//                        AndroidView(
//                            factory = { ctx ->
//                                PdfViewer.getScreen()
//                                com.github.barteksc.pdfviewer.PDFView(ctx, null).apply {
//                                    fromFile(File(document!!.filePath))
//                                        .load()
//                                }
//                            },
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(padding)
//                        )
                }

                "doc", "docx", "xls", "xlsx", "ppt", "pptx" -> {
                    // Open Office files externally
                    val file = File(document.filePath)
                    LaunchedEffect(file) {
                        try {
                            val uri = fromFile(file)
                            val intent = Intent(Intent.ACTION_VIEW)
                            val mimeType = when (document.fileType.lowercase()) {
                                "doc", "docx" -> "application/msword"
                                "xls", "xlsx" -> "application/vnd.ms-excel"
                                "ppt", "pptx" -> "application/vnd.ms-powerpoint"
                                else -> "*/*"
                            }
                            intent.setDataAndType(uri, mimeType)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "No application found to open this file",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Opening document in external viewer...")
                    }
                }

                "txt", "c", "cpp", "kt", "java" -> {
                    // For text or code files, read the file and display the text
                    var textContent by remember { mutableStateOf("") }
                    LaunchedEffect(document) {
                        try {
                            textContent = withContext(Dispatchers.IO) {
                                File(document!!.filePath).readText()
                            }
                        } catch (e: Exception) {
                            textContent = "Error loading text: ${e.localizedMessage}"
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(text = textContent)
                    }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Unsupported file type")
                    }
                }
            }
        }
    )
}
