package com.vovan.diplomaapp.domain

import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface MqttRepository {
    fun connect(): Observable<ConnectionState>
    fun subscribe(topic: String): Observable<SensorsEntity>
    fun publish(topic: String, data: String): Completable
    fun disconnect()
}