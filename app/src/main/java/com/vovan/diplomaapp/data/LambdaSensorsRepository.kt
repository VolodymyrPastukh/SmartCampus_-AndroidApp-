package com.vovan.diplomaapp.data

import com.vovan.diplomaapp.data.api.LambdaApi
import com.vovan.diplomaapp.domain.SensorsRepository
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.toEntity
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LambdaSensorsRepository(
    private val api: LambdaApi
): SensorsRepository {
    override fun getSensors(tableName: String): Flow<SensorsEntity> = flow {
        api.fetchSensorsDataToday(tableName).Items
            .sortedByDescending { it.time }
            .forEach { emit(it.toEntity(tableName)) }
    }
}