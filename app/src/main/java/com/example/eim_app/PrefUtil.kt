package com.example.eim_app

import android.content.Context

import androidx.preference.PreferenceManager.getDefaultSharedPreferences


class PrefUtil {
    companion object {

        fun getTimerLength(context: Context): Int{
            //placeholder
            val preferences = getDefaultSharedPreferences(context)
            return preferences.getInt("time for recipe", 0)
//            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }


        private const val TIMER_STATE_ID = "timer_state"

        fun getTimerState(context: Context): Timer.TimerState{
            val preferences = getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return Timer.TimerState.values()[ordinal]
        }

        fun setTimerState(state: Timer.TimerState, context: Context){
            val editor = getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }


        private const val SECONDS_REMAINING_ID = "seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val preferences = getDefaultSharedPreferences(context)
            return  preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}