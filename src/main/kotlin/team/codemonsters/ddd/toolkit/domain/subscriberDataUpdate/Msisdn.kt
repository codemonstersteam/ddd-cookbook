package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

data class Msisdn private constructor(override val value: String): ValueObject<String> {
    companion object {
        fun emerge(msisdn: String)
                : Result<Msisdn> =
            when (isStringConsists10Digits(msisdn)) {
                true -> Result.success(Msisdn(msisdn))
                else -> Result.failure(IllegalArgumentException("Msisdn consists of 10 numbers"))
            }

        private fun isStringConsists10Digits(value: String) =
            10 == value.length
                    && value.lineSequence().all { it in "0".."9" }
    }
}