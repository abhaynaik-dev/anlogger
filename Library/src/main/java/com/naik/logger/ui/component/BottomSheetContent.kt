package com.naik.logger.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naik.logger.ANLoggerHelper
import com.naik.logger.Constants
import com.naik.logger.R
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BottomSheetContent(onClosBottomSheet: () -> Unit) {
    val context: Context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            TitleWithCloseButton(
                title = stringResource(id = R.string.an_dialog_title),
                onClosBottomSheet = onClosBottomSheet
            )
            AddBody(context = context, onClosBottomSheet = onClosBottomSheet)
            BottomButtons(onClosBottomSheet = onClosBottomSheet)
        }
    }
}

@Composable
fun BodyContent(context: Context, closBottomSheet: () -> Unit) {
    val listOfLogFiles = ANLoggerHelper.getAllLogFiles()!!
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            itemsIndexed(items = listOfLogFiles) { index, file ->
                ListItem(file = file, background = Color.White,
                    onItemClick = {
                        Intent().also { intent ->
                            intent.action = Constants.BROADCAST_SHARE_LOG_FILE
                            intent.putExtra(Constants.INTENT_EXTRA_SHARE_LOG_FILE, it.absolutePath)
                            context.sendBroadcast(intent)
                        }
                        closBottomSheet()
                    })
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun ListItem(file: File, background: Color, onItemClick: (File) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(background)
            .padding(bottom = 5.dp)
            .clickable {
                onItemClick(file)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_file_share),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(start = 5.dp, end = 5.dp),
        )

        val fileNameForDateFormat =
            file.nameWithoutExtension.substring(4, file.nameWithoutExtension.length)
        val localDateTime = LocalDateTime.parse(
            fileNameForDateFormat,
            DateTimeFormatter.ofPattern(stringResource(id = R.string.an_file_formatter))
        )
        val formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"))

        Text(text = formattedDateTime)
    }
}

@Composable
private fun TitleWithCloseButton(title: String, onClosBottomSheet: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp),
            textAlign = TextAlign.Start,
            text = title,
            fontSize = 18.sp,
            color = Color.DarkGray,
        )

        CloseButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        ) {
            onClosBottomSheet()
        }
    }
}

@Composable
private fun AddBody(context: Context, onClosBottomSheet: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .wrapContentSize()
    ) {
        BodyContent(context = context, onClosBottomSheet)
    }
}

@Composable
private fun BottomButtons(onClosBottomSheet: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.White)
            .padding(start = 5.dp, bottom = 5.dp)
            .clickable {
                ANLoggerHelper.clearLogFiles()
                onClosBottomSheet()
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(start = 5.dp, end = 5.dp),
        )
        Text(
            text = stringResource(id = R.string.an_dialog_bottom_button),
            color = Color.DarkGray
        )
    }
}
