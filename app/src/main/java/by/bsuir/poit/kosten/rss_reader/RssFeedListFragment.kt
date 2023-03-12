package by.bsuir.poit.kosten.rss_reader

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.bsuir.poit.kosten.rss_reader.dialog.AddLinkDialog
import by.bsuir.poit.kosten.rss_reader.dialog.AddRssFeedDialog
import java.util.*

class RssFeedListFragment : Fragment(), AddRssFeedDialog.AddRssFeedListener
    , AddLinkDialog.AddLinkListener{

    private val openFileLauncher = registerForActivityResult(FileActivityResultContract()){
        result->
        if(result != null){
            rssFeedViewModel.writeRssFeedFile(result)
        }
    }

    interface Callbacks{
        fun onRssSelected(rssFeedId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var rssFeedsRecyclerView : RecyclerView
    private var adapter = RssFeedAdapter(emptyList())

    private val rssFeedViewModel by lazy {
        ViewModelProvider(this)[RssFeedViewModel::class.java]
    }

    companion object {
        fun newInstance() = RssFeedListFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callbacks = context as Callbacks
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.rss_add ->{

                val dialog = AddRssFeedDialog(this)
                dialog.show(parentFragmentManager, "add")
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_rss_feed_list, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_rss_feed_list, container, false)

        rssFeedsRecyclerView = view.findViewById(R.id.rss_feeds_recycler_view)
        rssFeedsRecyclerView.layoutManager = LinearLayoutManager(context)
        rssFeedsRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rssFeedViewModel.rssFeeds.observe(viewLifecycleOwner){
            updateUI(it)
        }
    }

    private fun updateUI(rssFeeds: List<RssFeed>){
        adapter = RssFeedAdapter(rssFeeds)
        rssFeedsRecyclerView.adapter = adapter
    }

    private inner class RssFeedHolder(view: View) : ViewHolder(view), OnClickListener{
        private val rssFeedLinkTextView: TextView = itemView.findViewById(R.id.rss_feed_link)
        private val rssFeedDeleteButton: Button = itemView.findViewById(R.id.rss_feed_delete)
        private lateinit var rssFeed: RssFeed

        init {
            itemView.setOnClickListener(this)
            rssFeedDeleteButton.setOnClickListener {
                rssFeedViewModel.deleteRssFeed(rssFeed)
            }
        }

        fun bind(rssFeed: RssFeed){
            this.rssFeed = rssFeed

            rssFeedLinkTextView.text = rssFeed.name
        }

        override fun onClick(v: View?) {
            callbacks?.onRssSelected(rssFeed.id)
        }
    }

    private inner class RssFeedAdapter(var rssFeeds: List<RssFeed>):Adapter<RssFeedHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssFeedHolder {
            val view = layoutInflater.inflate(R.layout.rss_feed_element, parent, false)
            return RssFeedHolder(view)
        }

        override fun onBindViewHolder(holder: RssFeedHolder, position: Int) {
            val rssFeed = rssFeeds[position]
            holder.bind(rssFeed)
        }

        override fun getItemCount(): Int {
            return rssFeeds.size
        }

    }

    override fun onDialogButtonGetFile() {
        openFileLauncher.launch(null)
    }

    override fun onDialogButtonGetLink() {
        val dialog = AddLinkDialog(this)
        dialog.show(parentFragmentManager, "add_link")
    }

    override fun onSaveClick(link: String) {
        val rssFeed = RssFeed()
        rssFeed.link = link
        rssFeed.isFile = false
        val uri = Uri.parse(link)
        rssFeed.name = uri.host + "\n" + uri.lastPathSegment
        rssFeedViewModel.addRssFeed(rssFeed)
    }

}