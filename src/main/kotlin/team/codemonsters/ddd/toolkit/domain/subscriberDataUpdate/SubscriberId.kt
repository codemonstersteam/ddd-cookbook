package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate
data class SubscriberId
private constructor(
    override val value: String
) : ValueObject<String> {
    companion object {
        fun emerge(subscriberId: String)
                : Result<SubscriberId> =
            when (isStringConsists9Digits(subscriberId)) {
                true -> Result.success(SubscriberId(subscriberId))
                else -> Result.failure(IllegalArgumentException("Subscriber Id consists of numbers maximum length 9"))
            }

        private val isStringConsist9Digits = "^\\d{1,6}\$".toRegex()
        private fun isStringConsists9Digits(value: String) =
            isStringConsist9Digits.matches(value)
    }
}