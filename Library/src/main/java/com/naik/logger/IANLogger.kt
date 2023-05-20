package com.naik.logger

import android.content.Context

internal interface IANLogger {
    fun setup(context: Context, tag: String, loggingLevel: ANLoggingLevel)
    fun shareLogFile(context: Context)
}