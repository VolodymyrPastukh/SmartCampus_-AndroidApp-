package com.vovan.diplomaapp.domain

import com.vovan.diplomaapp.domain.entity.SensorsEntity
import kotlinx.coroutines.flow.Flow


interface SensorsRepository {
    fun getSensors(tableName: String): Flow<SensorsEntity>
}