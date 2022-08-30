package team.codemonsters.ddd.toolkit.domain

import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.*

data class SubscriberDataUpdate private constructor(
    private val dataUpdate: DataUpdate,
    private val subscriber: Subscriber
) {

    val subscriberId: SubscriberId = subscriber.subscriberId
    val dataUpdateId: DataUpdateId = dataUpdate.dataUpdateId

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
                subscriberId.value,
                dataUpdate.msisdn.value,
                dataUpdate.mobileRegionId.value,
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
                    Subscriber(
                        it.subscriberId,
                        it.msisdn,
                        it.mobileRegionId
                    )
                )
            }
    }

}