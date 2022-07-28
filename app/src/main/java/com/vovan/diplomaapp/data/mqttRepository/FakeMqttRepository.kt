package com.vovan.diplomaapp.data.mqttRepository

import com.vovan.diplomaapp.di.ApplicationScope
import com.vovan.diplomaapp.di.DefaultDispatcher
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMqttRepository(
    @ApplicationScope private val externalScope: CoroutineScope,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : MqttRepository{

    override val connection: Flow<ConnectionState>
        get() = flow {  }

    override fun subscribe(topic: String): Flow<SensorsEntity> {
        return flow {  }
    }

    override suspend fun publish(topic: String, data: LedControllerEntity): Boolean {
        return true
    }

}