package com.weatherapp.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Custom okhttp interceptor to add the api auth key to all requests
 */
class ServiceInterceptor(private val apiKey: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url: HttpUrl = request.url().newBuilder()
            .addQueryParameter("appid", apiKey)
            .build()
        request = request.newBuilder().url(url).build()

        return chain.proceed(request)
    }
}