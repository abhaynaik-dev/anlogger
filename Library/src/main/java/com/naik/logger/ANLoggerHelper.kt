package com.naik.logger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import com.naik.logger.Constants.Companion.BROADCAST_SHARE_LOG_FILE
import com.naik.logger.Constants.Companion.INTENT_EXTRA_SHARE_LOG_FILE
import com.naik.logger.Constants.Companion.LOGGER_EXCEPTION
import com.naik.logger.exception.ANLoggerException
import com.naik.logger.filesharing.ANFileShare
import com.naik.logger.filesharing.ANShareLogFile
import com.naik.logger.ui.ShareLogDialogActivity
import timber.log.Timber
import java.io.File

internal object ANLoggerHelper {

    private lateinit var logger: ANLoggerConfig

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val file = intent!!.getStringExtra(INTENT_EXTRA_SHARE_LOG_FILE)?.let { File(it) }
            when (intent.action) {
                BROADCAST_SHARE_LOG_FILE -> file?.let {
                    shareSelectedLogFile(
                        context = contxt!!, file = it
                    )
                }
            }
        }
    }

    fun init(config: ANLoggerConfig) {
        logger = config
        if (config.loggingLevel == ANLoggingLevel.AN_ADB_LOGGING) {
            Timber.plant(Timber.DebugTree())
        } else if (config.loggingLevel == ANLoggingLevel.AN_FILE_LOGGING) {
            Timber.plant(ANFileWritingTree(logger))
        }
    }

    fun clearLogFiles() {
        if (isFileLoggingEnabled()) logger.clearLogFiles()
    }

    fun openFileSelector(context: Context) {
        if (isFileLoggingEnabled() && isLogFileExist((context))) {
            context.registerReceiver(broadCastReceiver, IntentFilter(BROADCAST_SHARE_LOG_FILE))
            context.startActivity(Intent(context, ShareLogDialogActivity::class.java))
        }
    }

    //To get all log files
    fun shareAllLogFiles(context: Context) {
        if (isFileLoggingEnabled() && isLogFileExist((context))) {
            val shareFileConfig = ANLoggerConfig.ANFileShareConfig.ShareFileSetup(
                context = context, filesToAppend = logger.getAllExistingLogFiles()
            )
            shareLogFile(shareFileConfig, context)
        }
    }

    //Share selected log files
    private fun shareSelectedLogFile(context: Context, file: File) {
        val shareFileConfig =
            ANLoggerConfig.ANFileShareConfig.ShareFileSetup(context = context, logFile = file)
        shareLogFile(shareFileConfig, context)
        context.unregisterReceiver(broadCastReceiver)
    }

    //Share log files over email
    private fun shareLogFile(shareFileSetup: ANLoggerConfig.ANFileShareConfig, context: Context) {
        if (isFileLoggingEnabled()) {
            val allFiles = shareFileSetup.filesToAppend.toMutableList()
            shareFileSetup.logFile?.let { allFiles.add(0, it) }
            val fileShare = ANShareLogFile(listOf(shareFileSetup.receiver),
                shareFileSetup.appVersionName,
                attachments = allFiles.map { ANFileShare.DefaultName(it) })
            fileShare.startEmailChooser(context, shareFileSetup.titleForChooser)
        }
    }

    internal fun getAllLogFiles(): List<File> {
        return logger.getAllExistingLogFiles()
    }

    private fun getLatestFile(): File? {
        return if (isFileLoggingEnabled()) {
            logger.getLatestLogFiles()
        } else {
            null
        }
    }

    private fun isFileLoggingEnabled(): Boolean {
        if (logger.loggingLevel == ANLoggingLevel.AN_FILE_LOGGING) {
            return true
        } else {
            throw ANLoggerException(LOGGER_EXCEPTION)
        }
    }

    private fun isLogFileExist(context: Context): Boolean {
        val listOfFiles = logger.getAllExistingLogFiles()
        return if (listOfFiles.isNotEmpty()) {
            true
        } else {
            Toast.makeText(
                context,
                context.resources.getString(R.string.an_error_file_does_not_exist),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

}