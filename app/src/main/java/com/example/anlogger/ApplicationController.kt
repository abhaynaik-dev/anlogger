package com.example.anlogger

import android.app.Application
import com.naik.logger.ANLogger
import com.naik.logger.ANLoggingLevel

class ApplicationController: Application() {
    override fun onCreate() {
        super.onCreate()
        //Enable ANLogger in debug mode only
        if (BuildConfig.DEBUG) {
            if(BuildConfig.isANFileLoggingEnabled)
                ANLogger.setup(context = this, tag = "IRONMAN" , ANLoggingLevel.AN_FILE_LOGGING)
            else
                ANLogger.setup(context = this, loggingLevel = ANLoggingLevel.AN_ADB_LOGGING)
        } else {
            //Do not do anything here
            //It's a production environment, let's not touch it
        }
    }
}