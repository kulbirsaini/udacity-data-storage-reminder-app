package com.gofedora.myreminder

import android.os.Bundle

interface FragmentCallback {
    companion object {
        const val ALARM = "ALARM"
        const val ACTION_KEY = "ACTION_KEY"
        const val ALARM_CLICKED = 1
    }

    fun onActionPerformed(bundle: Bundle)
}