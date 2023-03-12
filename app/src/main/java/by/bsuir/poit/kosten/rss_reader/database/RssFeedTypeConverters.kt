package by.bsuir.poit.kosten.rss_reader.database

import androidx.room.TypeConverter
import java.util.*

class RssFeedTypeConverters {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}