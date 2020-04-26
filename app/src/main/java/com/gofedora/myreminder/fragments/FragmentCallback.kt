package com.gofedora.myreminder.fragments

import android.os.Bundle

/**
 * Interface to facilitate communication between activity and fragments
 */
interface FragmentCallback {
    companion object {
        const val ACTION_KEY = "ACTION_KEY"

        // Constants related to AlertDialogs
        const val ALERT_BUTTON = "ALERT_BUTTON"
        const val ALERT_MESSAGE = "ALERT_MESSAGE"
        const val ALERT_TITLE = "ALERT_TITLE"
        const val ALERT_TYPE = "ALERT_TYPE"
        const val ALERT_ACTION_PERFORMED = 10
        const val ALERT_TYPE_DELETE = 11
        const val ALERT_TYPE_DELETE_ALL = 12

        // Constants related to Date/Time Pickers
        const val DATE = "DATE"
        const val TIME = "TIME"
        const val DATE_CLICKED = 20
        const val DATE_SELECTED = 21
        const val TIME_CLICKED = 22
        const val TIME_SELECTED = 23

        // Constants related to fragments
        const val FRAGMENT_TYPE = "FRAGMENT_TYPE"
        const val REMINDER_FRAGMENT_RENDERED = 30
        const val REMINDER_FRAGMENT_EDIT = 31
        const val REMINDER_FRAGMENT_LIST = 32

        // Constants related to reminder actions
        const val REMINDER = "REMINDER"
        const val REMINDER_CLICKED = 40
        const val SAVE_REMINDER_CLICKED = 41
        const val DELETE_REMINDER_CLICKED = 42
    }

    fun onActionPerformed(bundle: Bundle)
}