package com.naik.logger.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "TimberArgCount"
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetScreen() {

    val TAG = "BottomSheetScreen"
    val context: Context = LocalContext.current

    val bottomSheetEvent = remember { mutableStateOf (BottomSheetEvent.SHOW) }
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxWidth(),
        sheetBackgroundColor = Color.White,
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            Surface(
                color = Color.White
            ) {
                BottomSheetContent(
                    onClosBottomSheet = {
                        coroutineScope.launch {
                            handleBottomSheetState(
                                bottomSheetEvent = bottomSheetEvent,
                                modalSheetState = modalSheetState,
                                context = context
                            )
                        }
                    })
            }
        }
    ) {}

    // Take action based on hidden state
    LaunchedEffect(modalSheetState.currentValue) {
        when (modalSheetState.currentValue) {
            ModalBottomSheetValue.Hidden -> {
                handleBottomSheetState(
                    bottomSheetEvent = bottomSheetEvent,
                    modalSheetState = modalSheetState,
                    context = context
                )
            }
            else -> {
                bottomSheetEvent.value = BottomSheetEvent.HIDE
                Timber.tag(TAG).i("%s state", "Bottom sheet %s", modalSheetState.currentValue)
            }
        }
    }

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch {
            handleBottomSheetState(
                bottomSheetEvent = bottomSheetEvent,
                modalSheetState = modalSheetState,
                context = context
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private suspend fun handleBottomSheetState(
    bottomSheetEvent: MutableState<BottomSheetEvent>,
    modalSheetState: ModalBottomSheetState,
    context: Context
){
    if(bottomSheetEvent.value == BottomSheetEvent.SHOW) {
        modalSheetState.show()
    }
    //This means user has either clicked outside of the sheet
    else if (bottomSheetEvent.value == BottomSheetEvent.HIDE){
        modalSheetState.hide()
        (context as Activity).finish()
    }
}


enum class BottomSheetEvent  {
    NONE,
    SHOW,
    HIDE
}


