package by.bsuir.poit.kosten.rss_reader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.bsuir.poit.kosten.rss_reader.RssFeed

@Database(entities = [RssFeed::class], version = 1)
@TypeConverters(RssFeedTypeConverters::class)
abstract class RssFeedDatabase: RoomDatabase() {
    abstract fun rssFeedDao(): RssFeedDao
}