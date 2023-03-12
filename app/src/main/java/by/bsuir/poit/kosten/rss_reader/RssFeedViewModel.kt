package by.bsuir.poit.kosten.rss_reader

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RssFeedViewModel:ViewModel() {
    private val rssFeedRepository = RssFeedRepository.get()
    val rssFeeds: LiveData<List<RssFeed>> = rssFeedRepository.getRssFeeds()

    fun addRssFeed(rssFeed: RssFeed){
        rssFeedRepository.insertRssFeed(rssFeed)
    }

    fun writeRssFeedFile(uri: Uri){
        rssFeedRepository.writeRssFeedFile(uri)
    }

    fun deleteRssFeed(rssFeed: RssFeed){
        rssFeedRepository.deleteRssFeed(rssFeed)
    }
}