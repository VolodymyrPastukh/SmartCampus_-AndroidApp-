package com.vovan.diplomaapp.domain

import com.vovan.diplomaapp.domain.entity.SensorsEntity
import io.reactivex.Single

interface SensorsRepository {
    fun getSensors(): Single<List<SensorsEntity>>
}