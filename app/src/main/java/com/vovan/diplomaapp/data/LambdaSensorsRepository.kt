package com.vovan.diplomaapp.data

import com.vovan.diplomaapp.data.api.LambdaApi
import com.vovan.diplomaapp.domain.SensorsRepository
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.toEntity
import io.reactivex.Single

class LambdaSensorsRepository(
    private val api: LambdaApi
): SensorsRepository {
    override fun getSensors(tableName: String): Single<List<SensorsEntity>> {
        return api.fetchSensorsDataToday(tableName)
            .map { respond ->
                respond.Items
                    .sortedByDescending { it.time }
                    .map { it.toEntity(tableName) }
            }
    }

}