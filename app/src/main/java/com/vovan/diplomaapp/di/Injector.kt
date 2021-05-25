package com.vovan.diplomaapp.di


import android.content.Context
import com.vovan.diplomaapp.data.LambdaSensorsRepository
import com.vovan.diplomaapp.data.NetworkCampusRepository
import com.vovan.diplomaapp.data.api.MqttManager
import com.vovan.diplomaapp.data.api.createLambdaApi

object Injector {
    fun provideRepository(context: Context) = NetworkCampusRepository(
        api = MqttManager(context)
    )

    fun provideRepository() = LambdaSensorsRepository(
        createLambdaApi()
    )

}