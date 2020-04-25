package com.gofedora.myreminder.fragments

import android.os.Bundle

interface FragmentCallback {
    companion object {
        const val ACTION_KEY = "ACTION_KEY"
        const val REMINDER = "REMINDER"
        const val DATE = "DATE"
        const val TIME = "TIME"
        const val REMINDER_CLICKED = 1
        const val DATE_PICKER_CLICKED = 2
        const val DATE_SELECTED = 3
        const val TIME_PICKER_CLICKED = 4
        const val TIME_SELECTED = 5
        const val SAVE_REMINDER_CLICKED = 6
        const val DELETE_REMINDER_CLICKED = 7
    }

    fun onActionPerformed(bundle: Bundle)
}