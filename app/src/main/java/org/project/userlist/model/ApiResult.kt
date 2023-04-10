package org.project.userlist.model

import retrofit2.Response

sealed interface ApiResult<T: Any> {
    class ApiSuccess<T: Any>(val data: T): ApiResult<T>
    class ApiError<T: Any>(val code:Int, val message: String?): ApiResult<T>
    class ApiException<T: Any>(val exception: Throwable): ApiResult<T>

}

suspend fun <T : Any> ApiCall(call: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = call.invoke()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            // 데이터 수신 성공
            ApiResult.ApiSuccess(body)
        } else {
            // 수신은 성공했지만 오류 메세지가 포함된 응답
            ApiResult.ApiError(response.code(), response.message())
        }
    } catch (e: Exception) {
        // 에러 상황 발생
        ApiResult.ApiException(e)
    }
}