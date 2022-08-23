package com.vovan.diplomaapp.presentation.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.vovan.diplomaapp.TOPIC_PUB
import com.vovan.diplomaapp.convertLongToTime
import com.vovan.diplomaapp.data.sharedPreference.SharedPreferenceDataSource
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class PublishWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: MqttRepository,
) : CoroutineWorker(applicationContext, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            val publishData = getLastLedState()
            if (repository.publish(TOPIC_PUB, publishData)) {
//                repository.disconnect()
                Result.success(workDataOf("publishData" to publishData.rgb, "isSuccess" to true))
            } else Result.retry()
        } catch (error: Throwable) {
            error.printStackTrace()
            Result.failure(workDataOf("isSuccess" to false))
        }
    }

    private fun getLastLedState() =
        LedControllerEntity(
            "Cached led request ${convertLongToTime()}", inputData.getInt("led_data", 0))
            .also { it.buzzer = if (it.rgb == 7) 1 else 0 }

}