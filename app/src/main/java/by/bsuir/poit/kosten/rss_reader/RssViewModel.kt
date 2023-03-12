package by.bsuir.poit.kosten.rss_reader

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*
import java.util.concurrent.Executors

class RssViewModel : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()

    lateinit var context: Context

    private val rssFeedRepository = RssFeedRepository.get()
    private val rssFeedIdLiveData = MutableLiveData<UUID>()

    private val rssFeedLiveData:LiveData<RssFeed> = Transformations.switchMap(rssFeedIdLiveData){
        rssFeedId ->
            rssFeedRepository.getRssFeed(rssFeedId);
    }

    val rssItemsLiveData:LiveData<List<RssItem>> = Transformations.switchMap(rssFeedLiveData){
        rssFeed ->
            loadItems(rssFeed)
    }


    fun loadItems(id:UUID){
        rssFeedIdLiveData.postValue(id)
    }

    private fun loadItems(rssFeed: RssFeed):LiveData<List<RssItem>>{
        val itemsLiveData = MutableLiveData<List<RssItem>>()

        executor.execute {
            itemsLiveData.postValue(rssFeed.getRssItems(context))
        }

        return itemsLiveData
    }

}