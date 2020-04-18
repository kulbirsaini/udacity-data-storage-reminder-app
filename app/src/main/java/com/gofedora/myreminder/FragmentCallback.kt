package com.gofedora.myreminder

import android.os.Bundle

interface FragmentCallback {
    companion object {
        const val ALARM = "ALARM"
        const val ACTION_KEY = "ACTION_KEY"
        const val DATE = "DATE"
        const val TIME = "TIME"
        const val ALARM_CLICKED = 1
        const val DATE_PICKER_CLICKED = 2
        const val TIME_PICKER_CLICKED = 3
        const val DATE_SELECTED = 4
        const val TIME_SELECTED = 5
    }

    fun onActionPerformed(bundle: Bundle)
}