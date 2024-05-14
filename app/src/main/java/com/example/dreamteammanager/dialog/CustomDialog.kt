package com.example.dreamteammanager.dialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.dreamteammanager.databinding.FragmentCustomDialogBinding


class CustomDialogFragment : DialogFragment() {
    lateinit var binding: FragmentCustomDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomDialogBinding.inflate(inflater, container, false)
        val view: View = binding.getRoot()

        // Set click listeners for buttons
        binding.yesButton.setOnClickListener(View.OnClickListener { // Handle 'Yes' button click
            // E.g., dismiss dialog, perform action, etc.
            dismiss()
        })

        binding.noButton.setOnClickListener(View.OnClickListener { // Handle 'No' button click
            // E.g., dismiss dialog, perform action, etc.
            dismiss()
        })

        return view
    }


}
