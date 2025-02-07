package com.rudraksha.documentone.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CodeHighlighter(code: String, language: String = "kotlin") {
    val htmlContent = """
        <html>
        <head>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.6.0/styles/monokai.min.css">
            <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.6.0/highlight.min.js"></script>
            <script>hljs.highlightAll();</script>
        </head>
        <body style="background-color: #2E3440; color: #ECEFF4; font-family: monospace; padding: 16px;">
            <pre><code class="$language">$code</code></pre>
        </body>
        </html>
    """.trimIndent()

    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    }, modifier = Modifier.fillMaxSize())
}

@Preview
@Composable
fun PreviewScreen() {
    val sampleCode = """
        fun main() {
            println("Hello, World!")
        }
    """.trimIndent()

    CodeHighlighter(code = sampleCode, language = "kotlin")
}
