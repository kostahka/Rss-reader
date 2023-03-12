package by.bsuir.poit.kosten.rss_reader.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import by.bsuir.poit.kosten.rss_reader.RssFeed
import java.util.UUID

@Dao
interface RssFeedDao {
    @Query("SELECT * FROM rssfeed")
    fun getRssFeeds(): LiveData<List<RssFeed>>

    @Query("SELECT * FROM rssfeed WHERE id=(:id)")
    fun getRssFeed(id: UUID): LiveData<RssFeed>

    @Insert
    fun insertRssFeed(rssFeed: RssFeed)

    @Delete
    fun deleteRssFeed(rssFeed: RssFeed)
}