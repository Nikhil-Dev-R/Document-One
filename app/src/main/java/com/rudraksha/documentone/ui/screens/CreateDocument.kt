package com.rudraksha.documentone.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.rudraksha.documentone.data.database.DocumentDatabase
import com.rudraksha.documentone.data.model.DocumentEntity
import com.rudraksha.documentone.ui.navigation.AppNavigationBar
import com.rudraksha.documentone.ui.navigation.AppTopBar
import com.rudraksha.documentone.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDocumentScreen(
    addDocument: (documentEntity: DocumentEntity) -> Unit,
    onDocumentCreated: (documentEntity: DocumentEntity) -> Unit,
    onNavItemClick: (Screen) -> Unit = {},
) {
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var documentName by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Request permissions for storage and camera
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* Handle permission results if needed */ }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }

    // Multi-image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedImages = uris
        }
    }

    // Camera capture launcher – prepare a temporary Uri for the captured image
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && capturedImageUri != null) {
            selectedImages = selectedImages + capturedImageUri!!
        } else {
            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    // A suspend function that “creates” a PDF from the selected images.
    // (This is only a placeholder – integrate your PDF creation logic here.)
    suspend fun createPdfFromImages(imageUris: List<Uri>): File? = withContext(Dispatchers.IO) {
        try {
            if (imageUris.isEmpty()) return@withContext null
            val inputStream = context.contentResolver.openInputStream(imageUris.first())
            val pdfFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "document_${System.currentTimeMillis()}.pdf"
            )
            val outputStream = FileOutputStream(pdfFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            pdfFile
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Create Document",
            )
//            CenterAlignedTopAppBar(
//                title = { Text("Create Document") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
        },
        bottomBar = {
            AppNavigationBar(
                onItemClick = onNavItemClick,
                currentRoute = Screen.Create.toString()
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = documentName,
                    onValueChange = { documentName = it },
                    label = { Text("Document Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text("Select Images")
                    }
                    Button(onClick = {
                        val timeStamp =
                            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                        val imageFile = File(
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "IMG_$timeStamp.jpg"
                        )
                        capturedImageUri = FileProvider.getUriForFile(
                            context,
                            context.packageName + ".provider",
                            imageFile
                        )
                        capturedImageUri?.let {
                            cameraLauncher.launch(capturedImageUri!!)
                        }
                    }) {
                        Text("Capture Image")
                    }
                }

                if (selectedImages.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(selectedImages.size) { index ->
                            // Here you might show a thumbnail (using Coil, for example)
                            Box(
                                modifier = Modifier.size(100.dp)
                            ) {
                                Text("Image ${index + 1}")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (isCreating) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            if (documentName.isBlank() || selectedImages.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please enter document name and select images",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            isCreating = true
                            // Launch background work to create PDF and save document

                            scope.launch {
                                val pdfFile = createPdfFromImages(selectedImages)
                                if (pdfFile != null) {
                                    val currentTime = System.currentTimeMillis()
                                    val documentEntity = DocumentEntity(
                                        fileName = documentName,
                                        filePath = pdfFile.absolutePath,
                                        fileType = "pdf",
                                        createdAt = currentTime
                                    )
                                    try {
                                        addDocument(documentEntity)
                                        Toast.makeText(
                                            context,
                                            "Document created successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        onDocumentCreated(
                                            documentEntity
                                        )

                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Error saving document: ${e.localizedMessage}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error creating PDF",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                isCreating = false
                            }
                        }
                    ) {
                        Text("Create Document")
                    }
                }
            }
        }
    )
}
