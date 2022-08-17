package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

data class MobileRegionId private constructor(override val value: String) : ValueObject<String> {
    companion object {
        fun emerge(mobileRegionId: String)
                : Result<MobileRegionId> =
            when (isStringWith4Digits(mobileRegionId)) {
                true -> Result.success(MobileRegionId(mobileRegionId))
                else -> Result.failure(IllegalArgumentException("Mobile Region Id consists of numbers maximum length 4"))
            }

        private fun isStringWith4Digits(value: String) =
            value.length < 5
                    && value.lineSequence().all { it in "0".."9" }
    }
}