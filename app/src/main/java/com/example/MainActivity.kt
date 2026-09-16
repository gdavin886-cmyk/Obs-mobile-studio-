package com.example

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.example.ui.StudioDashboardScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StudioObsidian
import com.example.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    // Request permissions for camera, audio, and foreground live notifications
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requestPermissions(
        arrayOf(
          android.Manifest.permission.POST_NOTIFICATIONS,
          android.Manifest.permission.CAMERA,
          android.Manifest.permission.RECORD_AUDIO
        ),
        101
      )
    } else {
      requestPermissions(
        arrayOf(
          android.Manifest.permission.CAMERA,
          android.Manifest.permission.RECORD_AUDIO
        ),
        101
      )
    }

    // Configure global Coil ImageLoader with GIF and SVG support (.gif, .svg, .png, .jpg)
    val imageLoader = ImageLoader.Builder(this)
      .components {
        if (Build.VERSION.SDK_INT >= 28) {
          add(ImageDecoderDecoder.Factory())
        } else {
          add(GifDecoder.Factory())
        }
        add(SvgDecoder.Factory())
      }
      .crossfade(true)
      .build()
    Coil.setImageLoader(imageLoader)

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = StudioObsidian
        ) {
          val studioViewModel: StudioViewModel = viewModel()
          StudioDashboardScreen(viewModel = studioViewModel)
        }
      }
    }
  }
}

