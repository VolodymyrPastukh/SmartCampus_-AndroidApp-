package com.vovan.diplomaapp.di

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.regions.Regions
import com.google.gson.Gson
import com.vovan.diplomaapp.data.NetworkMqttRepository
import com.vovan.diplomaapp.domain.MqttRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.util.*
import javax.inject.Singleton

private const val CUSTOMER_SPECIFIC_ENDPOINT = "a240uztzb3wu4b-ats.iot.eu-west-2.amazonaws.com"
private const val COGNITO_POOL_ID = "eu-west-2:a976872f-11a6-43be-8d3a-5f7d1f53286c"
private val region = Regions.EU_WEST_2

@InstallIn(SingletonComponent::class)
@Module
object MqttModule {


    @Singleton
    @Provides
    fun provideCognitoCredentialsProvider(
        @ApplicationContext appContext: Context
    ): CognitoCachingCredentialsProvider {
        return CognitoCachingCredentialsProvider(
            appContext,
            COGNITO_POOL_ID,
            region
        )
    }

    @Singleton
    @Provides
    fun provideAwsIoTMqttManager(): AWSIotMqttManager {
        val clientId = UUID.randomUUID().toString()
        return AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_ENDPOINT)
    }

    @Provides
    fun provideGsonConverter(): Gson = Gson()

    @Singleton
    @Provides
    fun provideMqttRepository(
        manager: AWSIotMqttManager,
        credentialsProvider: CognitoCachingCredentialsProvider,
        gson: Gson,
        @ApplicationScope coroutineScope: CoroutineScope,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): MqttRepository {
        return NetworkMqttRepository(manager, credentialsProvider, gson, coroutineScope, defaultDispatcher)
    }
}