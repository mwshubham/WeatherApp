package com.weatherapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.weatherapp.databinding.ActivityMainBinding
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupRecyclerView()

        mainViewModel = ViewModelProvider(this, viewmodelFactory)[MainViewModel::class.java]
        mainViewModel.currentWeather.observe(this, Observer {result ->
            result.run {
                peekContent()
                    .onLoading {
                        binding.contentMain.refresh.isRefreshing = true
                    }.onSuccess {
                        binding.contentMain.refresh.isRefreshing = false
                        peekContent().data?.run {
                            binding.contentMain.currentTemp.text = currentTemp

                            when(currentWeather) {
                                "Clear" -> Picasso.get().load(R.drawable.clear).fit().into(binding.contentMain.tempIcon)
                                "Clouds" -> Picasso.get().load(R.drawable.clouds).fit().into(binding.contentMain.tempIcon)
                                "Rain" -> Picasso.get().load(R.drawable.rain).fit().into(binding.contentMain.tempIcon)
                                "Thunderstorm" -> Picasso.get().load(R.drawable.storm).fit().into(binding.contentMain.tempIcon)
                            }
                        }
                    }.onError { _, _ ->
                        binding.contentMain.refresh.isRefreshing = false

                        ObjectAnimator.ofFloat(binding.errorView.errorRootView,
                            "translationX", 0f).apply {
                            duration = 300
                            start()
                        }
                    }

                getContentIfNotHandled()?.let {
                    it.onError { msg, _ ->
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        mainViewModel.forecasts.observe(this, Observer { result ->
            result.run {
                peekContent()
                    .onSuccess {
                        forecastAdapter.submitList(it)
                    }

                getContentIfNotHandled()?.let {
                    it.onError { msg, _ ->
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        binding.contentMain.refresh.setOnRefreshListener {
            mainViewModel.retry()
        }

        binding.errorView.retry.setOnClickListener {
            mainViewModel.retry()

            ObjectAnimator.ofFloat(binding.errorView.errorRootView,
                "translationX", convertDpToPixel(800f)).apply {
                duration = 250
                start()
            }
        }

        mainViewModel.setCity("Bangalore")
    }

    private fun setupRecyclerView() {
        recyclerView = binding.forecast.forecasts
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)

        forecastAdapter = ForecastAdapter()
        recyclerView.adapter = forecastAdapter
    }

    private fun convertDpToPixel(dp: Float): Float {
        return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
