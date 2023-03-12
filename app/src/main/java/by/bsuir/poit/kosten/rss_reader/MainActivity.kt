package by.bsuir.poit.kosten.rss_reader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), RssFeedListFragment.Callbacks, RssFragment.Callbacks {

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val currentFragment = supportFragmentManager.findFragmentById(
            R.id.fragment_container
        )

        if(currentFragment == null){
            val fragment = RssFeedListFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onRssSelected(rssFeedId: UUID) {
        val fragment = RssFragment.newInstance(rssFeedId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNewsSelected(link: String) {
        val fragment = NewsFragment.newInstance(link)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}