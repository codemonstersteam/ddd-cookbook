package team.codemonsters.ddd.toolkit.domain

import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.DataUpdate
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.Subscriber
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.SubscriberUpdateRequest

data class SubscriberDataUpdate private constructor(
    private val dataUpdate: DataUpdate,
    private val subscriber: Subscriber
) {

    val subscriberId = subscriber.subscriberId
    val dataUpdateId = dataUpdate.dataUpdateId

    fun prepareUpdateRequest(): Result<SubscriberUpdateRequest> =
        when (isUpdateRequired()) {
            true -> createSubscriberUpdateRequest()
            else -> failNoUpdateRequired()
        }

    private fun failNoUpdateRequired(): Result<SubscriberUpdateRequest> =
        Result.failure(RuntimeException("No Update Required"))

    private fun createSubscriberUpdateRequest()
            : Result<SubscriberUpdateRequest> =
        Result.success(
            SubscriberUpdateRequest(
                subscriberId,
                dataUpdate.msisdn,
                dataUpdate.mobileRegionId,
                this
            )
        )

    private fun isUpdateRequired(): Boolean =
        subscriber.mobileRegionId != dataUpdate.mobileRegionId

    companion object {
        fun emerge(
            dataUpdate: DataUpdate,
            subscriberResult: Result<Subscriber>
        ): Result<SubscriberDataUpdate> =
            subscriberResult.map {
                SubscriberDataUpdate(
                    dataUpdate,
                    Subscriber(it.subscriberId, it.msisdn, it.mobileRegionId)
                )
            }
    }

}