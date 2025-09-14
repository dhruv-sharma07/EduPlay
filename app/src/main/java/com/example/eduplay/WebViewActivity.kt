package com.example.eduplay

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eduplay.ui.theme.EduPlayTheme
import com.example.eduplay.webview.WebViewDataBridge

class WebViewActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WebViewContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                
                // Enable JavaScript
                settings.javaScriptEnabled = true
                
                // Enable DOM storage
                settings.domStorageEnabled = true
                
                // Enable local storage
                settings.databaseEnabled = true
                
                // Enable file access
                settings.allowFileAccess = true
                settings.allowContentAccess = true
                
                // Enable mixed content
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                
                // Set cache mode
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                
                // Enable zoom controls
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                
                // Set user agent
                settings.userAgentString = settings.userAgentString + " EduPlayApp"
                
                // Initialize database bridge
                val dataBridge = WebViewDataBridge(context, this)
                dataBridge.injectJavaScriptInterface()
                
                // Load the HTML file from assets
                loadUrl("file:///android_asset/index.html")
            }
        },
        modifier = modifier.fillMaxSize()
    )
}
