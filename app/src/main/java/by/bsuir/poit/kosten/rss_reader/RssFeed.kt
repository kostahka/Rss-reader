package by.bsuir.poit.kosten.rss_reader

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.text.DateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

@Entity
data class RssFeed(@PrimaryKey val id : UUID = UUID.randomUUID(),
    var name:String = "",
    var link:String = "",
    var isFile:Boolean = false) {

    fun getRssItems(context: Context):List<RssItem>{
        val rssItems = ArrayList<RssItem>()
        var inputStream: InputStream? = null
        if(isFile){
            try {
                inputStream = RssFeedRepository.getExternalFile(link).inputStream()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else{
            try {
                val url = URL(link)
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (conn.responseCode === HttpURLConnection.HTTP_OK)
                    inputStream = conn.inputStream
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        if (inputStream != null) {
            try {
                val dbf: DocumentBuilderFactory = DocumentBuilderFactory
                    .newInstance()
                val db: DocumentBuilder = dbf.newDocumentBuilder()

                val document: Document = db.parse(inputStream)
                val element: Element = document.documentElement

                val nodeList: NodeList = element.getElementsByTagName("item")
                if (nodeList.length > 0) {
                    for (i in 0 until nodeList.length) {

                        val entry: Element = nodeList.item(i) as Element
                        val titleE: Element = entry.getElementsByTagName(
                            "title"
                        ).item(0) as Element
                        val descriptionE: Element = entry
                            .getElementsByTagName("description").item(0) as Element
                        val pubDateE: Element = entry
                            .getElementsByTagName("pubDate").item(0) as Element
                        val linkE: Element = entry.getElementsByTagName(
                            "link"
                        ).item(0) as Element
                        val title: String = titleE.firstChild.nodeValue
                        val description: String = descriptionE.firstChild.nodeValue

                        val df = DateFormat.getInstance()
                        val pubDate = Date(pubDateE.firstChild.nodeValue)
                        val link: String = linkE.firstChild.nodeValue

                        val rssItem = RssItem(
                            title, description,
                            pubDate, link
                        )
                        rssItems.add(rssItem)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                inputStream.close()
            }
        }
        return rssItems
    }
}