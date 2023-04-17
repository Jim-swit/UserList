package org.project.userlist.data.network

import retrofit2.HttpException
import retrofit2.Response

sealed interface ApiResult<T: Any> {
    class ApiSuccess<T: Any>(val data: T): ApiResult<T>
    class ApiError<T: Any>(val exception: Throwable): ApiResult<T>
    class ApiLoading<T: Any>(): ApiResult<T>

}

suspend fun <T : Any> APICall(call: suspend () -> Response<T>): ApiResult<T> {
    return runCatching {
        call()
    }.fold(
        onSuccess = {
            if (it.isSuccessful) {
                it.body()?.let { body ->
                    ApiResult.ApiSuccess(body)
                } ?: ApiResult.ApiError(Throwable("body is null"))
            } else {
                ApiResult.ApiError(HttpException(it))
            }
        },
        onFailure = {
            ApiResult.ApiError(it)
        }
    )
}