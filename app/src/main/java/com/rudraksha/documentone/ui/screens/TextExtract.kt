package com.rudraksha.documentone.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rudraksha.documentone.ui.navigation.AppNavigationBar
import com.rudraksha.documentone.ui.navigation.AppTopBar
import com.rudraksha.documentone.ui.navigation.Screen
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import android.content.Context
import androidx.compose.material.icons.filled.ContentCopy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import kotlinx.coroutines.Dispatchers

suspend fun extractTextFromImage(
    context: Context,
    bitmap: Bitmap,
    onTextExtracted: (String) -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            val image = InputImage.fromBitmap(bitmap, 0)

            // List of Recognizers for different scripts
            val recognizers = listOf(
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS), // Latin (English, Spanish, etc.)
                TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build()), // Hindi, Sanskrit, Marathi, etc.
//                TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build()), // Chinese
//                TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build()), // Japanese
//                TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()) // Korean
            )

            // Run all recognizers and combine results
            val results = recognizers.map { it.process(image).await() }
            val extractedText = results.joinToString("\n") { it.text }

            withContext(Dispatchers.Main) {
                onTextExtracted(extractedText)  // Return extracted text
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error extracting text: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// Suspend extension to await a Task result.
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    suspendCancellableCoroutine { cont ->
        addOnSuccessListener { result -> cont.resume(result) }
        addOnFailureListener { exception -> cont.resumeWithException(exception) }
    }

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        BitmapFactory.decodeStream(inputStream)
    } ?: throw IllegalArgumentException("Failed to load bitmap from URI: $uri")
}

@Composable
fun TextExtractionScreen(
    onNavItemClick: (Screen) -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var extractedText by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Request storage permission if needed.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }
    LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }

    // Launch text recognition
    LaunchedEffect(selectedImageUri) {
        if (selectedImageUri != null) {
            withContext(Dispatchers.IO) {
                try {
                    val bitmap = getBitmapFromUri(context, selectedImageUri!!)
                    imageBitmap = bitmap.asImageBitmap()
                    extractTextFromImage(
                        context,
                        bitmap,
                        onTextExtracted = { text ->
                            extractedText = text
                        }
                    )
                    isProcessing = false
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error loading image: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            isProcessing = true
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Text Extraction",
            )
        },
        bottomBar = {
            AppNavigationBar(
                onItemClick = onNavItemClick,
                currentRoute = Screen.Extract.toString(),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Show the selected image
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "Select image",
                            modifier = Modifier.size(400.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.size(400.dp)
                        ) {
                            Spacer(modifier = Modifier.height(300.dp))
                            Text(
                                text = "Select Image",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
                if (selectedImageUri != null) {
                    if (isProcessing) {
                        CircularProgressIndicator()
                    } else {
                        if (extractedText.isBlank()) {
                            Text(
                                "Failed to extract text",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Extracted Text", style = MaterialTheme.typography.titleMedium)
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(
                                            androidx.compose.ui.text.AnnotatedString(
                                                extractedText
                                            )
                                        )
                                        Toast.makeText(
                                            context,
                                            "Copied to clipboard",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = "Clear Text",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                            }
                            Text(extractedText)
                        }
                    }
                }
            }
        }
    )
}
