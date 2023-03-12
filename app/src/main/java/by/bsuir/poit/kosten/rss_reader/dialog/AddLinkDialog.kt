package by.bsuir.poit.kosten.rss_reader.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import by.bsuir.poit.kosten.rss_reader.R

class AddLinkDialog(private val listener: AddLinkListener):DialogFragment() {

    interface AddLinkListener{
        fun onSaveClick(link: String);
    }

    private lateinit var linkEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogGreen)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.rss_feed_add_link, null)

            linkEditText = view.findViewById(R.id.rss_feed_link)

            builder
                .setView(view)
                .setTitle(R.string.rss_add_link)
                .setPositiveButton(R.string.save) { _, _ ->
                    listener.onSaveClick(linkEditText.text.toString())
                }
                .setNeutralButton(R.string.cancel, null)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        return dialog
    }
}