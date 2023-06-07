package com.example.anlogger

import android.app.Application
import com.naik.logger.ANLogger
import com.naik.logger.ANLoggingLevel

class ApplicationController: Application() {

    val TAG = "IRONMAN"

    override fun onCreate() {
        super.onCreate()
        //Enable ANLogger in debug mode only
        if (BuildConfig.DEBUG) {
            if(BuildConfig.isANFileLoggingEnabled)
                ANLogger.setup(context = this, tag = TAG, loggingLevel =  ANLoggingLevel.AN_FILE_LOGGING)
            else
                ANLogger.setup(context = this, tag = TAG, loggingLevel = ANLoggingLevel.AN_ADB_LOGGING)
        } else {
            //It's a production environment
            //Do not do anything here
        }
    }
}