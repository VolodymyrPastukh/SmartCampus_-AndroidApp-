package com.vovan.diplomaapp.di

import android.content.Context
import com.vovan.diplomaapp.data.api.MqttManager

object Injector {
    fun getMqttManager(context: Context): MqttManager = MqttManager(context)
}