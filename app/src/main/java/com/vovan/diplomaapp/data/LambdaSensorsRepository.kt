package com.vovan.diplomaapp.data

import com.vovan.diplomaapp.data.api.LambdaApi
import com.vovan.diplomaapp.domain.SensorsRepository
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import io.reactivex.Single

class LambdaSensorsRepository(
    private val api: LambdaApi
): SensorsRepository {
    override fun getSensors(): Single<List<SensorsEntity>> {
        return api.fetchSensorsDataToday()
            .map { respond ->
                respond.Items.map { sensorsDTO ->
                    SensorsEntity(
                        sensorsDTO.time,
                        sensorsDTO.temperature,
                        sensorsDTO.light,
                        sensorsDTO.pressure
                    )
                }
            }
    }

}