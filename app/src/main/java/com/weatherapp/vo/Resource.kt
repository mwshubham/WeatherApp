package com.weatherapp.vo

import com.weatherapp.vo.Status.*

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }

    fun onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
        if (this.status == SUCCESS)
            onSuccess(this.data)

        return this
    }

    fun onLoading(onLoading: (data: T?) -> Unit): Resource<T> {
        if (this.status == LOADING)
            onLoading(this.data)

        return this
    }

    fun onError(onError: (msg: String?, data: T?) -> Unit): Resource<T> {
        if (this.status == ERROR)
            onError(this.message, data)

        return this
    }
}