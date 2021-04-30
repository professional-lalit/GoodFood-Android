package com.goodfood.app.ui.common.dialogs

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodfood.app.R
import com.goodfood.app.databinding.DialogNotificationBottomBinding
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.Message
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Created by Lalit N. Hajare (Android Developer) on 12/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class NotificationBottomDialog : BottomSheetDialogFragment() {

    companion object {

        const val TAG = "NotificationBottomDialog"
        private const val TIMED_DISMISSAL_VALUE = 2000L

        var isShown = false

        fun getInstance(
            title: String,
            isTimedDismissal: Boolean? = false
        ): NotificationBottomDialog {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putBoolean("is_timed", isTimedDismissal!!)
            return NotificationBottomDialog().apply { arguments = bundle }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isShown = true
    }

    override fun onDetach() {
        super.onDetach()
        isShown = false
    }

    private lateinit var binding: DialogNotificationBottomBinding

    private lateinit var title: String
    private var isTimedDismissal: Boolean = false

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        title = args?.getString("title") ?: ""
        isTimedDismissal = args?.getBoolean("is_timed") ?: false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        isCancelable = false
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNotificationBottomBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtNotificationTitle.text = title

        if (isTimedDismissal) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, TIMED_DISMISSAL_VALUE)
        }
    }

    @Subscribe
    open fun onEvent(event: Message) {
        if (event.eventId == EventConstants.Event.UPDATE_BOTTOM_NOTIFICATION_TITLE.id) {
            binding.txtNotificationTitle.text = event.payload as String
        }
    }

}