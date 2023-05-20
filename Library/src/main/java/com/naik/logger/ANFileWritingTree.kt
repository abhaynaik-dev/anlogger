package com.naik.logger

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.*
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import timber.log.Timber

internal class ANFileWritingTree (config: ANLoggerConfig?): Timber.Tree() {

    private var handlerThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null

    init {
        if (config == null){
            throw RuntimeException("You can not plant without proper configuration")
        }

        if (config.logOnBackgroundThread) {
            handlerThread = HandlerThread("ANHandler").apply {
                start()
                backgroundHandler = Handler(looper)
            }
        }
        configureMyTree(config)
    }

    private fun configureMyTree(config: ANLoggerConfig){
        val lc = LoggerFactory.getILoggerFactory() as LoggerContext
        lc.reset()

        //https://github.com/tony19/logback-android/wiki
        // 1) FileLoggingSetup - Encoder for File
        val encoder1 = PatternLayoutEncoder()
        encoder1.context = lc
        encoder1.pattern = config.setup.logPattern
        encoder1.start()

        // 2) FileLoggingSetup - rolling file appender
        val rollingFileAppender = RollingFileAppender<ILoggingEvent>()
        rollingFileAppender.isAppend = true
        rollingFileAppender.context = lc

        var triggeringPolicy: TriggeringPolicy<ILoggingEvent>? = null
        val timeBasedRollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>()
        timeBasedRollingPolicy.fileNamePattern =
            config.folder + "/" + config.setup.fileName + "_%d{yyyyMMdd}." + config.setup.fileExtension
        timeBasedRollingPolicy.maxHistory = config.setup.logsToKeep
        timeBasedRollingPolicy.isCleanHistoryOnStart = true
        timeBasedRollingPolicy.setParent(rollingFileAppender)
        timeBasedRollingPolicy.context = lc

        triggeringPolicy = timeBasedRollingPolicy

        // Let's begin
        triggeringPolicy.start()

        rollingFileAppender.triggeringPolicy = triggeringPolicy
        rollingFileAppender.encoder = encoder1
        rollingFileAppender.start()

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger
        val root = mLogger as ch.qos.logback.classic.Logger
        root.detachAndStopAllAppenders()
        root.addAppender(rollingFileAppender)

        // enable all log level
        root.level = Level.ALL
    }

    companion object {
        const val DATE_FILE_NAME_PATTERN = "%s_\\d{8}.%s"
        internal var mLogger =
            LoggerFactory.getLogger(ANFileWritingTree::class.java)//Logger.ROOT_LOGGER_NAME);
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        backgroundHandler?.post { doRealLog(priority, message) } ?: doRealLog(priority, message)
    }

    private val TD_MARKER = MarkerFactory.getMarker(config?.tag)

    private fun doRealLog(priority: Int, logMessage: String) {
        when (priority) {
            Log.VERBOSE -> mLogger.trace(TD_MARKER, logMessage)
            Log.DEBUG -> mLogger.debug(TD_MARKER, logMessage)
            Log.INFO -> mLogger.info(TD_MARKER, logMessage)
            Log.WARN -> mLogger.warn(TD_MARKER, logMessage)
            Log.ERROR -> mLogger.error(TD_MARKER, logMessage)
            Log.ASSERT -> mLogger.error(TD_MARKER, logMessage)
        }
    }
}