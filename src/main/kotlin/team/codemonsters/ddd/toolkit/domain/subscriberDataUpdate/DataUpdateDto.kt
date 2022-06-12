package team.codemonsters.ddd.toolkit.dto

data class DataUpdateDto(
    val dataUpdateId: String,
    val subscriberId: String,
    val msisdn: String,
    val mobileRegionId: String
)
