package com.naik.logger.ui

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.naik.logger.ui.component.BottomSheetScreen
import com.naik.logger.ui.theme.ComposableBasicsTheme

class ShareLogDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        super.onCreate(savedInstanceState)
        setContent {
            ComposableBasicsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                    color = Color.Transparent.copy(alpha = 0.1f)
                ) {
                    BottomSheetScreen()
                }
            }
        }
        getWindow().setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    ComposableBasicsTheme {
        BottomSheetScreen()
    }
}