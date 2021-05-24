package com.vovan.diplomaapp.di


import android.content.Context
import com.vovan.diplomaapp.data.NetworkCampusRepository
import com.vovan.diplomaapp.data.api.MqttManager

object Injector {
    fun provideRepository(context: Context) = NetworkCampusRepository(
        api = MqttManager(context)
    )
}