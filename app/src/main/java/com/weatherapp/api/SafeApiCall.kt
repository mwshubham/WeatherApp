/*
 * Copyright 2018 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weatherapp.api

import timber.log.Timber
import java.io.IOException

/**
 * Wrap a suspending API [call] in try/catch. In case an exception is thrown, a [ApiResponse.Error] is
 * created based on the [errorMessage].
 */
suspend fun <T> safeApiCall(
    call: suspend () -> ApiResponse<T>,
    errorMessage: String
): ApiResponse<T> {
    return try {
        call()
    } catch (e: Exception) {
        Timber.e(e, "Api call failed")
        // An exception was thrown when calling the API so we're converting this to an IOException
        ApiResponse.create(IOException(errorMessage, e))
    }
}
