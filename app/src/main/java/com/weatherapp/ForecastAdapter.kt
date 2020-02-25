package com.weatherapp

import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Picasso
import com.weatherapp.databinding.ForecastRowItemBinding
import com.weatherapp.util.DataBindingAdapter
import com.weatherapp.util.DataBindingViewHolder
import com.weatherapp.vo.ForecastUi

class ForecastAdapter : DataBindingAdapter<ForecastUi>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<ForecastUi>() {
        override fun areItemsTheSame(oldItem: ForecastUi, newItem: ForecastUi): Boolean {
            return (oldItem.forecastTemp == newItem.forecastTemp)
        }

        override fun areContentsTheSame(oldItem: ForecastUi, newItem: ForecastUi): Boolean {
            return (oldItem.forecastDate == newItem.forecastDate &&
                    oldItem.forecastTemp == newItem.forecastTemp &&
                    oldItem.forecastWeather == newItem.forecastWeather)
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<ForecastUi>, position: Int) {
        super.onBindViewHolder(holder, position)

        val context = holder.binding.root.context
        val binding = holder.binding as ForecastRowItemBinding

        when(getItem(position).forecastWeather) {
            "Clear" -> Picasso.get().load(context.resources.getIdentifier("clear",
                "drawable", context.packageName)).into(binding.tempIcon)
            "Clouds" -> Picasso.get().load(context.resources.getIdentifier("clouds",
                "drawable", context.packageName)).into(binding.tempIcon)
            "Rain" -> Picasso.get().load(context.resources.getIdentifier("rain",
                "drawable", context.packageName)).into(binding.tempIcon)
            "Thunderstorm" -> Picasso.get().load(context.resources.getIdentifier("storm",
                "drawable", context.packageName)).into(binding.tempIcon)
        }

    }

    override fun getItemViewType(position: Int) = R.layout.forecast_row_item
}