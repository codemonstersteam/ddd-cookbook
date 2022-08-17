package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import team.codemonsters.ddd.toolkit.dto.SubscriberDto

data class Subscriber(
    val subscriberId: SubscriberId,
    val msisdn: Msisdn,
    val mobileRegionId: MobileRegionId
) {
    companion object {
        fun emerge(subscriberResult: Result<SubscriberDto>)
                : Result<Subscriber> =
            subscriberResult.map {
                val subscriberId = SubscriberId.emerge(it.subscriberId)
                val msisdn = Msisdn.emerge(it.msisdn)
                val mobileRegionId = MobileRegionId.emerge(it.mobileRegionId)
                Subscriber(
                    subscriberId = subscriberId.getOrThrow(),
                    msisdn = msisdn.getOrThrow(),
                    mobileRegionId = mobileRegionId.getOrThrow()
                )
            }
    }
}