package com.naik.logger

import android.content.Context
import com.naik.logger.exception.ANLoggerException
import com.naik.logger.filesharing.ANFileShare
import com.naik.logger.filesharing.ANShareLogFile
import timber.log.Timber
import java.io.File


internal object ANLoggerHelper {

    private var logger: ANLoggerConfig? = null

    fun init(config: ANLoggerConfig) {
        if(config.loggingLevel == ANLoggingLevel.AN_ADB_LOGGING){
            Timber.plant(Timber.DebugTree())
        }else if (config.loggingLevel == ANLoggingLevel.AN_FILE_LOGGING){
            logger = config
            Timber.plant(ANFileWritingTree(logger))
        }
    }

    fun clearLogFiles() {
        if (logger != null){
            logger?.clearLogFiles()
        }else{
            throw ANLoggerException("Have you disabled file logging?")
        }
    }

    fun shareLogFile(shareFileSetup: ANLoggerConfig.ANFileShareConfig, context: Context){
        if(logger != null){
            val allFiles = shareFileSetup.filesToAppend.toMutableList()
            shareFileSetup.logFile?.let { allFiles.add(0, it) }
            val fileShare = ANShareLogFile(
                listOf(shareFileSetup.receiver),
                shareFileSetup.appVersionName,
                attachments = allFiles.map { ANFileShare.DefaultName(it) }
            )
            fileShare.startEmailChooser(context, shareFileSetup.titleForChooser)
        }else {
            throw ANLoggerException("Have you disabled file logging?")
        }
    }

    fun getLatestFile(): File?{
        if (logger != null){
            return logger?.getLatestLogFiles()
        }else{
            throw ANLoggerException("Have you disabled file logging?")
        }
    }
}