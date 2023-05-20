package com.example.anlogger

import android.app.Application
import com.naik.logger.ANLogger
import com.naik.logger.ANLoggingLevel

class ApplicationController: Application() {
    override fun onCreate() {
        super.onCreate()
        ANLogger.setup(this, "IRONMAN" , ANLoggingLevel.AN_FILE_LOGGING)
    }
}