package com.vovan.diplomaapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vovan.diplomaapp.BuildConfig
import com.vovan.diplomaapp.R
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}