package com.example.m19.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AttractionDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_ATTRACTION = "attraction"

        fun newInstance(attraction: Attraction): AttractionDialogFragment {
            val args = Bundle()
            args.putParcelable(ARG_ATTRACTION, attraction)
            val fragment = AttractionDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val attraction = arguments?.getParcelable<Attraction>(ARG_ATTRACTION)!!

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(attraction.name)
            .setMessage(attraction.title)
            .setPositiveButton("ОК") { dialog, id ->
                dialog.dismiss()
            }

        return builder.create()
    }
}

