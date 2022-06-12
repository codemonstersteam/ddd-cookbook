package team.codemonsters.ddd.toolkit.controller

import com.fasterxml.jackson.annotation.JsonIgnore
import team.codemonsters.ddd.toolkit.utils.DateTimeUtils
import java.io.Serializable

class RestResponse<out T> protected constructor(
    val status: String,
    val actualTimestamp: Long,
    var data: @UnsafeVariance T?,
    var error: RestError?
) :
    Serializable {

    @JsonIgnore
    val isSuccess: Boolean = null != data && "success" == status

    @JsonIgnore
    val isFail: Boolean = !isSuccess

    @JsonIgnore
    val valueOrThrow: T = if (isSuccess) data!! else throw RuntimeException("Can't get value here")

    companion object {
        fun <T> success(data: T): RestResponse<T> = RestResponse(
            status = "success",
            actualTimestamp = DateTimeUtils.nowToEpochMill(),
            data = data,
            error = null
        )

        fun <T> fail(error: RestError): RestResponse<T> = RestResponse(
            status = "success",
            actualTimestamp = DateTimeUtils.nowToEpochMill(),
            data = null,
            error = error
        )

    }
}
