package by.bsuir.poit.kosten.rss_reader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView

private const val ARG_LINK = "link"

class NewsFragment : Fragment() {

    private lateinit var newsWebView: WebView
    private var link: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            link = it.getString(ARG_LINK)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        newsWebView = view.findViewById(R.id.news_web_view)
        newsWebView.loadUrl(link)
        newsWebView.setInitialScale(100)
        newsWebView.settings.builtInZoomControls = true

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(link: String) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LINK, link)
                }
            }
    }
}