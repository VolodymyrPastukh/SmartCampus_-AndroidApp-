package com.vovan.diplomaapp.presentation.ledController

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.databinding.FragmentLedControllerBinding
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LedControllerFragment : Fragment() {

    private val viewModel: LedControllerViewModel by viewModels()
    private lateinit var binding: FragmentLedControllerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLedControllerBinding.inflate(
            inflater,
            container,
            false
        ).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        redLed.setOnClickListener { viewModel.clickOnLed(LedControllerViewModel.RED_LED) }
        greenLed.setOnClickListener { viewModel.clickOnLed(LedControllerViewModel.GREEN_LED) }
        blueLed.setOnClickListener { viewModel.clickOnLed(LedControllerViewModel.BLUE_LED) }

        viewModel.connectionState.observe(viewLifecycleOwner) { processConnectionState(it) }
        viewModel.dataState.observe(viewLifecycleOwner) { processDataState(it) }
    }

    private fun processConnectionState(state: SensorsConnectionState) = with(binding) {
        when (state) {
            is SensorsConnectionState.Connecting -> {
                progressBar.show()
                progressBar.setIndicatorColor(Color.BLACK)
                progressBar.isIndeterminate = true
            }

            is SensorsConnectionState.Connected -> {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.Connected),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                progressBar.hide()
            }

            is SensorsConnectionState.Error ->
                Toast.makeText(context, "Error ${state.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processDataState(state: SensorDataState<List<Boolean>>) {
        setColors(state.data)
    }

    private fun setColors(data: List<Boolean>) = with(binding){
        if (data[0]) redLed.setImageResource(R.drawable.redled)
        else redLed.setImageResource(R.drawable.led)

        if (data[1]) greenLed.setImageResource(R.drawable.greenled)
        else greenLed.setImageResource(R.drawable.led)

        if (data[2]) blueLed.setImageResource(R.drawable.blueled)
        else blueLed.setImageResource(R.drawable.led)
    }

}