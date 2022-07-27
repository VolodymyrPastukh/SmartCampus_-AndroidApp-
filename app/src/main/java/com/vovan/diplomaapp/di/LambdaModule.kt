package com.vovan.diplomaapp.di

import com.vovan.diplomaapp.data.LambdaSensorsRepository
import com.vovan.diplomaapp.data.api.LambdaApi
import com.vovan.diplomaapp.domain.SensorsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LambdaModule {

    @Singleton
    @Provides
    fun provideLambdaApi(): LambdaApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://2op9csyc6c.execute-api.eu-west-2.amazonaws.com/default/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(LambdaApi::class.java)
    }

    @Provides
    fun provideLambdaRepository(
        api: LambdaApi,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): SensorsRepository {
        return LambdaSensorsRepository(api, defaultDispatcher)
    }
}