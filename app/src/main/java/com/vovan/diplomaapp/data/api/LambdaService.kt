package com.vovan.diplomaapp.data.api

import com.vovan.diplomaapp.data.dto.ItemsSensorsDTO
import com.vovan.diplomaapp.data.dto.SensorsDTO
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LambdaApi{
    @GET("SmartCampusHTTPReadDatabase")
    fun fetchSensorsDataToday(
        @Query("TableName") tableName: String = "SC_DataToday"
    ): Single<ItemsSensorsDTO>
}