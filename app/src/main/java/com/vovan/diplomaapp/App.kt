package com.vovan.diplomaapp

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder().build()

}