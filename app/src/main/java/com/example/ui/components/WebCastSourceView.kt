package com.example.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.dialogs.WebCastUrlDialog
import com.example.ui.theme.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebCastSourceView(
    url: String,
    modifier: Modifier = Modifier,
    isTouchInteractiveDefault: Boolean = true,
    onUrlChange: (String) -> Unit = {}
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var fullScreenWebViewInstance by remember { mutableStateOf<WebView?>(null) }
    var currentTitle by remember { mutableStateOf("Broadcasting Web Source") }
    var isLoading by remember { mutableStateOf(false) }
    var isTouchInteractive by remember { mutableStateOf(isTouchInteractiveDefault) }
    var isFullScreen by remember { mutableStateOf(false) }
    var showUrlEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkSurface)
    ) {
        // Mini Web Browser Top Bar for the stream
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(StudioObsidian)
                .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Web Cast",
                tint = StudioCyan,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = url,
                color = Color(0xFFCBD5E1),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .clickable { showUrlEditDialog = true }
            )

            // Touch Mode Indicator Button
            IconButton(
                onClick = { isTouchInteractive = !isTouchInteractive },
                modifier = Modifier
                    .size(24.dp)
                    .testTag("toggle_web_touch_btn")
            ) {
                Icon(
                    imageVector = if (isTouchInteractive) Icons.Default.TouchApp else Icons.Default.DoNotTouch,
                    contentDescription = if (isTouchInteractive) "Touch Interactive" else "Touch Locked",
                    tint = if (isTouchInteractive) StudioNeonGreen else StudioAmber,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Quick Scroll Down
            IconButton(
                onClick = { webViewInstance?.scrollBy(0, 350) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Scroll Down",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Quick Scroll Up
            IconButton(
                onClick = { webViewInstance?.scrollBy(0, -350) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll Up",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = StudioCyan
                )
            } else {
                IconButton(
                    onClick = { webViewInstance?.reload() },
                    modifier = Modifier
                        .size(24.dp)
                        .testTag("refresh_web_cast_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Page",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // FULL SCREEN BUTTON
            IconButton(
                onClick = { isFullScreen = true },
                modifier = Modifier
                    .size(24.dp)
                    .testTag("webcast_fullscreen_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Full Screen Webcast",
                    tint = StudioCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Android WebView displaying actual web page
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.setSupportZoom(true)
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                isLoading = true
                            }
                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                                currentTitle = view?.title ?: "Broadcasting Web Source"
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                isLoading = newProgress < 100
                            }
                        }
                        // Disable hardware acceleration to prevent rendernode crashes on some emulators/devices
                        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                        loadUrl(url)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    if (webView.url != url) {
                        webView.loadUrl(url)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // If touch interaction is turned OFF, overlay transparent layer to block accidental touches
            if (!isTouchInteractive) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .clickable(enabled = false) {}
                )
            }

            // Transparent badge showing live status & full screen trigger
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xCC000000))
                    .border(0.5.dp, StudioCyan, RoundedCornerShape(4.dp))
                    .clickable { isFullScreen = true }
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = null,
                    tint = StudioCyan,
                    modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isTouchInteractive) "WEB LIVE • TOUCH ON" else "WEB LIVE • LOCKED",
                    color = if (isTouchInteractive) StudioNeonGreen else StudioAmber,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // FULL SCREEN WEBCAST INTERFACE
    if (isFullScreen) {
        Dialog(
            onDismissRequest = { isFullScreen = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(StudioObsidian)
                    .testTag("webcast_fullscreen_container")
            ) {
                // Full Screen WebView
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.setSupportZoom(true)
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isLoading = false
                                }
                            }
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    isLoading = newProgress < 100
                                }
                            }
                            // Disable hardware acceleration to prevent rendernode crashes on some emulators/devices
                            setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                            loadUrl(url)
                            fullScreenWebViewInstance = this
                        }
                    },
                    update = { webView ->
                        if (webView.url != url) {
                            webView.loadUrl(url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // If touch is locked in fullscreen, intercept touches
                if (!isTouchInteractive) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                            .clickable(enabled = false) {}
                    )
                }

                // Top Floating Control Deck for Web Scrolling, Zooming, Navigation
                Card(
                    shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface.copy(alpha = 0.95f)),
                    border = BorderStroke(1.dp, StudioCyan.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Close Full Screen Button
                            IconButton(
                                onClick = { isFullScreen = false },
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(StudioCardBg)
                                    .border(1.dp, StudioCyan, CircleShape)
                                    .testTag("exit_fullscreen_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FullscreenExit,
                                    contentDescription = "Exit Full Screen",
                                    tint = StudioCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Web URL & Title
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showUrlEditDialog = true }
                            ) {
                                Text(
                                    text = "FULL SCREEN WEB CAST",
                                    color = StudioCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = url,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // Touch Interaction Toggle Chip
                            FilterChip(
                                selected = isTouchInteractive,
                                onClick = { isTouchInteractive = !isTouchInteractive },
                                label = {
                                    Text(
                                        text = if (isTouchInteractive) "TOUCH: ON" else "TOUCH: OFF",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (isTouchInteractive) Icons.Default.TouchApp else Icons.Default.DoNotTouch,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0F3A22),
                                    selectedLabelColor = StudioNeonGreen,
                                    selectedLeadingIconColor = StudioNeonGreen,
                                    containerColor = StudioCardBg,
                                    labelColor = TextMuted
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isTouchInteractive,
                                    borderColor = if (isTouchInteractive) StudioNeonGreen else StudioCardBorder
                                ),
                                modifier = Modifier.height(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Controls Toolbar: Navigation, Scrolling, Zooming
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            // Browser Back
                            OutlinedButton(
                                onClick = {
                                    if (fullScreenWebViewInstance?.canGoBack() == true) {
                                        fullScreenWebViewInstance?.goBack()
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Browser Forward
                            OutlinedButton(
                                onClick = {
                                    if (fullScreenWebViewInstance?.canGoForward() == true) {
                                        fullScreenWebViewInstance?.goForward()
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Forward",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Reload Page
                            OutlinedButton(
                                onClick = { fullScreenWebViewInstance?.reload() },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reload",
                                    tint = StudioCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Reload", fontSize = 9.sp, color = StudioCyan)
                            }

                            // Scroll To Top
                            Button(
                                onClick = { fullScreenWebViewInstance?.scrollTo(0, 0) },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCardBg),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerticalAlignTop,
                                    contentDescription = "Top",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Top", fontSize = 9.sp, color = Color.White)
                            }

                            // Scroll Up
                            Button(
                                onClick = { fullScreenWebViewInstance?.scrollBy(0, -400) },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCardBg),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Scroll Up",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Scroll ▲", fontSize = 9.sp, color = Color.White)
                            }

                            // Scroll Down
                            Button(
                                onClick = { fullScreenWebViewInstance?.scrollBy(0, 400) },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCardBg),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Scroll Down",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Scroll ▼", fontSize = 9.sp, color = Color.White)
                            }

                            // Zoom In
                            OutlinedButton(
                                onClick = { fullScreenWebViewInstance?.zoomIn() },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ZoomIn,
                                    contentDescription = "Zoom In",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Zoom Out
                            OutlinedButton(
                                onClick = { fullScreenWebViewInstance?.zoomOut() },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, StudioCardBorder),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ZoomOut,
                                    contentDescription = "Zoom Out",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom Floating Live Indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xE60F172A))
                        .border(1.dp, StudioCyan, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StudioLiveRed)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LIVE WEBCAST INGEST ACTIVE",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isTouchInteractive) "• TOUCH ACTIVE" else "• TOUCH FROZEN",
                            color = if (isTouchInteractive) StudioNeonGreen else StudioAmber,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }

    if (showUrlEditDialog) {
        WebCastUrlDialog(
            currentUrl = url,
            onSaveUrl = {
                onUrlChange(it)
                showUrlEditDialog = false
            },
            onDismiss = { showUrlEditDialog = false }
        )
    }
}
