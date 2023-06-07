package com.naik.logger

import android.content.Context

object ANLogger: IANLogger {
    override fun setup(context: Context, tag: String, loggingLevel: ANLoggingLevel) {
        val config = ANLoggerConfig.FileProp(context= context, tag=tag, loggingLevel = loggingLevel, fileNamePattern = context.resources.getString(R.string.an_file_formatter))
        ANLoggerHelper.init(config)
    }

    override fun shareLogFile(context: Context)  {
        ANLoggerHelper.openFileSelector(context = context)
    }

    override fun shareAllLogFiles(context: Context) {
        ANLoggerHelper.shareAllLogFiles(context = context)
    }

    override fun clearLogFiles() {
        ANLoggerHelper.clearLogFiles()
    }
}