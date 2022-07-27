package com.vovan.diplomaapp.data

import com.vovan.diplomaapp.data.api.LambdaApi
import com.vovan.diplomaapp.di.ApplicationScope
import com.vovan.diplomaapp.di.DefaultDispatcher
import com.vovan.diplomaapp.di.IoDispatcher
import com.vovan.diplomaapp.domain.SensorsRepository
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class LambdaSensorsRepository(
    private val api: LambdaApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SensorsRepository {
    override suspend fun getSensors(tableName: String): List<SensorsEntity> =
        withContext(ioDispatcher) {
            api.fetchSensorsDataToday(tableName).Items
                .sortedByDescending { it.time }
                .map { it.toEntity(tableName) }
        }

}