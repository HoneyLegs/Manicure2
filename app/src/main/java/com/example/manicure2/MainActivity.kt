package com.example.manicure2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.FragmentManager
import com.example.manicure2.databinding.ActivityMainBinding
import java.util.*

private const val DEBUG_TAG = "Activity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var currentCalendar = Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) }

    private lateinit var fm : FragmentManager
    private lateinit var mDetector: GestureDetectorCompat

    private lateinit var currentFragment : TimeTableFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDetector = GestureDetectorCompat(this, MyGestureListener())

        fm = supportFragmentManager

        currentFragment = TimeTableFragment(
            currentCalendar.clone() as Calendar,
            mDetector
        )
        fm.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
            .add(R.id.container, currentFragment)
            .commit()

        updateMonth()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun getAnotherCalendarWeek(currentCalendar : Calendar, countFromCurrentWeek : Int) : Calendar =
        (currentCalendar.clone() as Calendar).apply {
            set(
                Calendar.WEEK_OF_YEAR,
                get(Calendar.WEEK_OF_YEAR) + countFromCurrentWeek,
            )
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

    private inner class MyGestureListener: GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val flingDistanceX = 30
            val flingDistanceY = 50

            if (e2.x - e1.x > flingDistanceX && kotlin.math.abs(e2.y - e1.y) < flingDistanceY)
            {
                currentCalendar = getAnotherCalendarWeek(currentCalendar, -1)
                currentFragment = TimeTableFragment(
                    (currentCalendar.clone() as Calendar),
                    mDetector,
                    currentFragment.getScrollY()
                )
                fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_from_left_to_centre, R.anim.slide_from_centre_to_right, R.anim.slide_from_right_to_center, R.anim.slide_from_center_to_left)
                    .replace(R.id.container, currentFragment)
                    .commit()

                updateMonth()
                return true
            }
            //findViewById<>()

            if (e2.x - e1.x < flingDistanceX && kotlin.math.abs(e2.y - e1.y) < flingDistanceY)
            {
                currentCalendar = getAnotherCalendarWeek(currentCalendar, 1)
                currentFragment = TimeTableFragment(
                    (currentCalendar.clone() as Calendar),
                    mDetector,
                    currentFragment.getScrollY()
                )
                fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_from_right_to_center, R.anim.slide_from_center_to_left, R.anim.slide_from_left_to_centre, R.anim.slide_from_centre_to_right)
                    .replace(R.id.container, currentFragment)
                    .commit()

                updateMonth()
                return true
            }
            return false
        }
    }

    fun updateMonth(){
        binding.tvMonthYear.text= currentCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("ru"))
    }
}