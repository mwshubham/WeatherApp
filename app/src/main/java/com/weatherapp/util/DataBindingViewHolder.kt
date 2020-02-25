package com.weatherapp.util

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.BR

class DataBindingViewHolder<T>(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}