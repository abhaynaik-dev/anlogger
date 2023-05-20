package com.naik.logger

import android.content.Context

object ANLogger: IANLogger {
    override fun setup(context: Context, tag: String, loggingLevel: ANLoggingLevel) {
        val config = ANLoggerConfig.FileProp(context= context, tag=tag, loggingLevel = loggingLevel)
        ANLoggerHelper.init(config)
        ANLoggerHelper.clearLogFiles()
    }

    override fun shareLogFile(context: Context) {
        val shareFileConfig = ANLoggerConfig.ANFileShareConfig.ShareFileSetup(context, ANLoggerHelper.getLatestFile())
        ANLoggerHelper.shareLogFile(shareFileConfig, context)
    }
}