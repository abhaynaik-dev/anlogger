package com.naik.logger

import android.content.Context
import com.naik.logger.exception.ANLoggerException

internal interface IANLogger {
    fun setup(context: Context, tag: String = context.packageName, loggingLevel: ANLoggingLevel)
    fun shareLogFile(context: Context)
    fun shareAllLogFiles(context: Context)
    fun clearLogFiles()
}