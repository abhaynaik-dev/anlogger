package com.naik.logger.ui

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.naik.logger.ui.component.BottomSheetScreen

class ShareLogDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        super.onCreate(savedInstanceState)
        setContent {
            BottomSheetScreen()
        }
        getWindow().setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
