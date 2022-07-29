package com.vovan.diplomaapp.presentation.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vovan.diplomaapp.TOPIC_PUB
import com.vovan.diplomaapp.data.NetworkMqttRepository
import com.vovan.diplomaapp.data.sharedPreference.SharedPreferenceDataSource
import com.vovan.diplomaapp.defineSharedState
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.presentation.ledController.LedControllerViewModel

class PublishWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters,
    private val repository: NetworkMqttRepository,
    private val sharedPreferenceDataSource: SharedPreferenceDataSource
) : CoroutineWorker(applicationContext, workerParameters) {

    override suspend fun doWork(): Result {
        repository.connection.collect{
            if(it == ConnectionState.Connected){
                repository.publish(TOPIC_PUB, getLastLedState())
            }
        }
    }

    private fun getLastLedState(): LedControllerEntity {
        val led = sharedPreferenceDataSource.getValue<Int>("led")
        return LedControllerEntity("Feel the pain!!!", led)
            .also { it.buzzer = if (it.rgb == 7) 1 else 0 }
    }

}