package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

data class Msisdn private constructor(override val value: String): ValueObject<String> {
    companion object {
        fun emerge(msisdn: String)
                : Result<Msisdn> =
            when (isStringConsists10Digits(msisdn)) {
                true -> Result.success(Msisdn(msisdn))
                else -> Result.failure(IllegalArgumentException("Msisdn consists of 10 numbers"))
            }

        private val isStringConsist10Digits = "^\\d{10}\$".toRegex()

        private fun isStringConsists10Digits(value: String) =
            isStringConsist10Digits.matches(value)

    }
}