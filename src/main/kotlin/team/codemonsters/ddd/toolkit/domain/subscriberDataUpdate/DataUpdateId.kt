package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate


data class DataUpdateId
private constructor(override val value: String) : ValueObject<String> {
    companion object {
        fun emerge(dataUpdateId: String): Result<DataUpdateId> =
            when (isStringWith9Digits(dataUpdateId)) {
                true -> Result.success(DataUpdateId(dataUpdateId))
                else -> Result.failure(IllegalArgumentException("Data update Id consists of numbers maximum length 9"))
            }

        private val isStringConsist9Digits = "^\\d{1,9}\$".toRegex()

        private fun isStringWith9Digits(value: String) =
            isStringConsist9Digits.matches(value)
    }
}