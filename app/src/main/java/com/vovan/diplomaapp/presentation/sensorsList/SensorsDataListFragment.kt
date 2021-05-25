package com.vovan.diplomaapp.presentation.sensorsList

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.databinding.FragmentSensorsDataListBinding
import com.vovan.diplomaapp.presentation.adapter.SensorsAdapter


class SensorsDataListFragment : Fragment() {
    private var adapter: SensorsAdapter? = null
    private var binding: FragmentSensorsDataListBinding? = null
    private var viewModel: SensorsDataListViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sensors_data_list,
            container,
            false
        )

        adapter = SensorsAdapter()
        binding?.let {
            it.recyclerView.adapter = adapter
            it.recyclerView.addItemDecoration(MarginItemDecoration(20))
        }

        viewModel = ViewModelProvider(this).get(SensorsDataListViewModel::class.java)
        viewModel?.let {
            it.state.observe(viewLifecycleOwner) { state ->
                displayData(state)
            }
        }

        return binding?.root

    }

    private fun displayData(state: SensorsDataListViewState) {
        when (state) {
            is SensorsDataListViewState.Loading -> {
                binding?.let {
                    it.progressBar.show()
                    it.progressBar.setIndicatorColor(Color.BLACK)
                    it.progressBar.isIndeterminate = true
                }
            }

            is SensorsDataListViewState.Data -> {
                adapter?.submitList(state.data)
                binding?.progressBar?.visibility = ProgressBar.GONE
            }
            is SensorsDataListViewState.Error ->
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
        }
    }

}