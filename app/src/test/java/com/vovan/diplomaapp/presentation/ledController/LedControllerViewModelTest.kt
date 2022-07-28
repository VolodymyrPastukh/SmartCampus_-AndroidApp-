package com.vovan.diplomaapp.presentation.ledController

import android.content.Context
import android.graphics.ColorSpace
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vovan.diplomaapp.data.NetworkMqttRepository
import com.vovan.diplomaapp.data.mqttRepository.FakeMqttRepository
import com.vovan.diplomaapp.di.CoroutinesModule
import com.vovan.diplomaapp.di.MqttModule
import com.vovan.diplomaapp.domain.MqttRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
class LedControllerViewModelTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var viewModel: LedControllerViewModel

    @Before
    fun prepare() {
        Dispatchers.setMain(mainThreadSurrogate)
        val repository = FakeMqttRepository(
            CoroutinesModule.providesExternalCoroutineScope(CoroutinesModule.providesIoDispatcher()),
            CoroutinesModule.providesDefaultDispatcher()
        )
        viewModel = LedControllerViewModel(repository)
    }

    @After
    fun cleanUp(){
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
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

    private fun await(block: () -> Unit){
        block()
        runBlocking {
            viewModel.viewModelScope.coroutineContext[Job]?.children?.forEach { it.join() }
        }
    }

}