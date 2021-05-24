package com.vovan.diplomaapp.presentation.sensors

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.databinding.FragmentSensorsBinding
import com.vovan.diplomaapp.domain.entity.SensorsEntity


class SensorsFragment : Fragment() {

    private lateinit var viewModel: SensorsViewModel
    private lateinit var binding: FragmentSensorsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sensors,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(SensorsViewModel::class.java)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            displayData(state)
        }


        return binding.root
    }



    private fun displayData(state: SensorsViewState){
        when(state){
            is SensorsViewState.Connecting -> {
                binding.progressBar.show()
                binding.progressBar.setIndicatorColor(Color.BLACK)
                binding.progressBar.isIndeterminate = true
            }

            is SensorsViewState.Connected -> {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.Connected),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                binding.progressBar.hide()
            }

            is SensorsViewState.Data -> setFields(state.data)

            is SensorsViewState.Error ->
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setFields(sensors: SensorsEntity){
        binding.date.text = sensors.time
        binding.temperature.text = sensors.temperature.toString()
        binding.light.text = sensors.light.toString()
        binding.pressure.text = sensors.pressure.toString()
    }

}