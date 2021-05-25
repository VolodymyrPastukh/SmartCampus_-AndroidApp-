package com.vovan.diplomaapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vovan.diplomaapp.databinding.SensorsItemBinding
import com.vovan.diplomaapp.domain.entity.SensorsEntity

class SensorsAdapter :
    ListAdapter<SensorsEntity, SensorsAdapter.SensorsViewHolder>(SensorsItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorsViewHolder {
        return SensorsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SensorsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class SensorsViewHolder private constructor(private val binding: SensorsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SensorsEntity) {
            binding.time.text = item.time
            binding.temperature.text = item.temperature.toString()
            binding.light.text = item.light.toString()
            binding.pressure.text = item.pressure.toString()
        }


        companion object {
            fun from(parent: ViewGroup): SensorsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SensorsItemBinding.inflate(layoutInflater, parent, false)
                return SensorsViewHolder(binding)
            }
        }
    }

    class SensorsItemDiffCallback : DiffUtil.ItemCallback<SensorsEntity>() {
        override fun areItemsTheSame(oldItem: SensorsEntity, newItem: SensorsEntity): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: SensorsEntity, newItem: SensorsEntity): Boolean {
            return oldItem == newItem
        }

    }
}