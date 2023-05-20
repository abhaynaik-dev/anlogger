package com.naik.logger

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.io.PrintWriter
import java.util.regex.Pattern

enum class ANLoggingLevel() {
    AN_NONE,
    AN_FILE_LOGGING,
    AN_ADB_LOGGING
}

sealed class ANLoggerConfig {

    abstract val pattern: String
    abstract val folder: String
    abstract val logOnBackgroundThread: Boolean
    abstract val setup: Setup
    abstract val tag: String
    abstract val loggingLevel : ANLoggingLevel

    @kotlinx.parcelize.Parcelize
    class FileProp(
        override val folder: String,
        override val logOnBackgroundThread: Boolean,
        override val setup: Setup,
        override val tag: String,
        override val loggingLevel: ANLoggingLevel
    ) : ANLoggerConfig(), Parcelable {

        constructor(context: Context,
                    folder: String = context.getFileStreamPath("").absolutePath,
                    logOnBackgroundThread: Boolean = true,
                    setup: Setup = Setup(),
                    tag: String = "AN ",
                    loggingLevel: ANLoggingLevel = ANLoggingLevel.AN_NONE) : this(folder, logOnBackgroundThread, setup, tag, loggingLevel)

        override val pattern = String.format(
            ANFileWritingTree.DATE_FILE_NAME_PATTERN,
            setup.fileName,
            setup.fileExtension
        )
    }

    @Parcelize
    class Setup(
        val logsToKeep: Int = 7,
        val logPattern: String = "%d [%marker] %-5level - %msg%n",
        val fileName: String = "log",
        val fileExtension: String = "log"
    ) : Parcelable

    fun getAllExistingLogFiles() = getFilesInFolder().filter { Pattern.matches(pattern, it.name) }
    fun getLatestLogFiles() = getAllExistingLogFiles().sortedByDescending { it.lastModified() }.firstOrNull()

    protected fun getFilesInFolder(): List<File> {
        val folder = File(folder)
        if (!folder.exists()) {
            return emptyList()
        }
        return folder.listFiles()?.filter { it.isFile } ?: emptyList()
    }

    fun clearLogFiles() {
        val newestFile = getLatestLogFiles()
        val filesToDelete = getAllExistingLogFiles().filter { it != newestFile }
        filesToDelete.forEach {
            it.delete()
        }
        newestFile?.let {
            val writer = PrintWriter(it)
            writer.print("")
            writer.close()
        }
    }

    internal sealed class ANFileShareConfig {

        abstract val logFile: File?
        abstract val receiver: String
        abstract val subject: String
        abstract val titleForChooser: String
        abstract val filesToAppend: List<File>
        abstract val appVersionName: String

        class ShareFileSetup(
            override val logFile: File?,
            override val receiver: String,
            override val subject: String,
            override val titleForChooser: String,
            override val filesToAppend: List<File>,
            override val appVersionName: String
        ) : ANFileShareConfig() {
            constructor(context: Context,
                        logFile: File?,
                        receiver: String = "Mail",
                        subject: String = "Log file for ${context.packageName}",
                        titleForChooser: String = "Share logs with",
                        filesToAppend: List<File> = emptyList(),
                        appVersionName: String = ANUtil.getRealSubject(context, subject) )
                        : this( logFile, receiver, subject, titleForChooser, filesToAppend, appVersionName)
        }
    }

}



