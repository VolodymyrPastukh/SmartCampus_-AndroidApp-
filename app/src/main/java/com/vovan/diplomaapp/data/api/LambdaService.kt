package com.vovan.diplomaapp.data.api

import com.vovan.diplomaapp.data.dto.ItemsSensorsDTO
import com.vovan.diplomaapp.data.dto.SensorsDTO
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


fun createLambdaApi(): LambdaApi {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://2op9csyc6c.execute-api.eu-west-2.amazonaws.com/default/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    return retrofit.create(LambdaApi::class.java)
}


interface LambdaApi{
    @GET("SmartCampusHTTPReadDatabase")
    fun fetchSensorsDataToday(
        @Query("TableName") tableName: String = "SC_DataToday"
    ): Single<ItemsSensorsDTO>
}