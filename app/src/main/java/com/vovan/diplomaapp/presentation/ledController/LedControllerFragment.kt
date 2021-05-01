package com.vovan.diplomaapp.presentation.ledController

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
import com.vovan.diplomaapp.databinding.FragmentLedControllerBinding

class LedControllerFragment : Fragment() {

    private lateinit var viewModel: LedControllerViewModel
    private lateinit var binding: FragmentLedControllerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_led_controller,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(LedControllerViewModel::class.java)


        binding.redLed.setOnClickListener {
            viewModel.turnOnLed(LedControllerViewModel.RED_LED)
        }

        binding.greenLed.setOnClickListener {
            viewModel.turnOnLed(LedControllerViewModel.GREEN_LED)
        }

        binding.blueLed.setOnClickListener {
            viewModel.turnOnLed(LedControllerViewModel.BLUE_LED)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            displayData(state)
        }



        return binding.root
    }


    private fun displayData(state: LedControllerViewState){
        when(state){
            is LedControllerViewState.Connecting -> {
                binding.progressBar.show()
                binding.progressBar.setIndicatorColor(Color.BLACK)
                binding.progressBar.isIndeterminate = true
            }

            is LedControllerViewState.Connected -> {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.Connected),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                binding.progressBar.hide()
            }

            is LedControllerViewState.Data -> setColors(state.data)

            is LedControllerViewState.Error ->
                Toast.makeText(context, "Error ${state.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setColors(data: List<Boolean>) {
        if (data[0]) binding.redLed.setImageResource(R.drawable.redled)
        else binding.redLed.setImageResource(R.drawable.led)

        if (data[1]) binding.greenLed.setImageResource(R.drawable.greenled)
        else binding.greenLed.setImageResource(R.drawable.led)

        if (data[2]) binding.blueLed.setImageResource(R.drawable.blueled)
        else binding.blueLed.setImageResource(R.drawable.led)
    }


}