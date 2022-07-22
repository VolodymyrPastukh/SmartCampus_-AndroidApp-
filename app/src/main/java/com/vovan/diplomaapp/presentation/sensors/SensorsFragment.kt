package com.vovan.diplomaapp.presentation.sensors

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.databinding.FragmentSensorsBinding
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorsFragment : Fragment() {

    private val viewModel: SensorsViewModel by viewModels()
    private lateinit var binding: FragmentSensorsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSensorsBinding.inflate(
        inflater,
        container,
        false
    ).apply { binding = this }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.connectionState.observe(viewLifecycleOwner) { processConnectionState(it) }
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
                    Snackbar.LENGTH_SHORT
                ).show()
                progressBar.hide()
                viewModel.dataState.observe(viewLifecycleOwner) { processDataState(it) }
            }

            is SensorsConnectionState.Error ->
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun processDataState(state: SensorDataState<SensorsEntity>) {
        setFields(state.data)
    }

    private fun setFields(sensors: SensorsEntity) = with(binding) {
        date.text = sensors.time
        temperature.text = sensors.temperature.toString()
        light.text = sensors.light.toString()
        pressure.text = sensors.pressure.toString()
    }

}