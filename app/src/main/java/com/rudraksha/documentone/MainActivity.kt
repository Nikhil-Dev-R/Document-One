package com.rudraksha.documentone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.rudraksha.documentone.ui.DocumentApp
import com.rudraksha.documentone.ui.theme.DocumentOneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DocumentOneTheme {
                DocumentApp(
                    context = applicationContext,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DocumentOneTheme {
        DocumentApp(context = LocalContext.current)
    }
}