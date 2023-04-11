package org.project.userlist.data.remote

import retrofit2.HttpException
import retrofit2.Response

sealed interface ApiResult<T: Any> {
    class ApiSuccess<T: Any>(val data: T): ApiResult<T>
    class ApiError<T: Any>(val exception: Throwable): ApiResult<T>
    class ApiLoading<T: Any>(val data: T? = null): ApiResult<T>

}

suspend fun <T : Any> APICall(call: suspend () -> Response<T>): ApiResult<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.let {
                return ApiResult.ApiSuccess(it)
            }
        }
        return ApiResult.ApiError(HttpException(response))
    } catch (e: Exception) {
        return ApiResult.ApiError(e)
    }
}