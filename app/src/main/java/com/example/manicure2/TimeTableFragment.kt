package com.example.manicure2

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.example.manicure2.databinding.FragmentTimetableBinding
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.yield
import java.util.*


private const val DEBUG_TAG = "TimeTableFragment"

class TimeTableFragment(private val calendar: Calendar, private val mDetector: GestureDetectorCompat, private val scrollY: Int = 0) : Fragment() {

    lateinit var binding: FragmentTimetableBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimetableBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayOf(
            binding.dateMonday,
            binding.dateTuesday,
            binding.dateWednesday,
            binding.dateThursday,
            binding.dateFriday,
            binding.dateSaturday,
            binding.dateSunday
        ).forEachIndexed { index, appCompatTextView ->
            if (index == 0) appCompatTextView.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            else appCompatTextView.text = calendar.apply { set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1) }
                .get(Calendar.DAY_OF_MONTH).toString()
        }

        binding.scrollView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View, p1: MotionEvent): Boolean {
                return mDetector.onTouchEvent(p1)
            }
        })

        Log.d(DEBUG_TAG, "Runnable created")
        binding.root.post (Runnable {
            binding.scrollView.scrollTo(0, scrollY)
            Log.d(DEBUG_TAG, "Runnable run, y=${scrollY}")})

    }

    fun getScrollY() : Int{
        return binding.scrollView.scrollY
    }
}