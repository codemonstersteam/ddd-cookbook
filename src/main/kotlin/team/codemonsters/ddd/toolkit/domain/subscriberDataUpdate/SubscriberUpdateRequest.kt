package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import team.codemonsters.ddd.toolkit.domain.SubscriberDataUpdate

data class SubscriberUpdateRequest constructor(
    val subscriberId: String,
    val msisdn: String,
    val mobileRegionId: String,
    private val dataUpdate: SubscriberDataUpdate
) {
   val dataUpdateId = dataUpdate.dataUpdateId
}
