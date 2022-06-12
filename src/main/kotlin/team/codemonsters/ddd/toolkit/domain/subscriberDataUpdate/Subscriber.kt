package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import team.codemonsters.ddd.toolkit.dto.SubscriberDto

data class Subscriber(
    val subscriberId: String,
    val msisdn: String,
    val mobileRegionId: String
) {
    companion object {
        fun emerge(subscriberResult: Result<SubscriberDto>)
                : Result<Subscriber> =
            subscriberResult.map {
                Subscriber(
                    it.subscriberId,
                    it.msisdn,
                    it.mobileRegionId
                )
            }
    }
}