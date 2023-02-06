package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate
data class SubscriberId
private constructor(
    override val value: String
) : ValueObject<String> {
    companion object {
        fun emerge(subscriberId: String)
                : Result<SubscriberId> =
            when (isStringConsists6Digits(subscriberId)) {
                true -> Result.success(SubscriberId(subscriberId))
                else -> Result.failure(IllegalArgumentException("Subscriber Id consists of numbers maximum length 6"))
            }

        private val isStringConsist6Digits = "^\\d{1,6}\$".toRegex()

        private fun isStringConsists6Digits(value: String) =
            isStringConsist6Digits.matches(value)
    }
}