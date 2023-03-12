package by.bsuir.poit.kosten.rss_reader.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import by.bsuir.poit.kosten.rss_reader.R

class AddRssFeedDialog(private val listener:AddRssFeedListener):DialogFragment() {

    interface AddRssFeedListener{
        fun onDialogButtonGetFile()
        fun onDialogButtonGetLink()
    }

    private lateinit var getFileButton:Button
    private lateinit var getLinkButton:Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogGreen)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.rss_feed_add, null)

            getFileButton = view.findViewById(R.id.rss_feed_file_button)
            getLinkButton = view.findViewById(R.id.rss_feed_link_button)

            getFileButton.setOnClickListener {
                listener.onDialogButtonGetFile()
            }

            getLinkButton.setOnClickListener {
                listener.onDialogButtonGetLink()
            }

            builder
                .setView(view)
                .setTitle(R.string.rss_add)
                .setNeutralButton(R.string.close, null)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")



        return dialog
    }
}