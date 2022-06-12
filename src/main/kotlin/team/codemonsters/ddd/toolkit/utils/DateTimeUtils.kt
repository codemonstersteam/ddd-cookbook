package team.codemonsters.ddd.toolkit.utils

import java.time.LocalDateTime
import java.time.ZoneOffset

class DateTimeUtils {

    companion object {
        fun nowToZonedLocalDataTime(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
        fun nowToEpochMill(): Long = nowToZonedLocalDataTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}