package by.bsuir.poit.kosten.rss_reader

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.Room
import by.bsuir.poit.kosten.rss_reader.database.RssFeedDatabase
import java.io.File
import java.io.InputStream
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "rssDB"

class RssFeedRepository private constructor(context: Context){
    private val database:RssFeedDatabase = Room.databaseBuilder(context.applicationContext,
    RssFeedDatabase::class.java, DATABASE_NAME).build()

    private val rssFeedDao = database.rssFeedDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getRssFeeds():LiveData<List<RssFeed>>
    {
        return rssFeedDao.getRssFeeds()
    }

    fun getRssFeed(id:UUID):LiveData<RssFeed>
    {
        return rssFeedDao.getRssFeed(id)
    }

    fun deleteRssFeed(rssFeed: RssFeed)
    {
        executor.execute {
            rssFeedDao.deleteRssFeed(rssFeed)
        }
    }

    fun insertRssFeed(rssFeed: RssFeed)
    {
        executor.execute {
            rssFeedDao.insertRssFeed(rssFeed)
        }
    }

    fun writeRssFeedFile(uri: Uri){
        executor.execute {
            var inputStream: InputStream? = null
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                val name = uri.lastPathSegment!!.substringAfterLast("/")

                val file = getExternalFile(name)
                val bytes = inputStream!!.readBytes()

                file.writeBytes(bytes)

                val rssFeed = RssFeed()
                rssFeed.isFile = true
                rssFeed.link = name
                rssFeed.name = name
                insertRssFeed(rssFeed)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
            finally {
                inputStream?.close()
            }
        }
    }

    companion object{
        lateinit var context:Context
        fun getExternalFile(name: String): File {
            return File(context.getExternalFilesDir(null), name)
        }

        private var INSTANCE: RssFeedRepository? = null
        public fun init(context: Context){
            this.context = context
            if(INSTANCE == null){
                INSTANCE = RssFeedRepository(context)
            }
        }

        public fun get(): RssFeedRepository{
            return INSTANCE?:
            throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}