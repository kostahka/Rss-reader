package by.bsuir.poit.kosten.rss_reader

import android.annotation.SuppressLint
import org.w3c.dom.*
import java.io.*
import java.net.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.xml.parsers.*
import java.util.*

data class RssItem (val title:String, val description: String,
    val pubDate: Date, val link: String){

    @SuppressLint("SimpleDateFormat")
    fun getFormatDate():String{
        val sdf = SimpleDateFormat("MM/dd - hh:mm:ss")
        return sdf.format(pubDate)
    }
}