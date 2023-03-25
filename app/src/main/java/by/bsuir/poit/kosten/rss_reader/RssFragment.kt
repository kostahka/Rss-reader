package by.bsuir.poit.kosten.rss_reader

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.bsuir.poit.kosten.rss_reader.dialog.DateDialog
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_RSS_FEED = "rss_feed"

class RssFragment : Fragment(), DateDialog.Callbacks{

    interface Callbacks{
        fun onNewsSelected(link: String)
    }

    private var callbacks: Callbacks? = null

    private lateinit var rssListView: ListView
    private lateinit var rssSearchEditText: EditText
    private var adapter = RssAdapter(emptyList())

    private lateinit var datePickButton: Button
    private lateinit var dateUnpickButton: Button
    private var filterDate:Date? = null

    companion object {
        fun newInstance(rssFeedId: UUID):RssFragment{
            val args = Bundle().apply{
                putSerializable(ARG_RSS_FEED, rssFeedId)
            }
            return RssFragment().apply {
                arguments = args
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel.context = context
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rssFeedId = arguments?.getSerializable(ARG_RSS_FEED) as UUID

        viewModel.loadItems(rssFeedId)
    }

    private val viewModel by lazy{
        ViewModelProvider(this)[RssViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_rss, container, false)

        datePickButton = view.findViewById(R.id.date_pick_button)
        datePickButton.setOnClickListener {
            DateDialog(this).show(parentFragmentManager, "Date_picker")
        }

        dateUnpickButton = view.findViewById(R.id.date_unpick_button)
        dateUnpickButton.setOnClickListener {
            filterDate = null
            adapter.setDateFilter(null)
            datePickButton.setText(R.string.date_pick)
        }

        rssListView = view.findViewById(R.id.rss_list_view)
        rssListView.adapter = adapter

        rssSearchEditText = view.findViewById(R.id.rss_search)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rssListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                callbacks?.onNewsSelected(adapter.filteredItems[position].link)
        }

        viewModel.rssItemsLiveData.observe(viewLifecycleOwner){
            adapter.setItems(it)
        }

        val searchWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.setFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        rssSearchEditText.addTextChangedListener(searchWatcher)

    }

    private inner class RssAdapter(private var items: List<RssItem>) : BaseAdapter(){
        var filteredItems = items;
        private var filter:String = ""
        private var dateFilter: Date? = null
        override fun getCount(): Int {
            return filteredItems.size
        }

        override fun getItem(position: Int): Any {
            return filteredItems[position];
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = layoutInflater.inflate(R.layout.rss_element, parent, false)
            }

            val item = getItem(position) as RssItem
            view!!.findViewById<TextView>(R.id.rss_item_title).text = item.title
            view.findViewById<TextView>(R.id.rss_item_date).text = item.getFormatDate()
            view.findViewById<TextView>(R.id.rss_item_description).text = item.description

            return view
        }

        private fun filter(){
            filteredItems = if(filter == "")
                items
            else
                items.filter { it.title.contains(filter, true) }

            val calendar1 = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()
            if (dateFilter != null){
                calendar1.time = dateFilter
                filteredItems = filteredItems.filter { calendar2.time = it.pubDate
                    calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                            && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)}
            }

        }

        fun setFilter(filter:String){
            this.filter = filter
            filter()

            notifyDataSetChanged()
        }

        fun setDateFilter(date:Date?){
            this.dateFilter = date
            filter()

            notifyDataSetChanged()
        }

        fun setItems(items: List<RssItem>){
            this.items = items
            filter()

            notifyDataSetChanged()
        }

    }

    override fun onDateSelected(date: Date) {
        filterDate = date
        val sdf = SimpleDateFormat("MM/dd - hh:mm:ss")
        datePickButton.text = sdf.format(date)
        adapter.setDateFilter(date)
    }

}