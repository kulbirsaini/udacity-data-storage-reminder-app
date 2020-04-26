package com.gofedora.myreminder.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gofedora.myreminder.MainActivity
import com.gofedora.myreminder.fragments.FragmentCallback

/**
 * We are using DialogFragment to show alerts so that we can survive orientation, screen size and other config changes.
 * DialogFragment takes care of the dialog lifecycle.
 */
class MyAlertDialog: DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var callback: FragmentCallback

    /**
     * Override onActivityCreated so that we can reassign the callback
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.callback = activity as MainActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val titleResource = arguments?.getInt(FragmentCallback.ALERT_TITLE)
        val messageResource = arguments?.getInt(FragmentCallback.ALERT_MESSAGE)

        // If we don't have title and message resources, we can't really create a dialog
        if (titleResource != null && messageResource != null) {
            context?.let {
                return AlertDialog.Builder(it)
                    .setTitle(getString(titleResource))
                    .setMessage(getString(messageResource))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, this)
                    .setNegativeButton(android.R.string.no, this)
                    .create()
            }
        }

        return super.onCreateDialog(savedInstanceState)
    }

    /**
     * This method is invoked when button on the alert dialog are clicked
     */
    override fun onClick(dialog: DialogInterface?, which: Int) {
        // arguments should already contain the fields that were passed while creating dialog
        val bundle = arguments?: Bundle()

        this.callback.onActionPerformed(bundle.apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.ALERT_ACTION_PERFORMED)
            putInt(FragmentCallback.ALERT_BUTTON, which)
        })
    }
}