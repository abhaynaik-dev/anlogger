package com.example.anlogger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.naik.logger.ANLogger
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var tvCounter: TextView
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvCounter = findViewById(R.id.textView3)
    }

    fun onIncrementCounter(view: View){
        updateCounterView(++counter)
        Timber.d("Counter has incremented to $counter")
    }

    fun onShareLogFile(view: View){
        ANLogger.shareLogFile(context = this)
    }

    fun onShareAllLogFiles(view: View){
        ANLogger.shareAllLogFiles(context = this)
    }

    fun onClearLogFiles(view: View){
        ANLogger.clearLogFiles()
        reset()
    }

    private fun updateCounterView(counter: Int){
        tvCounter.setText("Counter: $counter")
    }

    private fun reset(){
        counter = 0
        updateCounterView(counter)
    }

}