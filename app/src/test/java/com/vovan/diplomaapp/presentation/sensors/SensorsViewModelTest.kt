package com.vovan.diplomaapp.presentation.sensors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vovan.diplomaapp.TOPIC_SUB
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.utils.MainCoroutineRule
import com.vovan.diplomaapp.utils.TestObserver
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SensorsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SensorsViewModel
    private val repository = mockk<MqttRepository>()
    private val sensorEntity = mockk<SensorsEntity>(relaxed = true)

    @Before
    fun prepare() {

        coEvery { repository.connection } returns flow {
            repeat(3) {
                emit(ConnectionState.Connected)
                delay(5000)
            }
        }

        every { repository.subscribe(TOPIC_SUB) } returns flow {
            repeat(3) {
                emit(sensorEntity)
                delay(5000)
            }
        }

        viewModel = SensorsViewModel(repository)
    }

    @Test
    fun `check correct initialization`() = runTest {
        val stateObserver = TestObserver<SensorDataState<SensorsEntity>>()

        val actual = viewModel.dataState.apply { observeForever(stateObserver) }

        advanceUntilIdle()
        stateObserver.assertAllEmitted(
            listOf(
                SensorDataState(sensorEntity),
                SensorDataState(sensorEntity),
                SensorDataState(sensorEntity)
            )
        )
        actual.removeObserver(stateObserver)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }


}