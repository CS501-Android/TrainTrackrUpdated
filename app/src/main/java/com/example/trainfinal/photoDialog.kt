package com.example.trainfinal

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
class photoDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            // Ideally populate it with another layout like one of the homework
            builder.setMessage("Take a photo")
                .setPositiveButton("Start") { dialog, id ->
                    val transaction =
                        parentFragment?.parentFragmentManager
                        ?.beginTransaction()
                            ?.replace(R.id.fragment_container, Camera())
                            ?.commit()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // User cancelled the dialog.
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
