package com.vovan.diplomaapp.presentation.ledController

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.vovan.diplomaapp.utils.MainCoroutineRule
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.toAwsConnectionState
import com.vovan.diplomaapp.utils.TestObserver
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class LedControllerViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LedControllerViewModel
    private val repository = mockk<MqttRepository>()


    @Before
    fun prepare() {
        coEvery { repository.connection } returns flow {
            repeat(2) {
                emit(AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting.toAwsConnectionState())
                delay(1000)
            }
            delay(3000)
            emit(AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected.toAwsConnectionState())
        }

        viewModel = LedControllerViewModel(repository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun `check correct initialization`() = runTest {
        val job = launch {
            val stateObserver = TestObserver<SensorsConnectionState>()

            val actual = viewModel.connectionState.apply {
                observeForever(stateObserver)
            }

            advanceUntilIdle()
            stateObserver.assertAllEmitted(
                listOf(
                    SensorsConnectionState.Connecting,
                    SensorsConnectionState.Connecting,
                    SensorsConnectionState.Connected
                )
            )
            actual.removeObserver(stateObserver)
        }

        job.join()
        job.cancel()
    }

    @Test
    fun `check initial livedata state test`() {
        viewModel.dataState.value shouldNotBe null
        viewModel.dataState.value?.data shouldBe listOf(false, false, false)
    }

    @Test
    fun `publish led value test`() {
        viewModel.clickOnLed(Color.RED)
        viewModel.dataState.value?.data shouldBe listOf(true, false, false)
        viewModel.clickOnLed(Color.BLUE)
        viewModel.dataState.value?.data shouldBe listOf(true, false, true)
    }
}