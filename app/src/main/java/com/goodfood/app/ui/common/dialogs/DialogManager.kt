package com.goodfood.app.ui.common.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.goodfood.app.common.Constants
import com.goodfood.app.databinding.DialogLogoutBinding
import com.goodfood.app.databinding.DialogProfilePicSelectionBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 20/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

/**
 * Manages various dialogues for various purposes.
 */
class DialogManager constructor(private val context: Context) {

    private var dialogFragment: DialogFragment? = null

    fun closeDialog() {
        if (dialogFragment?.isAdded == true) {
            dialogFragment?.dismiss()
        }
    }

    fun showProfilePicDialog(fragmentManager: FragmentManager, callback: (Int) -> Unit) {
        val binding = DialogProfilePicSelectionBinding.inflate(LayoutInflater.from(context))
        binding.btnSelect.setOnClickListener {
            if (binding.rbCamera.isChecked) {
                callback.invoke(Constants.CAMERA_SELECTED)
            } else {
                callback.invoke(Constants.GALLERY_SELECTED)
            }
            dialogFragment!!.dismiss()
        }
        dialogFragment = CustomDialogFragment(binding)
        dialogFragment!!.show(fragmentManager, CustomDialogFragment.TAG)
    }

    fun showLogoutDialog(fragmentManager: FragmentManager, callback: (Boolean) -> Unit) {
        val binding = DialogLogoutBinding.inflate(LayoutInflater.from(context))
        binding.btnNo.setOnClickListener {
            callback.invoke(false)
            dialogFragment!!.dismiss()
        }
        binding.btnYes.setOnClickListener {
            callback.invoke(true)
            dialogFragment!!.dismiss()
        }
        dialogFragment = CustomDialogFragment(binding)
        dialogFragment!!.isCancelable = false
        dialogFragment!!.show(fragmentManager, CustomDialogFragment.TAG)
    }

}