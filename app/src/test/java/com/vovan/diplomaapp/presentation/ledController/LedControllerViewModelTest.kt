package com.vovan.diplomaapp.presentation.ledController

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.vovan.diplomaapp.OFFLINE_LED_PUBLISH
import com.vovan.diplomaapp.data.sharedPreference.SharedPreferenceDataSource
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.toAwsConnectionState
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
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

    private val dispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(dispatcher)

    private lateinit var viewModel: LedControllerViewModel
    private val repository = mockk<MqttRepository>()
    private val sharedPreferences = mockk<SharedPreferenceDataSource>()
    private val workManager = mockk<WorkManager>()


    @Before
    fun prepare() {
        Dispatchers.setMain(dispatcher)

        every { sharedPreferences.getValue<String>(OFFLINE_LED_PUBLISH) } returns ""

        coEvery { repository.connection } returns flow {
            repeat(2) {
                emit(AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting.toAwsConnectionState())
                delay(1000)
            }
            delay(3000)
            emit(AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected.toAwsConnectionState())
        }
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `check correct initialization`() = runTest {
        val job = launch {
            viewModel = LedControllerViewModel(repository, sharedPreferences, workManager)
            val stateObserver = TestableObserver<SensorsConnectionState>()

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
        await { viewModel.clickOnLed(LedControllerViewModel.RED_LED) }
        viewModel.dataState.value?.data shouldBe listOf(true, false, false)
        await { viewModel.clickOnLed(LedControllerViewModel.BLUE_LED) }
        viewModel.dataState.value?.data shouldBe listOf(true, false, true)
    }

    private fun await(block: () -> Unit) {
        block()
        runBlocking {
            viewModel.viewModelScope.coroutineContext[Job]?.children?.forEach { it.join() }
        }
    }

}

class TestableObserver<T> : Observer<T> {
    private val history: MutableList<T> = mutableListOf()

    override fun onChanged(value: T) {
        history.add(value)
    }

    fun assertAllEmitted(values: List<T>) {
        values.count() shouldBe history.count()

        history.forEachIndexed { index, t ->
            values[index] shouldBe t
        }
    }
}