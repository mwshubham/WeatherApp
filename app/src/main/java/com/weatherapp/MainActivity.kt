package com.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.weatherapp.databinding.ActivityMainBinding
import com.weatherapp.vo.Status
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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

        mainViewModel.setCity("Bangalore")
    }
}
