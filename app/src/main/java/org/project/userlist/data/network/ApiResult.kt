package org.project.userlist.data.network

import retrofit2.HttpException
import retrofit2.Response

sealed interface ApiResult<out t> {
    object Loading: ApiResult<Nothing>
    data class Success<T: Any>(val data: T): ApiResult<T>

    sealed class Fail: ApiResult<Nothing> {
        data class Error(val code: Int, val message: String?): ApiResult<Nothing>

        data class Exception(val e: Throwable): ApiResult<Nothing>
    }
}

suspend fun <T : Any> ApiCall(call: suspend () -> Response<T>): ApiResult<T> {
    return runCatching {
        call()
    }.fold(
        onSuccess = {
            if (it.isSuccessful) {
                it.body()?.let { body ->
                    ApiResult.Success(body)
                } ?: ApiResult.Fail.Error(it.code(),it.message())
            } else {
                ApiResult.Fail.Exception(HttpException(it))
            }
        },
        onFailure = {
            ApiResult.Fail.Exception(it)
        }
    ) as ApiResult<T>
}