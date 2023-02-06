package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

data class MobileRegionId
private constructor(override val value: String) : ValueObject<String> {
    companion object {
        fun emerge(mobileRegionId: String)
                : Result<MobileRegionId> =
            when (isStringWith4Digits(mobileRegionId)) {
                true -> Result.success(MobileRegionId(mobileRegionId))
                else -> Result.failure(IllegalArgumentException("Mobile Region Id consists of numbers maximum length 4"))
            }

        private val isStringConsist4Digits = "^\\d{1,4}\$".toRegex()

        private fun isStringWith4Digits(value: String) =
            isStringConsist4Digits.matches(value)

    }
}