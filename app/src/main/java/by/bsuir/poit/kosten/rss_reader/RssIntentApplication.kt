package by.bsuir.poit.kosten.rss_reader

import android.app.Application

class RssIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RssFeedRepository.init(this)
    }
}