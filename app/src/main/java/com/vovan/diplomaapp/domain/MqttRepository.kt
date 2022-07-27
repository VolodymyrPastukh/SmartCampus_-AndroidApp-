package com.vovan.diplomaapp.domain

import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface MqttRepository {
    val connection: Flow<ConnectionState>
    fun subscribe(topic: String): Flow<SensorsEntity>
    suspend fun publish(topic: String, data: String): Boolean
}