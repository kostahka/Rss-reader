package by.bsuir.poit.kosten.rss_reader.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.util.*

class DateDialog(private val targetFrag: Fragment) : DialogFragment(){
    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    private val dateListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int,
                                             month: Int, day: Int ->
            val resultDate: Date =
                GregorianCalendar(year, month, day).time
            targetFrag?.let { fragment ->
                (fragment as
                        Callbacks).onDateSelected(resultDate)
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }
}